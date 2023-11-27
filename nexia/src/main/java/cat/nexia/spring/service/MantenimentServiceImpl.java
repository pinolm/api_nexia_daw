package cat.nexia.spring.service;

import cat.nexia.spring.models.Manteniment;
import cat.nexia.spring.repository.MantenimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MantenimentServiceImpl implements MantenimentService{

    @Autowired
    private MantenimentRepository mantenimentRepository;

    @Override
    public void guardarManteniment(LocalDate dia, Long idPista) {
        mantenimentRepository.insertarManteniment(dia, idPista);

    }

    @Override
    public List<Manteniment> findMantenimentByDia(LocalDate dia) {
        return mantenimentRepository.findAllByDia(dia);
    }

    @Override
    public List<Manteniment> findAllbyDiaAndPista(LocalDate dia, Long pista) {
        return mantenimentRepository.findAllByDiaAndPista(dia, pista);
    }

    @Override
    public Integer countAllByDiaAndPista(LocalDate dia, Long pista) {
        return mantenimentRepository.countAllByDiaAndPista(dia, pista);
    }
}
