package gt.umg.gestionCobros.exceptions;

/**
 * @author amvraccot
 * @date 2/10/2024
 */

public enum ErrorEnum {

    //errores internos
    I_DESCONOCIDO                           (1000, EstadoHttp.INTERNAL_SERVER_ERROR, "Error desconocido, comuniquese con el administrador del sitio"),
    I_PRIMARY_KEY                           (1001, EstadoHttp.INTERNAL_SERVER_ERROR, "No se pudo crear la llave primaria para el nuevo registro"),
    I_OBJETO_NO_CREADO                      (1002, EstadoHttp.INTERNAL_SERVER_ERROR, "Error en la creación del objeto"),
    I_OBJETO_NULO                           (1003, EstadoHttp.INTERNAL_SERVER_ERROR, "Error al obtener la información para el objeto solicitado"),
    I_OBJETO_NO_ACTUALIZADO                 (1004, EstadoHttp.INTERNAL_SERVER_ERROR, "Error al actualizar el objeto"),
    I_OBJETO_INVALIDO                       (1004, EstadoHttp.INTERNAL_SERVER_ERROR, "El objeto es invalido"),
    I_ERROR_TRAER_OBJETOS                   (1005, EstadoHttp.INTERNAL_SERVER_ERROR, "Hubo un error al traer los objetos"),
    I_CONTADOR_EXIST                        (1005, EstadoHttp.BAD_REQUEST, "Ya existe el No. Contador registrado con otro usuario"),
    I_SIN_ACCESOS                           (1007, EstadoHttp.INTERNAL_SERVER_ERROR, "No cuenta con permisos suficientes para realizar dicha accion"),
    I_EMAIL_NOT_VALID                       (1007, EstadoHttp.INTERNAL_SERVER_ERROR, "Formato incorrecto"),
    I_EMAIL_ASIGNADO                        (1007, EstadoHttp.INTERNAL_SERVER_ERROR, "El correo electrónico ya cuenta con usuario creado"),
    I_EMAIL_ERROR                           (1007, EstadoHttp.INTERNAL_SERVER_ERROR, "Error en envio de correo electronico"),
    ASOCIACION_NOT_FOUND                    (1007, EstadoHttp.INTERNAL_SERVER_ERROR, "Asociacion no existe"),
    TOKEN_INVALIDO                    (1008, EstadoHttp.INTERNAL_SERVER_ERROR, "Token Inválido"),
    I_USUARIO_PASS_INCORRECTO               (1007, EstadoHttp.UNAUTHORIZED, "Usuario o contraseña incorrectos"),




    //errores con procesamiento JSON
    J_ERROR_PROCESAMIENTO_jSON              (1006, EstadoHttp.INTERNAL_SERVER_ERROR, "Problema con el JSON, formato corrupto"),

    //errores de recursos que no existen
    N_NO_EXISTE                             (2000, EstadoHttp.NOT_FOUND, "El servicio solicitado no existe"),
    N_OBJETO_NO_EXISTE                      (2001, EstadoHttp.NOT_FOUND, "El objeto solicitado no existe"),
    WS_ERROR_CONEXION_WS                    (2002, EstadoHttp.NOT_FOUND, "Servicio Web Fuera de Servicio"),

    //errores de seguridad
    S_DESCONOCIDO                           (3000, EstadoHttp.UNAUTHORIZED, "Credenciales de usuario inválidas."),
    S_USER_NOT_FOUND                        (3000, EstadoHttp.UNAUTHORIZED, "Usuario no asignado a ningún DAT"),

    D_ERROR_DOCUMENTO                       (4000, EstadoHttp.INTERNAL_SERVER_ERROR, "Error en generación de documento"),

