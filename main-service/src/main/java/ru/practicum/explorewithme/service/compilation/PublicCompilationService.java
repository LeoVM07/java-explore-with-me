package ru.practicum.explorewithme.service.compilation;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compilationId);
}
