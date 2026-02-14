package gt.umg.gestionCobros.utils;

import gt.umg.gestionCobros.exceptions.ErrorEnum;
import gt.umg.gestionCobros.exceptions.MSRinconException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
public class ReportUtil {
    public static final String EXCEL = "xls";
    public static final String PDF = "pdf";
    public static final String WORD = "doc";

    /**
     * Metodo que recibe cualquier tipo de coleccion de datos y lo exporta en el
     * tipo de docuemnto solicitado utilizando JasperReports.
     *
     *
     * @param data es la coleccion de datos a convertir en documento.
     * @param templateName es el nombre del formato de jasper report
     * (template.jrxml)
     * @param pagination
     * @param response es el objeto por el cual se enviara el docuemnto
     * generado.
     * @param type tipo de documento a exportar.
     * @param <T> Tipo de la información a plasmar en el reporte tiene que
     * extender de una colección de datos {@link Collection}
     */
    public static <T extends Collection> void crearReporte(T data, String templateName, boolean pagination, HttpServletResponse response, String type) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment");
        try {
            log.info("crearReporte: type: {}   :: template: {}", type, templateName);
            exportToOutputSteam(data, templateName, pagination, response.getOutputStream(), type);
        } catch (IOException ex) {
            log.error("Error crearReporte: {}",ex);
            throw new MSRinconException(ErrorEnum.D_ERROR_DOCUMENTO);
        }
    }

    /**
     * Metodo que recibe cualquier tipo de coleccion de datos y lo exporta en el
     * tipo de docuemnto solicitado utilizando JasperReports.
     *
     *
     * @param data es la coleccion de datos a convertir en documento.
     * @param templateName es el nombre del formato de jasper report
     * (template.jrxml)
     * @param pagination
     * @param type tipo de documento a exportar.
     * @param <T> Tipo de la información a plasmar en el reporte tiene que
     * extender de una colección de datos {@link Collection}
     * @return Una {@link MultipartFile}
     * @throws FileNotFoundException
     */
    public static <T extends Collection> MultipartFile crearReporteMultipart(T data, String templateName, boolean pagination, String type) throws IOException {

        String path = System.getProperty("java.io.tmpdir");
        String fileName = path + getFilePath(type);
        String name = getFilePath(type);

        try (OutputStream output = new FileOutputStream(new File(fileName))) {
            exportToOutputSteam(data, templateName, pagination, output, type);
            output.close();

            return new MultipartFileItem(fileName, name);

        } catch (IOException ex) {
            log.error("Error crearReporteMultipart: {}",ex);
            throw new MSRinconException(ErrorEnum.D_ERROR_DOCUMENTO);
        }
    }

    public static <T extends Collection> void exportToOutputSteam(T data, String templateName, boolean pagination, OutputStream outputStream, String type) {

        SimpleDateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormatHora = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String fechaActual = dateFormatFecha.format(date);
        String horaActual = dateFormatHora.format(date);

        try {
            log.info("exportToOutputSteam: type: {}   :: template: {}", type, templateName);
            InputStream jasperStream = ReportUtil.class.getResourceAsStream("/".concat(templateName));
            JasperDesign design = JRXmlLoader.load(jasperStream);
            log.info("Diseño del reporte cargado correctamente.");
            JasperReport report = JasperCompileManager.compileReport(design);
            log.info("Reporte compilado correctamente.");
            Map<String, Object> parameterMap = new HashMap();
            parameterMap.put("IS_IGNORE_PAGINATION", pagination);

            JRDataSource jRDataSource = new JRBeanCollectionDataSource(data);

            parameterMap.put("datasource", jRDataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameterMap, jRDataSource);
            if (jasperPrint.getPages().isEmpty()) {
                log.warn("JasperPrint no contiene páginas.");
            } else {
                log.info("JasperPrint contiene {} páginas.", jasperPrint.getPages().size());
            }
            switch (type) {
                case PDF:
                    log.info("PDF: type: {}   :: template: {}", type, templateName);
                    createPDFReport(jasperPrint, outputStream);
                    break;
                case EXCEL:
                    createExcelReport(jasperPrint, outputStream);
                    break;
                case WORD:
                    createWordReport(jasperPrint, outputStream);
            }
        } catch (JRException ex) {
            log.error("Error exportToOutputSteam: {}",ex);
            throw new MSRinconException(ErrorEnum.D_ERROR_DOCUMENTO);
        }
    }

    /**
     * Metodo que genera las clausulas where de un query en base a un objeto
     * (object), las genera a un query base (baseQuery) y crea el query con el
     * administrador de entidades (entityManager).
     *
     *
     * @param entityManager es el administrador de entidades utilizado para
     * crear el query.
     * @param baseQuery es el query base (antes de where) para la creación del
     * query.
     * @param object es la instancia del objeto que tiene la estructura de la
     * consulta.
     * @param type tipo del objeto que se retornará.
     * @return Query con los fitros calculados.
     *
     */
    public static Query createCompiledQuery(final EntityManager entityManager, final String baseQuery, final Object object, final Class type) {
        final Method[] allMethods = object.getClass().getDeclaredMethods();
        final Query q = entityManager.createNativeQuery(createQuery(baseQuery, object), type);

        for (Method method : allMethods) {
            try {
                final Column column = method.getDeclaredAnnotation(Column.class);
                if (column != null) {
                    final Object value = method.invoke(object);
                    if (column.forceAdd() || (value != null && !value.equals("") && !value.equals(0) && !value.equals(0.0) && !value.equals(0.0f))) {
                        q.setParameter(method.getName(), value);
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ReportUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return q;
    }

    private static void createExcelReport(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRXlsExporter export = new JRXlsExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(false);
        export.setConfiguration(configuration);
        export.exportReport();
    }

    private static void createWordReport(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRDocxExporter export = new JRDocxExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        export.exportReport();
    }

    private static void createPDFReport(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        log.info("createPDFReport: jasperPrint: {}   :: outputStream: {}", jasperPrint, outputStream);
        JRPdfExporter export = new JRPdfExporter();
        export.setExporterInput(new SimpleExporterInput(jasperPrint));
        export.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setCreatingBatchModeBookmarks(false);
        export.setConfiguration(configuration);
        export.exportReport();
    }

    private static <T> String createQuery(final String baseQuery, final T object) {
        final StringBuilder query = new StringBuilder(baseQuery);
        final List<String> order = new ArrayList<>();
        final Method[] allMethods = object.getClass().getDeclaredMethods();
        boolean isFirst = true;

        for (final Method method : allMethods) {
            try {
                final Column column = method.getDeclaredAnnotation(Column.class);
                if (column != null) {
                    final Object value = method.invoke(object);

                    if (value != null && !value.equals("") && !value.equals(0) && !value.equals(0.0) && !value.equals(0.0f)) {
                        query.append(isFirst ? " where " : " and ")
                                .append(column.table())
                                .append(".")
                                .append(column.name())
                                .append(" ")
                                .append(column.operator())
                                .append(" :")
                                .append(method.getName());
                        isFirst = false;
                    }
                    if (column.orderBy()) {
                        order.add(column.table() + "." + column.name());
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ReportUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        query.append(" order by ").append(order.stream().collect(Collectors.joining(" and ")));
        return query.toString();
    }

    private static String getFilePath(String type) {
        switch (type) {
            case PDF:
                return "Documento.pdf";
            case EXCEL:
                return "Documento.xlsx";
            case WORD:
                return "Documento.doc";
            default:
                return "";
        }
    }

    @Target(value = {ElementType.METHOD, ElementType.FIELD})
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface Column {

        String name() default "";

        String table() default "";

        String operator() default "=";

        boolean forceAdd() default false;

        boolean orderBy() default false;
    }
}
