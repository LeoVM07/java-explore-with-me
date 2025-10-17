package ru.practicum.explorewithme.service.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CompilationRepository;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.CreateCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationDto;
import ru.practicum.explorewithme.exception.CompilationIdException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Autowired
    public AdminCompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository,
                                       CompilationMapper compilationMapper) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.compilationMapper = compilationMapper;
    }

    @Override
    public CompilationDto addCompilation(CreateCompilationDto newCompilation) {

        List<Event> events = newCompilation.getEventIds() != null ?
                eventRepository.findByIdIn(newCompilation.getEventIds())
                : List.of();
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilation.getPinned());
        compilation.setTitle(newCompilation.getTitle());

        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Добавление новой подборки событий: {}", savedCompilation);
        return compilationMapper.toDto(savedCompilation);
    }

    public void deleteCompilation(Long compilationId) {
        if (!compilationRepository.existsById(compilationId)) {
            throw new CompilationIdException(compilationId);
        }
        log.info("Удаление подборки по id: {}", compilationId);
        compilationRepository.deleteById(compilationId);
    }

    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationDto updateCompilation) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationIdException(compilationId));

        if (updateCompilation.getEventIds() != null) {
            List<Event> events = eventRepository.findByIdIn(updateCompilation.getEventIds());
            compilation.setEvents(events);
        }

        if (updateCompilation.getPinned() != null) {
            compilation.setPinned(updateCompilation.getPinned());
        }

        if (updateCompilation.getTitle() != null) {
            compilation.setTitle(updateCompilation.getTitle());
        }

        compilationRepository.save(compilation);
        log.info("Обновление категории: {}", compilation);
        return compilationMapper.toDto(compilation);
    }
}
