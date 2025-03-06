package com.slavchev.scheduledcalculations.service;

import com.slavchev.scheduledcalculations.model.CalculationResult;
import com.slavchev.scheduledcalculations.repository.CalculationResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationResultService {

    private final CalculationResultRepository calculationResultRepository;

    public CalculationResultService(CalculationResultRepository calculationResultRepository) {
        this.calculationResultRepository = calculationResultRepository;
    }

    public List<CalculationResult> getCalculations() {
        return calculationResultRepository.findAll();
    }
}
