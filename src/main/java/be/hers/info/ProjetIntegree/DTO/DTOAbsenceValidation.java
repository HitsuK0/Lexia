package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.TimeSlot;

public class DTOAbsenceValidation {
    private int numAbsence;
    private String status;
    private String reason;
    private boolean privateReason;
    private TimeSlot timeSlot;
    private String name;

    /**
     * Instanciate a DTOAbsenceValidation without any param.
     */
    public DTOAbsenceValidation(){

    }
    /**
     * Create an Absence with all attribute
     *
     * @param numAbsence    the numero of the absence
     * @param status        the status of the absence (it can only be among these values: 'en attente', 'accepte' or 'refuse')
     * @param timeSlot      the time slot of absence
     * @param reason        the reason of absence, can be null
     * @param privateReason the privateReason of absence, defines if the reason is private or not
     * @param name the name of the interpreter
     * @throws BadStatusException       If the absence status is not among these values: 'en attente', 'accepte' or 'refuse'
     * @throws IllegalArgumentException if timeSlot is null
     */
    public DTOAbsenceValidation(int numAbsence, String status, TimeSlot timeSlot, String reason, boolean privateReason, String name) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        if (timeSlot == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null");
        this.numAbsence = numAbsence;
        this.status = status;
        this.timeSlot = timeSlot;

        if (reason == null) {
            this.reason = "";
        } else {
            this.reason = reason;
        }

        this.privateReason = privateReason;
        this.name = name;
    }
    /**
     *
     * @return the reason of absence
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @param reason the reason of absence
     */
    public void setReason(String reason) {
        if (reason == null) {
            this.reason = "";
        } else {
            this.reason = reason;
        }
    }

    /**
     *
     * @return privateReason which defines if the reason is private or not
     */
    public boolean isPrivateReason() {
        return privateReason;
    }

    /**
     *
     * @param privateReason the privateReason state of absence
     */
    public void setPrivateReason(boolean privateReason) {
        this.privateReason = privateReason;
    }

    /**
     *
     * @return the time slot of absence
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     *
     * @param timeSlot the time slot of absence
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null)
            throw new IllegalArgumentException("[POJOAbsence] La tranche horaire de l'absence ne peut pas être null");

        this.timeSlot = timeSlot;
    }

    /**
     *
     * @return the status of the absence (it can only be among these values: 'en attente', 'accepte' or 'refuse')
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status the status of the absence (it can only be among these values: 'en attente', 'accepte' or 'refuse')
     * @throws BadStatusException If the absence status is not among these values: 'en attente', 'accepte' or 'refuse'
     */
    public void setStatus(String status) throws BadStatusException {
        if (!status.equals("en attente") && !status.equals("accepte") && !status.equals("refuse"))
            throw new BadStatusException("[POJOAbsence] Mauvais status de l'absence");
        this.status = status;
    }

    /**
     *
     * @return the numero of the absence
     */
    public int getNumAbsence() {
        return numAbsence;
    }

    /**
     *
     * @param numAbsence the numero of the absence
     * @throws IllegalArgumentException if numAbsence is negative
     */
    public void setNumAbsence(int numAbsence) {
        if (numAbsence < 0)
            throw new IllegalArgumentException("[POJOAbsence] Le numéro de l'absence ne peut pas être négatif");
        this.numAbsence = numAbsence;
    }
    /**
     *
     * @return the name of interpreter
     */
    public String getName() {
        return name;
    }
    /**
     *
     * @param name the name of interpreter
     */
    public void setName(String name) {
        this.name = name;
    }
}
