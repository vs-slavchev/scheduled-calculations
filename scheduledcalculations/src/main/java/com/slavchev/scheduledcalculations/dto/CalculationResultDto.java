package com.slavchev.scheduledcalculations.dto;

import java.time.LocalDateTime;

public record CalculationResultDto(int result,
                                   LocalDateTime startedAt,
                                   LocalDateTime finishedAt,
                                   String scheduleType,
                                   String scheduleString) {
}
