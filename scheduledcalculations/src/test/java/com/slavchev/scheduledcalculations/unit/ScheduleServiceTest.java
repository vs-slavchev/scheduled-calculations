package com.slavchev.scheduledcalculations.unit;

import com.slavchev.scheduledcalculations.repository.ScheduleRepository;
import com.slavchev.scheduledcalculations.service.ScheduleService;
import com.slavchev.scheduledcalculations.service.ScheduledTasksManager;
import com.slavchev.scheduledcalculations.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @InjectMocks
    private ScheduleService scheduleService;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private ScheduledTasksManager scheduledTasks;

    @Test
    public void whenSyncingSchedule_thenCallRepositoryAndService() {
        when(scheduleRepository.findByIsEnabledTrue()).thenReturn(List.of());
        when(scheduledTasks.getAddedSchedules(List.of())).thenReturn(Set.of());
        when(scheduledTasks.getRemovedSchedules(List.of())).thenReturn(Set.of());

        scheduleService.syncSchedule();

        verify(scheduleRepository).findByIsEnabledTrue();
        verify(scheduledTasks).getAddedSchedules(List.of());
        verify(scheduledTasks).getRemovedSchedules(List.of());
    }
}
