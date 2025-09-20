package ru.practicum.explorewithme.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ServiceHitDto;
import ru.practicum.dto.ServiceHitForListDto;
import ru.practicum.explorewithme.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class StatsController {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public ResponseEntity<ServiceHitDto> saveHit(@Valid @RequestBody ServiceHitDto hitDto) {
        return new ResponseEntity<>(statsService.saveHit(hitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ServiceHitForListDto>> getHitStats(
            @DateTimeFormat(pattern = DATE_TIME_PATTERN)
            @RequestParam LocalDateTime start,
            @DateTimeFormat(pattern = DATE_TIME_PATTERN)
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        return new ResponseEntity<>(statsService.getHitStats(start, end, uris, unique), HttpStatus.OK);
    }
}
