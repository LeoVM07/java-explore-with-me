package ru.practicum.explorewithme.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.utility.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusUpdateDto {

    @NotEmpty(message = "Необходимо указать id запросов, статус которых необходимо обновить")
    private List<Long> requestIds;

    @NotNull(message = "Необходимо указать желаемый статус запросов")
    private RequestStatus status;
}
