package com.slavchev.scheduledcalculations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduledcalculationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScheduledcalculationsApplication.class, args);
	}

}
