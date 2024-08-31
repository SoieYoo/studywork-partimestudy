package com.partimestudy.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeScheduleDTO {
    private LocalDate applyDate;
    private Integer hours;
}
