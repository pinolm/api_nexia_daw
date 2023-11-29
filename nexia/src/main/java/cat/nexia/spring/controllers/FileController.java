package cat.nexia.spring.controllers;

import cat.nexia.spring.dto.response.ResponseFile;
import cat.nexia.spring.dto.response.ResponseMessage;
import cat.nexia.spring.models.FileDB;
import cat.nexia.spring.service.FileStorageService;
import cat.nexia.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador del mòdul de fitxers. Sense ús.
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private UserService userService;

    /**
     * Desar un fitxer a la taula de base de dades
     *
     * @param file fitxer a guardar
     * @param userId id de l'usuari autentificat
     * @return resposta amb codi htpp i missatge
     */

    @PostMapping(value ="/upload", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file,
                                                      @RequestPart("userId") @Valid String userId) {

        String message = "";
        try {
            storageService.store(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    /**
     * Obtenir el llistat de fitxers desats a bbdd
     * @return Llista de ResponseFile
     */

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    /**
     * Obtenir el fitxer per id de fitxer
     * @param id identificador del fitxer
     * @return fitxer en byte[]
     */


    @GetMapping("/files/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
}
