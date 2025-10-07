package ru.practicum.explorewithme.controller.compilation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.CreateCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationDto;
import ru.practicum.explorewithme.service.compilation.AdminCompilationService;

@RestController
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {

    private final AdminCompilationService compilationService;

    @Autowired
    public AdminCompilationController(AdminCompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody @Valid CreateCompilationDto newCompilation) {
        return new ResponseEntity<>(compilationService.addCompilation(newCompilation), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@Positive @PathVariable("compilationId") Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @Positive @PathVariable("compilationId") Long compilationId,
            @RequestBody @Valid UpdateCompilationDto updateCompilation) {
        return new ResponseEntity<>(compilationService.updateCompilation(compilationId, updateCompilation), HttpStatus.OK);
    }
}
