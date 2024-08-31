package com.partimestudy.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeApplicationDTO {
    private Long challengeId;
    private String challengeName;
    private Integer deposit;
    private List<ChallengeScheduleDTO> challengeSchedules;
    private Integer paymentAmount;
}
