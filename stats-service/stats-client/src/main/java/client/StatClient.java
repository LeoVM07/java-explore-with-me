package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.ServiceHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(ServiceHitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getHitStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", uris != null ? String.join(",", uris) : "",
                "unique", unique != null ? unique.toString() : "false");

        String path = "/stats?start={start}&end={end}";
        if (uris != null && !uris.isEmpty()) {
            path += "&uris={uris}";
        }
        path += "&unique={unique}";

        return get(path, params);
    }
}
