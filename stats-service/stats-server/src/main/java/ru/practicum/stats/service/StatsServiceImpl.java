package ru.practicum.stats.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ServiceHitDto;
import ru.practicum.dto.ServiceHitForListDto;
import ru.practicum.stats.dal.StatsRepository;
import ru.practicum.stats.exception.InvalidDateException;
import ru.practicum.stats.mapper.ServiceHitMapper;
import ru.practicum.stats.model.ServiceHit;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Transactional
    @Override
    public ServiceHitDto saveHit(ServiceHitDto hitDto) {
        ServiceHit hit = ServiceHitMapper.toHit(hitDto);
        ServiceHit savedHit = statsRepository.save(hit);
        return ServiceHitMapper.toDtoFromHit(savedHit);
    }

    @Transactional
    @Override
    public List<ServiceHitForListDto> getHitStats(LocalDateTime start, LocalDateTime end,
                                                  List<String> uris, Boolean unique) {
        List<ServiceHitForListDto> hitList;
        if (start.isAfter(end)) {
            throw new InvalidDateException("Дата окончания события не может быть раньше начала");
        }

        if (unique) {
            hitList = statsRepository.findAllUniqueIpAndTimestampBetweenAndUriIn(start, end, uris);
        } else {
            hitList = statsRepository.findAllByTimestampBetweenAndUriIn(start, end, uris);
        }
        return hitList;
    }
}
