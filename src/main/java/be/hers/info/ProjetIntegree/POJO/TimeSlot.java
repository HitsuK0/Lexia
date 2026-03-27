package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalTime;

public abstract class TimeSlot {
    protected LocalTime startTime;
    protected LocalTime duration;

    public TimeSlot(LocalTime startTime, LocalTime duration) {
        this.startTime = startTime;
        this.duration = duration;
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

    public abstract boolean overlapsWith(TimeSlot timeSlot);
    public abstract boolean overlapsWith(TimeSlotPunctual timeSlotPunctual);
    public abstract boolean overlapsWith(TimeSlotBase timeSlotBase);
}
