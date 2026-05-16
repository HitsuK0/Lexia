package be.hers.info.ProjetIntegree.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTOAbsence is a minimalist Absence used for the form in the web page.
 * The attribute are most the same than the Absence.
 * Sping use the setter to put the data in the attribute.
 */
public class DTOAbsence {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private String status;
    private boolean privateReason;
    private boolean fullDay;


    /**
     * Instanciate a DTOAbsence without any param.
     */
    public DTOAbsence() {

    }

    /**
     *
     * @param startDate the date when the Absence start.
     * @param endDate the date when the Absence end.
     * @param startTime the time when the Absence start.
     * @param endTime the time when the Absence end.
     * @param reason the reason of the Absence.
     * @param status the status of the Absence ('en attente', 'accepte' or 'refuse')
     * @param privateReason true if the reason is private.
     * @param fullDay true if the Absence is the fullDay.
     */
    public DTOAbsence(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String reason, String status, boolean privateReason, boolean fullDay) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.privateReason = privateReason;
        this.fullDay = fullDay;
    }

    /**
     *
     * @param startTime the time when the Absence start.
     * @param endTime the time when the Absence end.
     * @param reason the reason of the Absence.
     * @param status the status of the Absence ('en attente', 'accepte' or 'refuse')
     */
    public DTOAbsence(LocalTime startTime, LocalTime endTime, String reason, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
        this.fullDay = false;
        this.privateReason = false;
    }


    /**
     * Set a startDate with a LocalDate.
     * @param startDate the date when the Absence start.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Set a endDate with a LocalDate.
     * @param endDate the date when the Absence end.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Set a time when the Absence start.
     * @param startTime the time when the Absence start.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Set a time when the Absence end.
     * @param endTime the time when the Absence end.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Set a reason for the Absence.
     * @param reason is the reason why the Absence exist.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Set the status of the Absence. ('en attente', 'accepte' or 'refuse').
     * @param status in ('en attente', 'accepte' or 'refuse')
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Set if the reason is private or not.
     * @param privateReason true if the reason is private.
     */
    public void setPrivateReason(boolean privateReason) {
        this.privateReason = privateReason;
    }

    /**
     * Set if the Absence is a whole day or not.
     * @param fullDay true if the Absence is a whole day.
     */
    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    /**
     *
     * @return startDate, the date when the Absence start.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     *
     * @return endDate, the date when the Absence end.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     *
     * @return startTime, the time when the Absence start.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     *
     * @return endTime, the time when the Absence end.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     *
     * @return reason, the Reason why there is an Absence.
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @return status, the Status of the Absence.
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @return privateReason, if the reason is private or not.
     */
    public boolean isPrivateReason() {
        return privateReason;
    }

    /**
     *
     * @return fullDay, define if the Absence is the fullDay of the dayStart or not.
     */
    public boolean isFullDay() {
        return fullDay;
    }
}
