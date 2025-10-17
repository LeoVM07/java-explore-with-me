package ru.practicum.explorewithme.service.compilation;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.CreateCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationDto;

public interface AdminCompilationService {

    CompilationDto addCompilation(CreateCompilationDto newCompilation);

    void deleteCompilation(Long compilationId);

    CompilationDto updateCompilation(Long compilationId, UpdateCompilationDto updateCompilation);
}
