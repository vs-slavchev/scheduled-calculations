package com.slavchev.scheduledcalculations;

import org.springframework.boot.SpringApplication;

public class TestScheduledcalculationsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ScheduledcalculationsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
