package com.slavchev.scheduledcalculations.repository;

import com.slavchev.scheduledcalculations.model.CalculationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationResultRepository extends JpaRepository<CalculationResult, Long> {

}
