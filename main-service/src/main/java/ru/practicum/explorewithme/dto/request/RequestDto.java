package ru.practicum.explorewithme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.utility.enums.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utility.Constant.DATE_TIME_PATTERN;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;
    private Long event; //id
    private Long requester; //id
    private RequestStatus status = RequestStatus.PENDING;
}
