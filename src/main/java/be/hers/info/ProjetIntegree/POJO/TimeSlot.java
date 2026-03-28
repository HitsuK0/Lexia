package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalTime;

public abstract class TimeSlot {
    protected LocalTime startTime;
    protected LocalTime duration;

    protected static final int TRAVEL_TIME_MINUTES = 40;

    /**
     * Initialize a TimeSlot with startTime and duration
     * @param startTime the start time of the time slot
     * @param duration the duration
     * @throws NullPointerException if startTime or duration is null
     */
    public TimeSlot(LocalTime startTime, LocalTime duration) {
        if(startTime == null || duration == null) {
            throw new NullPointerException();
        }

        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Initialize a TimeSlot with no elements
     */
    public TimeSlot() {
        this.startTime = null;
        this.duration = null;
    }

    /**
     * @return the start time of the time slot
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return the duration
     */
    public LocalTime getDuration() {
        return duration;
    }

    /**
     * @param startTime the start time to set
     * @throws NullPointerException if startTime is null
     */
    public void setStartTime(LocalTime startTime) {
        if(startTime == null) {
            throw new NullPointerException();
        }

        this.startTime = startTime;
    }

    /**
     * @param duration the duration to set
     * @throws NullPointerException if duration is null
     */
    public void setDuration(LocalTime duration) {
        if(duration == null) {
            throw new NullPointerException();
        }

        this.duration = duration;
    }

    /**
     * Dispatches the overlap check to the correct overlapsWith method
     * based on the runtime type of the given TimeSlot
     * @param timeSlot the TimeSlot to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if timeSlot is null
     */
    public abstract boolean overlapsWith(TimeSlot timeSlot);

    /**
     * Checks if this TimeSlot overlaps with a TimeSlotPunctual
     * @param timeSlotPunctual the TimeSlotPunctual to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if timeSlotPunctual is null
     */
    public abstract boolean overlapsWith(TimeSlotPunctual timeSlotPunctual);

    /**
     * Checks if this TimeSlot overlaps with a TimeSlotBase
     * @param timeSlotBase the TimeSlotBase to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if timeSlotBase is null
     */
    public abstract boolean overlapsWith(TimeSlotBase timeSlotBase);
}
