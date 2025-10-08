package ru.practicum.explorewithme.dto.compilation;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompilationDto {

    @JsonProperty("events")
    private List<Long> eventIds;

    @NotNull
    private Boolean pinned = false;

    @NotBlank(message = "Необходимо указать название подпорки")
    @Size(min = 1, max = 50, message = "Длина названия подборки не может превышать 50 символов")
    private String title;
}
