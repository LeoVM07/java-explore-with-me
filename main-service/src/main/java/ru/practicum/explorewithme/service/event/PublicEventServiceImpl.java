package ru.practicum.explorewithme.service.event;

import client.StatsClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ServiceHitDto;
import ru.practicum.dto.ServiceHitForListDto;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dto.event.EventDto;
import ru.practicum.explorewithme.dto.event.EventInfoDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.EventIdException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.utility.enums.EventState;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventInfoDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                        HttpServletRequest request) {

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Дата начала события не может быть позже даты окончания");
        }

        List<Event> events = eventRepository.findAllByFiltersPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, PageRequest.of(from, size));

        try {
            ServiceHitDto hitDto = new ServiceHitDto(
                    "evm-main-service",
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    LocalDateTime.now());
            statsClient.saveHit(hitDto);
            log.debug("Сохранение статистики запросов: {}", hitDto);
        } catch (Exception e) {
            log.error("Возникла ошибка при сохранении статистики запросов :{}", e.getMessage());
        }

        Map<Long, Long> views = getAmountOfViews(events);
        return events.stream()
                .map(event -> {
                    EventInfoDto infoDto = eventMapper.toEventInfoDto(event);
                    infoDto.setViews(views.getOrDefault(event.getId(), 0L));
                    return infoDto;
                })
                .toList();
    }

    public EventDto getEvent(Long eventId, HttpServletRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventIdException(eventId));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException(String.format("Событие с id= %d недоступно, так как не опубликовано", eventId));
        }

        try {
            statsClient.saveHit(new ServiceHitDto(
                    "evm-main-service",
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    LocalDateTime.now()));
        } catch (Exception e) {
            log.error("Не удалось отправить запрос о сохранении на сервер статистики");
        }

        EventDto result = eventMapper.toEventDto(event);
        Map<Long, Long> views = getAmountOfViews(List.of(event));
        result.setViews(views.getOrDefault(event.getId(), 0L));

        return result;
    }

    private Map<Long, Long> getAmountOfViews(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return Collections.emptyMap();
        }
        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .distinct()
                .toList();

        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(5);

        Map<Long, Long> viewsMap = new HashMap<>();
        try {
            log.debug("Получение статистики по времени для URI: {} с {} по {}", uris, startTime, endTime);
            List<ServiceHitForListDto> stats = statsClient.getHitStat(
                    startTime,
                    endTime,
                    uris,
                    true
            );
            if (stats != null && !stats.isEmpty()) {
                for (ServiceHitForListDto stat : stats) {
                    Long eventId = Long.parseLong(stat.getUri().substring("/events/".length()));
                    viewsMap.put(eventId, stat.getHits());
                }
            }
        } catch (Exception e) {
            log.error("Не удалось получить статистику");
        }
        return viewsMap;
    }
}
