package com.slavchev.scheduledcalculations.controller;

import com.slavchev.scheduledcalculations.dto.CreateScheduleDto;
import com.slavchev.scheduledcalculations.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/schedule")
@CrossOrigin
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addSchedule(@RequestBody CreateScheduleDto scheduleDto) {
        scheduleService.addSchedule(CreateScheduleDto.toDo(scheduleDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
