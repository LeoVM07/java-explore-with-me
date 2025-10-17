package ru.practicum.explorewithme.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CategoryRepository;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.exception.CategoryIdException;
import ru.practicum.explorewithme.mapper.CategoryMapper;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public PublicCategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        log.info("Выведен список категорий с позиции: {}, размер списка: {}", from, size);
        return categoryRepository
                .findAll(pageable)
                .map(categoryMapper::toDto)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long categoryId) {
        log.info("Поиск категории по id {}", categoryId);
        return categoryRepository.findById(categoryId)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new CategoryIdException(categoryId));
    }
}
