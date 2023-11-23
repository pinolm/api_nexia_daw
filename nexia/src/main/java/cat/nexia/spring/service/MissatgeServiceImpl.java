package cat.nexia.spring.service;

import cat.nexia.spring.models.Missatge;
import cat.nexia.spring.repository.MissatgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissatgeServiceImpl implements MissatgeService {

    private final MissatgeRepository missatgeRepository;

    @Autowired
    public MissatgeServiceImpl(MissatgeRepository missatgeRepository) {
        this.missatgeRepository = missatgeRepository;
    }

    @Override
    public List<Missatge> getAllMissatges() {
        return missatgeRepository.findAll();
    }

    @Override
    public Missatge getMissatgeById(Long id) {
        return missatgeRepository.findById(id).orElse(null);
    }

    @Override
    public Missatge createMissatge(Missatge missatge) {
        return missatgeRepository.save(missatge);
    }

    @Override
    public void deleteMissatge(Long id) {
        missatgeRepository.deleteById(id);
    }
}
