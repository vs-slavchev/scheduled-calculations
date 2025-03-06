package com.slavchev.scheduledcalculations.presenter;

import com.slavchev.scheduledcalculations.dto.CalculationResultDto;
import com.slavchev.scheduledcalculations.model.CalculationResult;
import com.slavchev.scheduledcalculations.model.Schedule;

import java.time.temporal.ChronoUnit;

public class CalculationResultPresenter {

    public static CalculationResultDto toDto(CalculationResult calculationResult) {
        Schedule schedule = calculationResult.getSchedule();
        return new CalculationResultDto(
                calculationResult.getResult(),
                calculationResult.getStartedAt().truncatedTo(ChronoUnit.SECONDS),
                calculationResult.getFinishedAt().truncatedTo(ChronoUnit.SECONDS),
                schedule.getScheduleType(),
                schedule.getCronString() == null ? schedule.getExecutionTimes().toString()
                        : schedule.getCronString()
        );
    }
}
