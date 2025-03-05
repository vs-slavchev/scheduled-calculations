package com.slavchev.scheduledcalculations.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_type")
    private String scheduleType;

    @Column(name = "cron_string")
    private String cronString;

    @Type(ListArrayType.class)
    @Column(name = "execution_times", columnDefinition = "timestamp[]")
    private List<LocalDateTime> executionTimes;

    @Column(name = "enabled")
    private boolean isEnabled = true;

    public Schedule() {
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getCronString() {
        return cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
    }

    public List<LocalDateTime> getExecutionTimes() {
        return executionTimes;
    }

    public void setExecutionTimes(List<LocalDateTime> executionTimes) {
        this.executionTimes = executionTimes;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return isEnabled == schedule.isEnabled && Objects.equals(id, schedule.id) && Objects.equals(scheduleType, schedule.scheduleType) && Objects.equals(cronString, schedule.cronString) && Objects.deepEquals(executionTimes, schedule.executionTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scheduleType, cronString, executionTimes.hashCode(),
                isEnabled);
    }
}
