package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalTime;

public class TimeSlotBase extends TimeSlot {
    public TimeSlotBase(LocalTime start_time, LocalTime duration) {
        super(start_time, duration);
    }

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
