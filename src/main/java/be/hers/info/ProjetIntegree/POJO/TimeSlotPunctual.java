package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotPunctual extends TimeSlot {
    private LocalDate startDate;
    private LocalDate endDate;

    public TimeSlotPunctual(LocalTime start_time, LocalTime duration, LocalDate startDate, LocalDate endDate) {
        super(start_time, duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    // TODO : A FAIRE
    @Override
    public boolean overlaps(TimeSlot time_slot) {
        return false;
    }

    @Override
    public boolean overlaps(TimeSlotPunctual time_slot_punctual) {
        return false;
    }

    @Override
    public boolean overlaps(TimeSlotBase time_slot_base) {
        return false;
    }
}