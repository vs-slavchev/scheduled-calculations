package com.slavchev.scheduledcalculations;

import com.slavchev.scheduledcalculations.model.CalculationResult;
import com.slavchev.scheduledcalculations.repository.CalculationResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Testcontainers
@DataJpaTest
public class CalculationResultRepositoryIntegrationTest {

    @Autowired
    private CalculationResultRepository calculationResultRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    @AfterEach
    public void cleanUp() {
        calculationResultRepository.deleteAll();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void whenSavingCalculationResult_thenFindOne() {
        CalculationResult calculationResult = new CalculationResult();
        int result = 42;
        calculationResult.setResult(result);
        calculationResult.setStartedAt(LocalDateTime.now().minusMinutes(1));
        calculationResult.setFinishedAt(LocalDateTime.now());

        calculationResultRepository.save(calculationResult);

        List<CalculationResult> actual = calculationResultRepository.findAll();
        assertEquals(1, actual.size());
        assertEquals(result, actual.get(0).getResult());
    }
}
