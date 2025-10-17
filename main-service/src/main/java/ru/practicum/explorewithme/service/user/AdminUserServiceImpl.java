package ru.practicum.explorewithme.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dal.UserRepository;
import ru.practicum.explorewithme.dto.user.CreateUserDto;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exception.InvalidRequestDataException;
import ru.practicum.explorewithme.exception.UserIdException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.model.User;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto addUser(CreateUserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new InvalidRequestDataException("Адрес электронной почты уже занят");
        }
        if (userRepository.existsByName(userDto.getName())) {
            throw new InvalidRequestDataException("Пользователь с таким именем уже существует");
        }
        User user = userMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        log.info("Добавление пользователя: {}", savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getUsers(List<Long> userIds, int from, int size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());
        Page<User> userPage;

        if (userIds == null || userIds.isEmpty()) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.findByIdIn(userIds, pageable);
        }

        log.info("Выведен список пользователей по id: {}, с позиции: {}, размер списка: {}", userIds, from, size);
        return userPage
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserIdException(userId);
        }
        log.info("Удаление пользователя по id: {}", userId);
        userRepository.deleteById(userId);
    }


}
