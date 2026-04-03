package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Vatafu Jean
 * @reviewer Nicolas Jean-François
 */

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
     * @throws IllegalArgumentException if startDate is null
     */
    public TimeSlotPunctual(LocalTime startTime, LocalTime duration, LocalDate startDate) {
        super(startTime, duration);

        if(startDate == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de début ne peut pas être nulle");
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
     * @throws IllegalArgumentException if startDate or endDate is null
     */
    public TimeSlotPunctual(LocalTime startTime, LocalTime duration, LocalDate startDate, LocalDate endDate) {
        super(startTime, duration);

        if(startDate == null || endDate == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de début et la date de fin ne peuvent pas être nulles");
        }

        this.startDate = startDate;
        this.endDate = endDate;
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
     * @throws IllegalArgumentException if startDate is null
     *                                  if this.endDate is not null and startDate is after this.endDate
     */
    public void setStartDate(LocalDate startDate) {
        if(startDate ==  null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de début ne peut pas être nulle");
        }

        if(this.endDate != null && startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de début ne peut pas être après la date de fin");
        }

        this.startDate = startDate;
    }

    /**
     * @param endDate the end date to set
     * @throws IllegalArgumentException if endDate is null
     *                                  if this.startDate is not null and endDate is before this.startDate
     */
    public void setEndDate(LocalDate endDate) {
        if(endDate == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de fin ne peut pas être nulle");
        }

        if(this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de fin ne peut pas être avant la date de début");
        }

        this.endDate = endDate;
    }

    /**
     * Dispatches the overlap check to the correct overlapsWith method
     * based on the runtime type of the given TimeSlot
     * @param timeSlot the TimeSlot to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws IllegalArgumentException if timeSlot is null
     */
    @Override
    public boolean overlapsWith(TimeSlot timeSlot) {
        if(timeSlot == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La tranche horaire ne peut pas être nulle");
        }

        return timeSlot.overlapsWith(this);
    }

    /**
     * Checks if this TimeSlotPunctual overlaps with another TimeSlotPunctual
     * A travel time of 40 minutes is added to this TimeSlot's end time
     * Two TimeSlots overlap if their dates overlap and their times overlap
     * @param punctual the TimeSlotPunctual to check overlap with
     * @return true if the two TimeSlots overlap, false otherwise
     * @throws IllegalArgumentException if punctual is null
     *                              if this.startDate is null
     */
    @Override
    public boolean overlapsWith(TimeSlotPunctual punctual) {
        if(punctual == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La tranche horaire ponctuelle ne peut pas être nulle");
        }

        if(this.startDate == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de début de cette tranche horaire ne peut pas être nulle");
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

        if(!thisEnd.isBefore(punctual.getStartDate()) && !otherEnd.isBefore(this.startDate)) {
            LocalTime thisTime = this.getStartTime().plusSeconds(this.getDuration().toSecondOfDay()).plusMinutes(getTravelTimeMinutes());
            LocalTime otherTime = punctual.getStartTime().plusSeconds(punctual.getDuration().toSecondOfDay());

            if(!thisTime.isBefore((punctual.getStartTime())) && !otherTime.isBefore(this.getStartTime())) {
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
     * @throws IllegalArgumentException if base is null
     *                              if this.startDate is null
     */
    @Override
    public boolean overlapsWith(TimeSlotBase base) {
        if(base == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La tranche horaire répétitive ne peut pas être nulle");
        }

        if(this.startDate == null) {
            throw new IllegalArgumentException("[POJOTimeSlotPunctual] La date de début de cette tranche horaire ne peut pas être nulle");
        }

        if (endDate == null) {
            if (this.startDate.getDayOfWeek().getValue() == base.getDayNumber()) {
                LocalTime thisTime = this.getStartTime().plusSeconds(this.getDuration().toSecondOfDay()).plusMinutes(getTravelTimeMinutes());
                LocalTime otherTime = base.getStartTime().plusSeconds(base.getDuration().toSecondOfDay());

                if (thisTime.isAfter(base.getStartTime()) && otherTime.isAfter(this.getStartTime())) {
                    return true;
                }
            }
        } else {
            LocalDate current = this.startDate;
            while (!current.isAfter(this.endDate)) {
                if (current.getDayOfWeek().getValue() == base.getDayNumber()) {
                    LocalTime thisTotalTime = this.getStartTime().plusSeconds(this.getDuration().toSecondOfDay()).plusMinutes(getTravelTimeMinutes());
                    LocalTime otherTotalTime = base.getStartTime().plusSeconds(base.getDuration().toSecondOfDay());

                    if (thisTotalTime.isAfter(base.getStartTime()) && otherTotalTime.isAfter(this.getStartTime())) {
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
        sb.append("Heure de début : ").append(this.getStartTime()).append("\n");
        sb.append("Durée : ").append(this.getDuration()).append("\n");
        sb.append("Date de début : ").append(this.startDate).append("\n");

        if(endDate == null) {
            sb.append("Date de fin : Aucune date de fin\n");
        } else {
            sb.append("Date de fin : ").append(this.endDate).append("\n");
        }

        return sb.toString();
    }
}