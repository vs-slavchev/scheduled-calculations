package com.slavchev.scheduledcalculations.integration;

import com.slavchev.scheduledcalculations.model.Schedule;
import com.slavchev.scheduledcalculations.repository.ScheduleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@Testcontainers
@DataJpaTest
public class ScheduleRepositoryIntegrationTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    @AfterEach
    public void cleanUp() {
        scheduleRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void whenSavingCronSchedule_thenFindOneCronSchedule() {
        Schedule schedule = new Schedule();
        String cronString = "0 0 12 * * ?";
        schedule.setCronString(cronString);

        scheduleRepository.save(schedule);

        List<Schedule> actual = scheduleRepository.findByIsEnabledTrue();
        assertEquals(1, actual.size());
        assertEquals(cronString, actual.get(0).getCronString());
        assertEquals("CRON", actual.get(0).getScheduleType());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void whenSavingTimestampsSchedule_thenFindOneTimestampSchedule() {
        Schedule schedule = new Schedule();
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        schedule.setExecutionTimes(List.of(dateTime));

        scheduleRepository.save(schedule);

        List<Schedule> actual = scheduleRepository.findByIsEnabledTrue();
        assertEquals(1, actual.size());
        assertEquals(dateTime, actual.get(0).getExecutionTimes().get(0));
        assertEquals("TIMESTAMP", actual.get(0).getScheduleType());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void whenSavingScheduleWithBothTypes_thenError() {
        Schedule schedule = new Schedule();
        LocalDateTime dateTime = LocalDateTime.now();
        schedule.setExecutionTimes(List.of(dateTime));
        schedule.setCronString("0 0 12 * * ?");

        assertThrows(DataIntegrityViolationException.class, () -> scheduleRepository.save(schedule));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void whenSavingScheduleWithNoType_thenError() {
        Schedule schedule = new Schedule();

        assertThrows(DataIntegrityViolationException.class, () -> scheduleRepository.save(schedule));
    }
}