    //Errores de Negocio
    T_ERORR_TARIFA_MORA                     (5000, EstadoHttp.BAD_REQUEST, "Ya existe una tarifa de mora"),
    T_ERORR_TARIFA_EXCESO                   (5001, EstadoHttp.BAD_REQUEST, "Ya existe una tarifa de exceso"),
    T_ERORR_TARIFA_CUOTA_FIJA               (5002, EstadoHttp.BAD_REQUEST, "Ya existe una cuota fija"),
    T_ERORR_TARIFA_RANGO                    (5003, EstadoHttp.BAD_REQUEST, "Ya existe una tarifa en el rango de consumos"),
    T_ERORR_TARIFA_RANGO_EXCESO             (5004, EstadoHttp.BAD_REQUEST, "Existe una tarifa de exceso dentro del rango de consumos"),
    B_ERROR_CREAR_US_MIGRACION                   (4003, EstadoHttp.BAD_REQUEST, "Error al crear usuario en migracion"),
    B_ERROR_CREAR_CONTADOR_MIGRACION                   (4004, EstadoHttp.BAD_REQUEST, "Error al crear usuario en migracion"),
    B_ERROR_CREAR_CONSUMO_MIGRACION                   (4003, EstadoHttp.BAD_REQUEST, "Error al crear usuario en migracion"),
    I_ERROR_LEER_DOCUMENTO                   (4005, EstadoHttp.BAD_REQUEST, "Error al leer el documento"),
    I_ERROR_EDIT_CONTADOR                   (4005, EstadoHttp.BAD_REQUEST, "El No. Contador y Cliente no se pueden editar al mismo tiempo"),
    I_ERROR_RUTA_CONTADOR                   (4005, EstadoHttp.BAD_REQUEST, "El No. Contador esta activo en la ruta: "),
    I_ERROR_USER_MASTER_APP                   (4006, EstadoHttp.BAD_REQUEST, "Error al crear el usuario Master"),
    I_ERROR_CREAR_MASTER_APP                   (4006, EstadoHttp.BAD_REQUEST, "Error al crear el Master App"),
    I_ERROR_MASTER_APP                   (4006, EstadoHttp.NOT_FOUND, "Error al encontrar el Master App"),
    ;

//______________________________________________________________________________
    /**
     * <p>Codigo de error.</p>
     */
    private final int codigo;
//______________________________________________________________________________
    /**
     * <p>Codigo de estado Http.</p>
     */
    private final int estadoHttp;
//______________________________________________________________________________
    /**
     * <p>Descripcion del error.</p>
     */
    private final String descripcion;
//______________________________________________________________________________
    /**
     * <p>Constructor de la enumeracion, establece el valor de los atributos.</p>
     *
     * @param pCodigo Codigo de error
     * @param pEstadoHttp Codigo de estado de Http.
     * @param pMensaje Descripcion de error
     */
    ErrorEnum(int pCodigo, int pEstadoHttp, String pMensaje) {
        this.codigo = pCodigo;
        this.estadoHttp = pEstadoHttp;
        this.descripcion = pMensaje;
    }
//______________________________________________________________________________
    /**
     * <p>Codigo que identifica el error de forma unica.</p>
     *
     * @return int Codigo de error
     */
    public int getCodigo() {
        return codigo;
    }
//______________________________________________________________________________
    /**
     * <p>Codigo de estado Http.</p>
     *
     * @return int Estado Http
     */
    public int getEstadoHttp() {
        return estadoHttp;
    }
//______________________________________________________________________________
    /**
     * <p>Descripcion del error, proporciona un punto de inicio para que el
     * usuario sepa que correcciones realizar.</p>
     *
     * @return String Descripcion del error
     */
    public String getDescripcion() {
        return descripcion;
    }
//______________________________________________________________________________
    /**
     * <p>Devuelve un texto indicando el tipo, codigo y descripcion del error.</p>
     *
     * @return String Representacion textual del error
     *//*
    @Override
    public String toString() {
        return "AOP-"
                .concat(String.valueOf(this.estadoHttp))
                .concat("-")
                .concat(String.valueOf(this.codigo))
                .concat(": ")
                .concat(this.descripcion);
    }*/
}
