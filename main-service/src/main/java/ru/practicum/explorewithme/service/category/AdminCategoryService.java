package ru.practicum.explorewithme.service.category;

import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.CategoryShortDto;

public interface AdminCategoryService {
    CategoryDto addCategory(CategoryShortDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

}
