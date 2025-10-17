package client;

import org.springframework.http.ResponseEntity;
import ru.practicum.dto.ServiceHitDto;
import ru.practicum.dto.ServiceHitForListDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BaseClient {

    ResponseEntity<ServiceHitDto> saveHit(ServiceHitDto serviceHitDto);

    List<ServiceHitForListDto> getHitStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
