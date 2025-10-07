package ru.practicum.explorewithme.dto.category;

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
public class CategoryShortDto {

    @NotBlank(message = "Необходимо указать название категории")
    @Size(min = 1, max = 50, message = "Название категории не должно быть короче 1 и длиннее 50 символов")
    private String name;
}
