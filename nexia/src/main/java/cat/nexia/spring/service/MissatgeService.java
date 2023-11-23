package cat.nexia.spring.service;

import cat.nexia.spring.models.Missatge;

import java.util.List;

public interface MissatgeService {

    List<Missatge> getAllMissatges();

    Missatge getMissatgeById(Long id);

    Missatge createMissatge(Missatge missatge);

    void deleteMissatge(Long id);
}
