package cat.nexia.spring.service;

import cat.nexia.spring.models.FileDB;
import cat.nexia.spring.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    private String uuidImage;


    public FileDB store(MultipartFile file) throws IOException {
        if(file != null && file.getOriginalFilename()!=null){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            UUID uuid = UUID.randomUUID();
            String uuidAsString = uuid.toString();
            FileDB FileDB = new FileDB(uuidAsString, fileName, file.getContentType(), file.getBytes());
            setUuidImage(uuidAsString);
            return fileDBRepository.save(FileDB);
        } else {
            return null;
        }
    }
    public FileDB getFile(String id) {
        if(fileDBRepository.findById(id).isPresent()){
            return fileDBRepository.findById(id).get();
        } else {
            return null;
        }

    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    public String getUuidImage() {
        return uuidImage;
    }

    public void setUuidImage(String uuidImage) {
        this.uuidImage = uuidImage;
    }
}
