package com.slavchev.scheduledcalculations.integration;

import com.slavchev.scheduledcalculations.model.Schedule;
import com.slavchev.scheduledcalculations.repository.CalculationResultRepository;
import com.slavchev.scheduledcalculations.repository.ScheduleRepository;
import com.slavchev.scheduledcalculations.service.ScheduleService;
import com.slavchev.scheduledcalculations.service.ScheduledTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class ScheduleServiceIntegrationTest {
    private static final String CRON_STRING = "59 59 23 31 12 7";

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private CalculationResultRepository calculationResultRepository;
    @MockitoSpyBean
    private ScheduledTasksManager scheduledTasksManager;
    @MockitoSpyBean
    private TaskScheduler taskScheduler;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    @AfterEach
    public void cleanUp() {
        calculationResultRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    public void whenNoSchedulesToSync_thenNoTasksAreScheduled() {
        scheduleService.syncSchedule();

        verify(scheduledTasksManager, times(0)).put(any(), any());
        verify(taskScheduler, times(0)).schedule(any(), any(Trigger.class));
        verify(taskScheduler, times(0)).schedule(any(), any(Instant.class));
    }

    @Test
    public void whenOneCronScheduleToSync_thenOneCronTaskScheduled() {
        Schedule schedule = new Schedule();
        schedule.setCronString(CRON_STRING);
        schedule.setScheduleType("CRON");
        scheduleRepository.save(schedule);

        scheduleService.syncSchedule();

        verify(scheduledTasksManager, times(1)).put(eq(schedule), any());
        verify(taskScheduler, times(1)).schedule(any(), any(Trigger.class));
    }

    @Test
    public void whenOneTimestampScheduleToSync_thenOneTimestampTaskScheduled() {
        Schedule schedule = new Schedule();
        schedule.setExecutionTimes(List.of(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        schedule.setScheduleType("TIMESTAMP");
        scheduleRepository.save(schedule);

        scheduleService.syncSchedule();

        verify(scheduledTasksManager, times(1)).put(eq(schedule), any());
        verify(taskScheduler, times(1)).schedule(any(), any(Instant.class));
    }

    @Test
    public void whenOneScheduleToRemove_thenOneTaskCancelled() {
        Schedule schedule = new Schedule();
        schedule.setCronString(CRON_STRING);
        schedule.setScheduleType("CRON");
        scheduleRepository.save(schedule);
        scheduleService.syncSchedule();
        scheduleRepository.delete(schedule);

        scheduleService.syncSchedule();

        verify(scheduledTasksManager, times(1)).remove(eq(schedule));
    }

    @Test
    public void whenOneScheduleToExecute_thenCalculationResultInDb() {
        Schedule schedule = new Schedule();
        schedule.setCronString("* * * * * *");
        schedule.setScheduleType("CRON");
        scheduleRepository.save(schedule);
        scheduleService.syncSchedule();
        assertTrue(calculationResultRepository.findAll().isEmpty());

        await().atMost(3, SECONDS)
                .pollInterval(1, SECONDS)
                .until(() -> !calculationResultRepository.findAll().isEmpty());

        assertFalse(calculationResultRepository.findAll().isEmpty());
        verify(scheduledTasksManager, times(1)).put(eq(schedule), any());
        verify(taskScheduler, times(1)).schedule(any(), any(Trigger.class));
    }

}
