package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utility.Constant.DATE_TIME_PATTERN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDto {

    @NotBlank(message = "Необходимо указать краткое описание события")
    @Size(min = 20, max = 2000, message = "Длина аннотации должна быть не менее 20 и не более 2000 символов")
    private String annotation;

    @NotNull(message = "Необходимо указать id категории")
    @Positive
    @JsonProperty("category")
    private Long categoryId;

    @NotBlank(message = "Необходимо оставить описание события")
    @Size(min = 20, max = 7000, message = "Длина описания должна быть не менее 20 и не более 7000 символов")
    private String description;

    @NotNull(message = "Необходимо указать дату события")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @NotNull(message = "Необходимо указать координаты события")
    private LocationDto location;

    @NotNull
    private Boolean paid = false;

    @PositiveOrZero(message = "Количество участников не может быть отрицательным числом")
    private Long participantLimit = 0L;

    @NotNull
    private Boolean requestModeration = true;

    @NotBlank(message = "Необходимо указать название события")
    @Size(min = 3, max = 120, message = "Длина названия должна быть не менее 3 и не более 120 символов")
    private String title;
}
