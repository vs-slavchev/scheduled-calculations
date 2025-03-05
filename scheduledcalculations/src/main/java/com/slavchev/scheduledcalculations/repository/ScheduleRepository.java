package com.slavchev.scheduledcalculations.repository;

import com.slavchev.scheduledcalculations.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByIsEnabledTrue();

}
