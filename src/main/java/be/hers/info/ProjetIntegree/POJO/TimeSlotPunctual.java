package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlotPunctual extends TimeSlot {
    private final LocalDate startDate;
    private final LocalDate endDate;

    private static final int TRAVEL_TIME_MINUTES = 40;

    public TimeSlotPunctual(LocalTime startTime, LocalTime duration, LocalDate startDate) {
        super(startTime, duration);
        this.startDate = startDate;
        endDate = null;
    }

    public TimeSlotPunctual(LocalTime startTime, LocalTime duration, LocalDate startDate, LocalDate endDate) {
        super(startTime, duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {

        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public boolean overlapsWith(TimeSlot timeSlot) {

        return timeSlot.overlapsWith(this);
    }

    @Override
    public boolean overlapsWith(TimeSlotPunctual punctual) {
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

        if(thisEnd.isBefore(punctual.getStartDate()) || otherEnd.isBefore(this.startDate)) {
            return false;
        }

        LocalTime thisTime = this.startTime.plusSeconds(this.duration.toSecondOfDay()).plusMinutes(TRAVEL_TIME_MINUTES);
        LocalTime otherTime = punctual.getStartTime().plusSeconds(punctual.getDuration().toSecondOfDay());

        if(thisTime.isBefore((punctual.getStartTime())) || otherTime.isBefore(this.startTime)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean overlapsWith(TimeSlotBase time_slot_base) {
        return false;
    }
}