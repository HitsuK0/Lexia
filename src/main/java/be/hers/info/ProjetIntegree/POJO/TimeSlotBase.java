package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotBase extends TimeSlot {
    private int dayNumber;

    private static final int MIN_DAY = 1;
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
     * @return the day of the time slot base
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
     * @param timeSlot
     * @throws IllegalArgumentException if dayNumber is smaller than MIN_DAY or greater than MAX_DAY
     */
    @Override
    public boolean overlapsWith(TimeSlot timeSlot) {
        if(timeSlot == null) {
            throw new NullPointerException();
        }

        return timeSlot.overlapsWith(this);
    }

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

    @Override
    public String toString() {

    }
}
