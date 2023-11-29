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

/**
 * Servei per a la gestió dels fitxers
 */
@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    private String uuidImage;

    /**
     * Guardar una fitxer a la base de dades
     * @param file fitxer d'entrada
     * @return retorna una instància de FileDB del fitxer guardat
     * @throws IOException en cas d'error llença una excepció del tipus I/O
     */

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

    /**
     * Obtenir un fitxer de la bbdd a partir del seu id
     * @param id id del fitxer
     * @return null si no el troba o un objecte del tipus FileDB
     */
    public FileDB getFile(String id) {
        if(fileDBRepository.findById(id).isPresent()){
            return fileDBRepository.findById(id).get();
        } else {
            return null;
        }

    }

    /**
     * Obté tots els fitxers guardats a la bbdd
     * @return llista d'objectes FileDB o llista buida en cas que no n'hi hagin.
     */

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
