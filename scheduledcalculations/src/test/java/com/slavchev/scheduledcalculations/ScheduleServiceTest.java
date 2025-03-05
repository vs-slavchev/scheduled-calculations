package com.slavchev.scheduledcalculations;

import com.slavchev.scheduledcalculations.repository.ScheduleRepository;
import com.slavchev.scheduledcalculations.service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @InjectMocks
    private ScheduleService scheduleService;
    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    public void whenPollingSchedule_thenCallRepository() {
        when(scheduleRepository.findByIsEnabledTrue()).thenReturn(List.of());

        scheduleService.pollSchedule();

        verify(scheduleRepository).findByIsEnabledTrue();
    }
}
