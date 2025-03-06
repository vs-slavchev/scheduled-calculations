package com.slavchev.scheduledcalculations.service;

import com.slavchev.scheduledcalculations.model.CalculationResult;
import com.slavchev.scheduledcalculations.model.Schedule;
import com.slavchev.scheduledcalculations.repository.CalculationResultRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final Logger logger = Logger.getLogger(TaskService.class.getName());
    private final ScheduledTasksManager scheduledTasks;
    private final TaskScheduler scheduler;
    private final CalculationResultRepository calculationResultRepository;

    public TaskService(ScheduledTasksManager scheduledTasks,
                       TaskScheduler taskScheduler,
                       CalculationResultRepository calculationResultRepository) {
        this.scheduledTasks = scheduledTasks;
        this.scheduler = taskScheduler;
        this.calculationResultRepository = calculationResultRepository;
    }

    public void scheduleTask(Schedule schedule) {
        List<ScheduledFuture<?>> scheduledFutures = List.of();
        if (schedule.getScheduleType().equals("CRON")) {
            scheduledFutures = List.of(scheduleCron(schedule));
        } else if (schedule.getScheduleType().equals("TIMESTAMP")) {
            scheduledFutures = schedule.getExecutionTimes().stream()
                    .filter(executionTime -> executionTime.isAfter(LocalDateTime.now(ZoneOffset.UTC)))
                    .map(executionTime -> scheduleAt(schedule, executionTime))
                    .collect(Collectors.toList());
        }
        scheduledTasks.put(schedule, scheduledFutures);
    }

    public void cancelTask(Schedule schedule) {
        List<ScheduledFuture<?>> tasksToCancel = scheduledTasks.get(schedule);
        tasksToCancel.forEach(scheduledFuture -> scheduledFuture.cancel(false));
        scheduledTasks.remove(schedule);
    }

    private ScheduledFuture<?> scheduleCron(Schedule schedule) {
        logger.info(String.format("Scheduling cron task for schedule %d", schedule.getId()));
        return scheduler.schedule(createTask(schedule, addition()), new CronTrigger(schedule.getCronString()));
    }

    private ScheduledFuture<?> scheduleAt(Schedule schedule, LocalDateTime executionTime) {
        logger.info(String.format("Scheduling timestamp task for schedule %d at %s",
                schedule.getId(), executionTime));
        return scheduler.schedule(createTask(schedule, multiplication()),
                executionTime.atZone(ZoneId.of("UTC")).toInstant());
    }

    private Runnable createTask(Schedule schedule, BiFunction<Integer, Integer, Integer> calculation) {
        return () -> {
            CalculationResult calculationResult = new CalculationResult();
            calculationResult.setScheduleId(schedule.getId());
            calculationResult.setStartedAt(LocalDateTime.now());
            calculationResultRepository.save(calculationResult);

            int result = calculation.apply(10, 10);

            calculationResult.setResult(result);
            calculationResult.setFinishedAt(LocalDateTime.now());
            calculationResultRepository.save(calculationResult);
            logger.info(String.format("Calculation for schedule %d finished with result: %d",
                    schedule.getId(), result));
        };
    }

    private BiFunction<Integer, Integer, Integer> addition() {
        return (a, b) -> a + b;
    }

    private BiFunction<Integer, Integer, Integer> multiplication() {
        return (a, b) -> a * b;
    }
}