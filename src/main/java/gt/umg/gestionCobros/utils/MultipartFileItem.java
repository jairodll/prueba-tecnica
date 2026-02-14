package gt.umg.gestionCobros.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultipartFileItem implements MultipartFile {

    private final byte[] fileContent;

    private final String fileName;

    private String contentType;

    private final File file;

    private final String destPath = System.getProperty("java.io.tmpdir");

    private FileOutputStream fileOutputStream;

    public MultipartFileItem(String originFilePath, String name) throws IOException {
        this.fileContent = Files.readAllBytes(Paths.get(originFilePath));
        this.fileName = name;
        file = new File(destPath + fileName);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(fileContent);
    }

    public void clearOutStreams() throws IOException {
        if (null != fileOutputStream) {
            fileOutputStream.flush();
            fileOutputStream.close();
            file.deleteOnExit();
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent != null && fileContent.length != 0;
    }

    @Override
    public long getSize() {
        return isEmpty() ? 0 : fileContent.length;
    }
}
