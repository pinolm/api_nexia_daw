package cat.nexia.spring.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Classe per gestionar els fitxers
 */
public class CustomMultipartFile implements MultipartFile {
    private byte[] input;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return input == null || input.length == 0;
    }

    @Override
    public long getSize() {
        return input.length;
    }

    @Override
    public byte[] getBytes() {
        return input;
    }

    @Override
    public InputStream getInputStream()  {
        return new ByteArrayInputStream(input);
    }

    @Override
    public void transferTo(File destination) throws IOException, IllegalStateException {
        try(FileOutputStream fos = new FileOutputStream(destination)) {
            fos.write(input);
        }
    }
}
