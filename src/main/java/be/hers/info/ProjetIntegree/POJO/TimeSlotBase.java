package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotBase extends TimeSlot {
    private int dayNumber;

    /** Minimum day number (1 = Monday) */
    private static final int MIN_DAY = 1;

    /** Maximum day number (7 = Sunday) */
    private static final int MAX_DAY = 7;

    /**
     * Initialize a TimeSlotBase with startTime, duration and dayNumber
     * @param startTime the start time of the time slot
     * @param duration the duration
     * @param dayNumber the number of the day concerned
     * @throws NullPointerException if startTime or duration is null
     * @throws IllegalArgumentException if dayNumber is smaller than MIN_DAY or greater than MAX_DAY
     */
    public TimeSlotBase(LocalTime startTime, LocalTime duration, int dayNumber) {
        super(startTime, duration);

        if(dayNumber < MIN_DAY || dayNumber > MAX_DAY) {
            throw new IllegalArgumentException();
        }
        this.dayNumber = dayNumber;
    }

    /**
     * Initialize a TimeSlotBase with no elements
     */
    public TimeSlotBase() {
        super();
        this.dayNumber = 0;
    }

    /**
     * @return the day of the time slot base (1 = Monday, 7 = Sunday)
     */
    public int getJour() {
        return dayNumber;
    }

    /**
     * @param dayNumber  the day to set
     * @throws IllegalArgumentException if dayNumber is smaller than MIN_DAY or greater than MAX_DAY
     */
    public void setDayNumber(int dayNumber) {
        if(dayNumber < MIN_DAY || dayNumber > MAX_DAY) {
            throw new IllegalArgumentException();
        }

        this.dayNumber = dayNumber;
    }

    /**
     * Dispatches the overlap check to the correct overlapsWith method
     * based on the runtime type of the given TimeSlot
     * @param timeSlot the TimeSlot to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if timeSlot is null
     */
    @Override
    public boolean overlapsWith(TimeSlot timeSlot) {
        if(timeSlot == null) {
            throw new NullPointerException();
        }

        return timeSlot.overlapsWith(this);
    }

    /**
     * Checks if this TimeSlotBase overlaps with a TimeSlotPunctual
     * A travel time of 40 minutes is added to this TimeSlot's end time
     * If punctual's endDate is null, only the punctual's startDate day of week is compared to this dayNumber
     * If punctual's endDate is not null, all days between punctual's startDate and endDate are checked
     * @param punctual the TimeSlotPunctual to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if punctual is null
     *                              if punctual's startDate is null
     * @throws IllegalArgumentException if this.dayNumber is 0
     */
    @Override
    public boolean overlapsWith(TimeSlotPunctual punctual) {
        if(punctual == null) {
            throw new NullPointerException();
        }

        if(this.dayNumber == 0) {
            throw new IllegalArgumentException();
        }

        if(punctual.getStartDate() == null) {
            throw new NullPointerException();
        }

        if(punctual.getEndDate() == null) {
            if(punctual.getStartDate().getDayOfWeek().getValue() == this.dayNumber) {
                LocalTime thisTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
                LocalTime otherTime = punctual.getStartTime().plusSeconds(punctual.getDuration().toSecondOfDay());

                if(thisTime.isAfter(punctual.getStartTime()) && otherTime.isAfter(this.startTime)) {
                    return true;
                }
            }
        } else {
            LocalDate current = punctual.getStartDate();
            while(!current.isAfter(punctual.getEndDate())) {
                if(current.getDayOfWeek().getValue() == this.dayNumber) {
                    LocalTime thisTotalTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
                    LocalTime otherTotalTime = punctual.getStartTime().plusSeconds(punctual.getDuration().toSecondOfDay());

                    if(thisTotalTime.isAfter(punctual.getStartTime()) && otherTotalTime.isAfter(this.startTime)) {
                        return true;
                    }
                }
                current = current.plusDays(1);
            }
        }
        return false;
    }

    /**
     * Checks if this TimeSlotBase overlaps with another TimeSlotBase
     * A travel time of 40 minutes is added to this TimeSlot's end time
     * Two TimeSlotBase overlap if they have the same dayNumber and their times overlap
     * @param base the TimeSlotBase to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if base is null
     * @throws IllegalArgumentException if this.dayNumber is 0
     */
    @Override
    public boolean overlapsWith(TimeSlotBase base) {
        if(base == null) {
            throw new NullPointerException();
        }

        if(this.dayNumber == 0) {
            throw new IllegalArgumentException();
        }

        if(this.dayNumber != base.getJour()) {
            return false;
        }

        LocalTime thisTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
        LocalTime otherTime = base.getStartTime().plusSeconds(base.getDuration().toSecondOfDay());

        if(thisTime.isAfter(base.getStartTime()) && otherTime.isAfter(this.startTime)) {
            return true;
        }

        return false;
    }

    /**
     * @return a String containing the start time, duration and day number
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tranche horaire répétitive :\n");
        sb.append("Heure de début : ").append(this.startTime).append("\n");
        sb.append("Durée : ").append(this.duration).append("\n");
        sb.append("Jour : ").append(this.dayNumber).append("\n");

        return sb.toString();
    }
}