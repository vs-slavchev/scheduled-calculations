package com.slavchev.scheduledcalculations.controller;

import com.slavchev.scheduledcalculations.dto.CalculationResultDto;
import com.slavchev.scheduledcalculations.model.CalculationResult;
import com.slavchev.scheduledcalculations.presenter.CalculationResultPresenter;
import com.slavchev.scheduledcalculations.service.CalculationResultService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calculations")
@CrossOrigin
public class CalculationResultController {

    private final CalculationResultService calculationResultService;

    public CalculationResultController(CalculationResultService CalculationResultService) {
        this.calculationResultService = CalculationResultService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalculationResultDto>> getCalculations() {
        List<CalculationResult> results = calculationResultService.getCalculations();

        return ResponseEntity.ok(results.stream()
                .map(CalculationResultPresenter::toDto)
                .toList());
    }

}
