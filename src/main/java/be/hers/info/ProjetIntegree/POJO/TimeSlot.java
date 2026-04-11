package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Vatafu Jean
 * @reviewer Nicolas Jean-François, Halet Louis
 */

import java.time.LocalTime;

public abstract class TimeSlot {
    private int numTimeSlot;
    private LocalTime startTime;
    private LocalTime duration;

    /* Travel time in minutes added to the end of a time slot to account for travel between appointments */
    private static final int TRAVEL_TIME_MINUTES = 40;

    /**
     * Initialize a TimeSlot with no elements
     * The parameter numTimeSlot can only be initialized with setNumTimeSlot
     */
    public TimeSlot() {
        this.startTime = null;
        this.duration = null;
    }

    /**
     * Initialize a TimeSlot with numTimeSlot, startTime and duration
     * @param numTimeSlot the id of the time slot
     * @param startTime the start time of the time slot
     * @param duration the duration
     * @throws IllegalArgumentException if startTime or duration is null
     */
    public TimeSlot(int numTimeSlot, LocalTime startTime, LocalTime duration) {
        if(startTime == null || duration == null) {
            throw new IllegalArgumentException("[POJOTimeSlot] L'heure de début et la durée ne peuvent pas être nulles");
        }

        this.numTimeSlot = numTimeSlot;
        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * Initialize a TimeSlot with startTime and duration
     * @param startTime the start time of the time slot
     * @param duration the duration
     * @throws IllegalArgumentException if startTime or duration is null
     */
    public TimeSlot(LocalTime startTime, LocalTime duration) {
        if(startTime == null || duration == null) {
            throw new IllegalArgumentException("[POJOTimeSlot] L'heure de début et la durée ne peuvent pas être nulles");
        }

        this.startTime = startTime;
        this.duration = duration;
    }

    /**
     * @return the id of the time slot
     */
    public int getNumTimeSlot() {

        return numTimeSlot;
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
     * @return the minutes of travel time
     */
    public int getTravelTimeMinutes() {

        return TRAVEL_TIME_MINUTES;
    }

    /**
     * @param numTimeSlot the id to set
     */
    public void setNumTimeSlot(int numTimeSlot) {

        this.numTimeSlot = numTimeSlot;
    }

    /**
     * @param startTime the start time to set
     * @throws IllegalArgumentException if startTime is null
     */
    public void setStartTime(LocalTime startTime) {
        if(startTime == null) {
            throw new IllegalArgumentException("[POJOTimeSlot] L'heure de début ne peut pas être nulle");
        }

        this.startTime = startTime;
    }

    /**
     * @param duration the duration to set
     * @throws IllegalArgumentException if duration is null
     */
    public void setDuration(LocalTime duration) {
        if(duration == null) {
            throw new IllegalArgumentException("[POJOTimeSlot] La durée ne peut pas être nulle");
        }

        this.duration = duration;
    }

    /**
     * Dispatches the overlap check to the correct overlapsWith method
     * based on the runtime type of the given TimeSlot
     * @param timeSlot the TimeSlot to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws IllegalArgumentException if timeSlot is null
     */
    public abstract boolean overlapsWith(TimeSlot timeSlot);

    /**
     * Checks if this TimeSlot overlaps with a TimeSlotPunctual
     * @param timeSlotPunctual the TimeSlotPunctual to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws IllegalArgumentException if timeSlotPunctual is null
     */
    public abstract boolean overlapsWith(TimeSlotPunctual timeSlotPunctual);

    /**
     * Checks if this TimeSlot overlaps with a TimeSlotBase
     * @param timeSlotBase the TimeSlotBase to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws IllegalArgumentException if timeSlotBase is null
     */
    public abstract boolean overlapsWith(TimeSlotBase timeSlotBase);

    /**
     * @return a String representation of the TimeSlot
     */
    @Override
    public abstract String toString();
}
