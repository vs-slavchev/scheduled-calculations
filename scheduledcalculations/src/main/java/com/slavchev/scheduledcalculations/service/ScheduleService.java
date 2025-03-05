package com.slavchev.scheduledcalculations.service;

import com.slavchev.scheduledcalculations.model.Schedule;
import com.slavchev.scheduledcalculations.repository.ScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ScheduleService {

    private final Logger logger = Logger.getLogger(ScheduleService.class.getName());
    private final Map<Schedule, ScheduledTask> scheduledTasks = new HashMap<>();
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Scheduled(fixedRateString = "${schedule.polling.milliseconds}")
    public void pollSchedule() {
        List<Schedule> enabledSchedules = scheduleRepository.findByIsEnabledTrue();
        logger.info("Found " + enabledSchedules.size() + " enabled schedules.");
    }
}
