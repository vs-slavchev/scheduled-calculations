package com.slavchev.scheduledcalculations.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ControllersIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void whenGettingEmptyCalculationResults_thenReturnEmptyList() {
        given()
                .when()
                .get("/api/v1/calculations")
            .then()
                .statusCode(200)
                .contentType("application/json")
                .body(
                        ".", hasSize(0));
    }

    @Test
    void whenAddingValidCronSchedule_thenReturnCreated() {
        given()
                .body("{\"scheduleString\": \"* * * * * *\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/schedule")
            .then()
                .statusCode(201);
    }

    @Test
    void whenAddingValidTimestampsSchedule_thenReturnCreated() {
        given()
                .body("{\"scheduleString\": \"2025-02-26 13:02:00, 2025-02-26 13:02:30\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/schedule")
                .then()
                .statusCode(201);
    }

    @Test
    void whenAddingInvalidSchedule_thenReturnBadRequest() {
        given()
                .body("{\"scheduleString\": \"invalid-schedule\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/schedule")
                .then()
                .statusCode(400)
                .body("error", CoreMatchers.containsString("Invalid schedule format"));
    }

}
