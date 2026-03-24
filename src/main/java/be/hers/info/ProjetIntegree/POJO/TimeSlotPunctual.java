package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotPunctual extends TimeSlot {
    private LocalDate date;

    public TimeSlotPunctual(LocalTime start_time, LocalTime duration, LocalDate date) {
        super(start_time, duration);
        this.date = date;
    }

    public LocalDate get_date() { return date;}

    public LocalTime get_start_time() { return start_time;}
    public LocalTime get_duration() { return duration;}
    public void set_start_time(LocalTime start_time) { this.start_time = start_time;}
    public void set_duration(LocalTime duration) { this.duration = duration;}


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