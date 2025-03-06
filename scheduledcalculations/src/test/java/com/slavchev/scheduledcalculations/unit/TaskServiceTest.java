package com.slavchev.scheduledcalculations.unit;

import com.slavchev.scheduledcalculations.model.Schedule;
import com.slavchev.scheduledcalculations.repository.CalculationResultRepository;
import com.slavchev.scheduledcalculations.service.ScheduledTasksManager;
import com.slavchev.scheduledcalculations.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskScheduler scheduler;
    @Mock
    private CalculationResultRepository calculationResultRepository;
    @Mock
    private ScheduledTasksManager scheduledTasks;

    @Test
    public void whenSchedulingCronTask_thenAddToTasksManager() {
        Schedule schedule = new Schedule();
        schedule.setScheduleType("CRON");
        schedule.setCronString("*/10 * * * * *");
        when(scheduler.schedule(any(Runnable.class), any(CronTrigger.class)))
                .thenReturn(mock(ScheduledFuture.class));

        taskService.scheduleTask(schedule);

        verify(scheduler).schedule(any(Runnable.class), any(CronTrigger.class));
        verify(scheduledTasks).put(eq(schedule), any(List.class));
    }

    @Test
    public void whenSchedulingFutureTimestampTask_thenAddToTasksManager() {
        Schedule schedule = new Schedule();
        schedule.setScheduleType("TIMESTAMP");
        schedule.setExecutionTimes(List.of(LocalDateTime.now().plusMinutes(2)));
        when(scheduler.schedule(any(Runnable.class), any(Instant.class)))
                .thenReturn(mock(ScheduledFuture.class));

        taskService.scheduleTask(schedule);

        verify(scheduler).schedule(any(Runnable.class), any(Instant.class));
        verify(scheduledTasks).put(eq(schedule), any(List.class));
    }

    @Test
    public void whenSchedulingPastTimestampTask_thenDoNotAddToTasksManager() {
        Schedule schedule = new Schedule();
        schedule.setScheduleType("TIMESTAMP");
        schedule.setExecutionTimes(List.of(LocalDateTime.parse("2020-01-01T00:00:00")));

        taskService.scheduleTask(schedule);

        verifyNoInteractions(scheduler);
        verify(scheduledTasks).put(eq(schedule), eq(List.of()));
    }

    @Test
    public void whenCancellingTask_thenCallScheduledTasksManager() {
        Schedule schedule = new Schedule();

        taskService.cancelTask(schedule);

        verify(scheduledTasks).get(schedule);
        verify(scheduledTasks).remove(schedule);
    }
}
