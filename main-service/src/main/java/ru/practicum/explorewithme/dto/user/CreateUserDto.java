package ru.practicum.explorewithme.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "Необходимо указать электронный адрес")
    @Email(message = "Некорректный формат электронного адреса")
    @Size(min = 6, max = 254, message = "Длина электронного адреса не должна быть не менее 6 и не более 254 символов")
    private String email;

    @NotBlank(message = "Необходимо указать имя пользователя")
    @Size(min = 2, max = 250, message = "Длинна имени пользователя должна быть не менее 2 и не более 250 символов")
    private String name;
}
