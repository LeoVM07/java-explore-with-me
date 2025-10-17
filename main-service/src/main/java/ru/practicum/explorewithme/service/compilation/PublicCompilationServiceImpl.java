package ru.practicum.explorewithme.service.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CompilationRepository;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.exception.CompilationIdException;
import ru.practicum.explorewithme.mapper.CompilationMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Autowired
    public PublicCompilationServiceImpl(CompilationRepository compilationRepository, CompilationMapper compilationMapper) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        log.info("Выведение списка подборок по условиям: pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilationRepository.findCompilations(pinned, pageable)
                .stream()
                .map(compilationMapper::toDto)
                .toList();
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        log.info("Поиск подборки по id: {}", compilationId);
        return compilationMapper.toDto(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationIdException(compilationId)));
    }
}
