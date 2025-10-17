package ru.practicum.explorewithme.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.CategoryRepository;
import ru.practicum.explorewithme.dal.EventRepository;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.CategoryShortDto;
import ru.practicum.explorewithme.exception.CategoryIdException;
import ru.practicum.explorewithme.exception.InvalidRequestDataException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminAdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Autowired
    public AdminAdminCategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
                                         EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryShortDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new InvalidRequestDataException("Название категории уже занято");
        }
        Category category = categoryMapper.toCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        log.info("Добавление новой категории: {}", savedCategory);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        List<Event> events = eventRepository.findAllByCategoryId(categoryId);
        if (events.isEmpty()) {
            log.info("Удаление категории с id: {}", categoryId);
            categoryRepository.deleteById(categoryId);
        } else {
            throw new InvalidRequestDataException("Категорию невозможно удалить, пока в ней содержатся события");
        }
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {

        Category categoryToUpdate = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryIdException(categoryId));

        if (!categoryDto.getName().equals(categoryToUpdate.getName())) {
            if (categoryRepository.existsByName(categoryDto.getName())) {
                throw new InvalidRequestDataException("Название категории уже занято");
            }
        }

        categoryMapper.updateCategoryFromDto(categoryDto, categoryToUpdate);
        Category savedCategory = categoryRepository.save(categoryToUpdate);

        log.info("Обновление категории по id {}", categoryId);
        return categoryMapper.toDto(savedCategory);
    }

}
