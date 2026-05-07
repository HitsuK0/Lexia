package be.hers.info.ProjetIntegree.DTO;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public class DTOAbsence {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private boolean status;
    private boolean privateReason;
    private boolean fullDay;




    public DTOAbsence() {

    }

    public DTOAbsence(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String reason, boolean status, boolean privateReason, boolean fullDay) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.privateReason = privateReason;
        this.fullDay = fullDay;
    }

    public DTOAbsence(LocalTime startTime, LocalTime endTime, String reason, boolean status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.fullDay = false;
        this.privateReason = false;
    }


    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPrivateReason(boolean privateReason) {
        this.privateReason = privateReason;
    }

    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isPrivateReason() {
        return privateReason;
    }

    public boolean isFullDay() {
        return fullDay;
    }
}
