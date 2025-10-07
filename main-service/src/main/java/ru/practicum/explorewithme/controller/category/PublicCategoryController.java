package ru.practicum.explorewithme.controller.category;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.category.PublicCategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class PublicCategoryController {
    private final PublicCategoryService categoryService;

    @Autowired
    public PublicCategoryController(PublicCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(categoryService.getCategories(from, size), HttpStatus.OK);
    }


    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PositiveOrZero @PathVariable("catId") Long catId) {
        return new ResponseEntity<>(categoryService.getCategoryById(catId), HttpStatus.OK);
    }
}
