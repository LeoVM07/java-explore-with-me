package ru.practicum.explorewithme.service.user;

import ru.practicum.explorewithme.dto.user.CreateUserDto;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {

    UserDto addUser(CreateUserDto userDto);

    List<UserDto> getUsers(List<Long> userIds, int from, int size);

    void deleteUser(Long userId);
}
