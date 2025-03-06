package com.slavchev.scheduledcalculations.service;

import com.slavchev.scheduledcalculations.model.Schedule;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
public class ScheduledTasksManager {
    private final Map<Schedule, List<ScheduledFuture<?>>> scheduledTasks = new HashMap<>();

    public void put(Schedule schedule, List<ScheduledFuture<?>> scheduledFutures) {
        scheduledTasks.put(schedule, scheduledFutures);
    }

    public void remove(Schedule schedule) {
        scheduledTasks.remove(schedule);
    }

    public List<ScheduledFuture<?>> get(Schedule schedule) {
        return scheduledTasks.get(schedule);
    }

    public Set<Schedule> getAddedSchedules(List<Schedule> polledSchedules) {
        return polledSchedules.stream()
                .filter(polledSchedule -> !scheduledTasks.containsKey(polledSchedule))
                .collect(Collectors.toSet());
    }

    public Set<Schedule> getRemovedSchedules(List<Schedule> polledSchedules) {
        return scheduledTasks.keySet().stream()
                .filter(schedule -> !polledSchedules.contains(schedule))
                .collect(Collectors.toSet());
    }
}
