package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalTime;

public abstract class TimeSlot {
    protected LocalTime start_time;
    protected LocalTime duration;

    public TimeSlot(LocalTime start_time, LocalTime duration) {
        this.start_time = start_time;
        this.duration = duration;
    }

    public LocalTime get_start_time() { return start_time;}

    public LocalTime get_duration() { return duration;}

    public void set_start_time(LocalTime start_time) { this.start_time = start_time;}

    public void set_duration(LocalTime duration) { this.duration = duration;}

    public abstract boolean overlaps(TimeSlot time_slot);
    public abstract boolean overlaps(TimeSlotPunctual time_slot_punctual);
    public abstract boolean overlaps(TimeSlotBase time_slot_base);
}
