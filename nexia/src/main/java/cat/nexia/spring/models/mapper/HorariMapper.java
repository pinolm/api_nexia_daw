package cat.nexia.spring.models.mapper;

import cat.nexia.spring.dto.response.HorariDto;
import cat.nexia.spring.models.Horari;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HorariMapper {

    private static final Log LOG = LogFactory.getLog(HorariMapper.class);

    public static HorariDto toHorariDto(Horari reserva) {

        if (reserva == null){
            return null;
        }
        LOG.info("Mapejant la clase Horari a HorariDto");
        HorariDto horariDto = new HorariDto();
        horariDto.setIniHora(reserva.getIniHora());
        horariDto.setFiHora(reserva.getFiHora());
        return horariDto;
    }
}
