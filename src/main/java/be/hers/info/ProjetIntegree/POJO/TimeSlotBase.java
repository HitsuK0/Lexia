package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalTime;

public class TimeSlotBase extends TimeSlot {
    private int dayNumber;

    private static final int MIN_DAY = 1;
    private static final int MAX_DAY = 7;

    public TimeSlotBase(LocalTime startTime, LocalTime duration, int dayNumber) {
        super(startTime, duration);

        if(dayNumber < MIN_DAY || dayNumber > MAX_DAY) {
            throw new IllegalArgumentException();
        }
        this.dayNumber = dayNumber;
    }

    public TimeSlotBase() {
        super();
        this.dayNumber = 0;
    }

    @Override
    public boolean overlapsWith(TimeSlot timeSlot) {
        return false;
    }

    @Override
    public boolean overlapsWith(TimeSlotPunctual timeSlotPunctual) {
        return false;
    }

    @Override
    public boolean overlapsWith(TimeSlotBase timeSlotBase) {
        return false;
    }
}
