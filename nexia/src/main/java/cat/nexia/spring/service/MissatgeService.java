package cat.nexia.spring.service;

import java.util.List;
import cat.nexia.spring.models.Missatge;

public interface MissatgeService {

    List<Missatge> getAllMissatges();

    Missatge getMissatgeById(Long id);

    Missatge createMissatge(Missatge missatge);

    void deleteMissatge(Long id);
}
