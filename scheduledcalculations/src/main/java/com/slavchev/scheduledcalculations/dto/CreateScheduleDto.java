package com.slavchev.scheduledcalculations.dto;

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.slavchev.scheduledcalculations.model.Schedule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cronutils.model.CronType.SPRING53;

public record CreateScheduleDto(String scheduleString) {

    public static Schedule toDo(CreateScheduleDto createScheduleDto) {
        String cronString = createScheduleDto.scheduleString;

        Schedule schedule = new Schedule();
        if (isCron(cronString)) {
            schedule.setCronString(cronString);
            return schedule;
        }

        List<LocalDateTime> executionTimes = toLocalDateTimes(cronString);
        schedule.setExecutionTimes(executionTimes);
        return schedule;
    }

    private static List<LocalDateTime> toLocalDateTimes(String cronString) {
        try {
        return Stream.of(cronString.split(","))
                .map(String::trim)
                .map(timeString -> LocalDateTime.parse(timeString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid schedule format. " +
                    "Timestamp format: 'yyyy-MM-ddTHH:mm:ss', cron format: '* * * * * *'");
        }
    }

    private static boolean isCron(String scheduleString) {
        CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(SPRING53));
        try {
            Cron cron = parser.parse(scheduleString);
            cron.validate();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
