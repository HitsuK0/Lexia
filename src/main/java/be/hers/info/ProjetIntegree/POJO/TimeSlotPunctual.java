package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotPunctual extends TimeSlot {
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Initialize a TimeSlotPunctual with startTime, duration, startDate and endDate is null by default
     * @param startTime the start time of the time slot
     * @param duration the duration
     * @param startDate the concerned date
     * @throws NullPointerException if startDate is null
     */
    public TimeSlotPunctual(LocalTime startTime, LocalTime duration, LocalDate startDate) {
        super(startTime, duration);

        if(startDate == null) {
            throw new NullPointerException();
        }

        this.startDate = startDate;
        this.endDate = null;
    }

    /**
     * Initialize a TimeSlotPunctual with startTime, duration, startDate and endDate
     * @param startTime the start time of the time slot
     * @param duration the duration
     * @param startDate the start date of the time slot
     * @param endDate the end date of the time slot
     * @throws NullPointerException if startDate or endDate is null
     */
    public TimeSlotPunctual(LocalTime startTime, LocalTime duration, LocalDate startDate, LocalDate endDate) {
        super(startTime, duration);

        if(startDate == null || endDate == null) {
            throw new NullPointerException();
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Initialize a TimeSlotPunctual with no elements
     */
    public TimeSlotPunctual() {
        super();
        this.startDate = null;
        this.endDate = null;
    }

    /**
     * @return the start date
     */
    public LocalDate getStartDate() {

        return startDate;
    }

    /**
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param startDate the start date to set
     * @throws NullPointerException if startDate is null
     * @throws IllegalArgumentException if this.startDate is not null and this.startDate is after startDate
     */
    public void setStartDate(LocalDate startDate) {
        if(startDate ==  null) {
            throw new NullPointerException();
        }

        if(this.startDate != null && this.startDate.isAfter(startDate)) {
            throw new IllegalArgumentException();
        }

        this.startDate = startDate;
    }

    /**
     * @param endDate the end date to set
     * @throws NullPointerException if endDate is null
     * @throws IllegalArgumentException if this.startDate is not null and endDate is before this.startDate
     */
    public void setEndDate(LocalDate endDate) {
        if(endDate == null) {
            throw new NullPointerException();
        }

        if(this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException();
        }

        this.endDate = endDate;
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
     * Checks if this TimeSlotPunctual overlaps with another TimeSlotPunctual
     * A travel time of 40 minutes is added to this TimeSlot's end time
     * Two TimeSlots overlap if their dates overlap and their times overlap
     * @param punctual the TimeSlotPunctual to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if punctual is null
     *                              if this.startDate is null
     */
    @Override
    public boolean overlapsWith(TimeSlotPunctual punctual) {
        if(punctual == null) {
            throw new NullPointerException();
        }

        if(this.startDate == null) {
            throw new NullPointerException();
        }

        LocalDate thisEnd = null;
        if(this.endDate == null) {
            thisEnd = this.startDate;
        } else {
            thisEnd = this.endDate;
        }

        LocalDate otherEnd = null;
        if(punctual.getEndDate() == null) {
            otherEnd = punctual.getStartDate();
        } else {
            otherEnd = punctual.getEndDate();
        }

        if(thisEnd.isAfter(punctual.getStartDate()) && otherEnd.isAfter(this.startDate)) {
            LocalTime thisTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
            LocalTime otherTime = punctual.getStartTime().plusSeconds(punctual.getDuration().toSecondOfDay());

            if(thisTime.isAfter((punctual.getStartTime())) && otherTime.isAfter(this.startTime)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if this TimeSlotPunctual overlaps with a TimeSlotBase
     * A travel time of 40 minutes is added to this TimeSlot's end time
     * If endDate is null, only the startDate's day of week is compared to the TimeSlotBase's day
     * If endDate is not null, all days between startDate and endDate are checked
     * @param base the TimeSlotBase to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws NullPointerException if base is null
     *                              if this.startDate is null
     */
    @Override
    public boolean overlapsWith(TimeSlotBase base) {
        if(base == null) {
            throw new NullPointerException();
        }

        if(this.startDate == null) {
            throw new NullPointerException();
        }

        if (endDate == null) {
            if (this.startDate.getDayOfWeek().getValue() == base.getJour()) {
                LocalTime thisTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
                LocalTime otherTime = base.getStartTime().plusSeconds(base.getDuration().toSecondOfDay());

                if (thisTime.isAfter(base.getStartTime()) && otherTime.isAfter(this.startTime)) {
                    return true;
                }
            }
        } else {
            LocalDate current = this.startDate;
            while (!current.isAfter(this.endDate)) {
                if (current.getDayOfWeek().getValue() == base.getJour()) {
                    LocalTime thisTotalTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
                    LocalTime otherTotalTime = base.getStartTime().plusSeconds(base.getDuration().toSecondOfDay());

                    if (thisTotalTime.isAfter(base.getStartTime()) && otherTotalTime.isAfter(this.startTime)) {
                        return true;
                    }
                }
                current = current.plusDays(1);
            }
        }
        return false;
    }

    /**
     * @return a String containing the start time, duration, start date and end date
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tranche horaire ponctuelle :\n");
        sb.append("Heure de début : ").append(this.startTime).append("\n");
        sb.append("Durée : ").append(this.duration).append("\n");
        sb.append("Date de début : ").append(this.startDate).append("\n");

        if(endDate == null) {
            sb.append("Date de fin : Aucune date de fin\n");
        } else {
            sb.append("Date de fin : ").append(this.endDate).append("\n");
        }

        return sb.toString();
    }
}