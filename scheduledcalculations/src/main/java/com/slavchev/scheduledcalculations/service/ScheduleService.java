package com.slavchev.scheduledcalculations.service;

import com.slavchev.scheduledcalculations.model.Schedule;
import com.slavchev.scheduledcalculations.repository.ScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class ScheduleService {

    private final Logger logger = Logger.getLogger(ScheduleService.class.getName());
    private final TaskService taskService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduledTasksManager scheduledTasks;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           TaskService taskService,
                           ScheduledTasksManager scheduledTasks) {
        this.scheduleRepository = scheduleRepository;
        this.taskService = taskService;
        this.scheduledTasks = scheduledTasks;
    }

    @Scheduled(fixedRateString = "${schedule.polling.milliseconds}")
    public void syncSchedule() {
        List<Schedule> polledSchedules = scheduleRepository.findByIsEnabledTrue();
        logger.info("Found " + polledSchedules.size() + " enabled schedules.");

        Set<Schedule> addedSchedules = scheduledTasks.getAddedSchedules(polledSchedules);
        startNewSchedules(addedSchedules);

        Set<Schedule> removed = scheduledTasks.getRemovedSchedules(polledSchedules);
        cancelRemovedSchedules(removed);
    }

    private void startNewSchedules(Set<Schedule> addedSchedules) {
        addedSchedules
                .forEach(taskService::scheduleTask);
        logger.info("Added " + addedSchedules.size() + " new schedules.");
    }

    private void cancelRemovedSchedules(Set<Schedule> removedSchedules) {
        removedSchedules
                .forEach(taskService::cancelTask);
        logger.info("Removed " + removedSchedules.size() + " schedules.");
    }


}
