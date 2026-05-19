package be.hers.info.ProjetIntegree.DTO;

import java.util.List;

/**
 * DTO used to carry the appointment's editable profile fields between the HTML form and the controller.
 *
 * @author Rosman Loïs
 * @reviewer Halet Louis, Wellinger Chloé, Nicolas Jean-François
 */

public class DTOAppointment {
    private String status;
    private List<String> appointmentLocals;

    // Each field allows the identification of its own object. This is the form binding.
    private int numBeneficiary;
    private List<Integer> numInterpreters;
    private int numTimeSlot;
    private int numEstablishment;
    private List<Integer> numAcademicSkillsNeeded;
    private List<Integer> numProfessionalSkillsNeeded;

    /**
     * Creates an empty DTOAppointment
     */
    public DTOAppointment() {
    }

    /**
     * Creates a fully initialised DTOAppointment
     * @param status the status of the appointment
     * @param appointmentLocals the list of appointment locals
     * @param numBeneficiary the id of the beneficiary related to the appointment
     * @param numInterpreters the list of interpreters identifiers related to the appointment
     * @param numTimeSlot the id of the time slot related to the appointment
     * @param numEstablishment the id of the establishment related to the appointment
     * @param numAcademicSkillsNeeded the list of academic skills identifiers related to the appointment
     * @param numProfessionalSkillsNeeded the list of professional skills identifiers related to the appointment
     */
    public DTOAppointment(String status, List<String> appointmentLocals, int numBeneficiary,
                          List<Integer> numInterpreters, int numTimeSlot, int numEstablishment,
                          List<Integer> numAcademicSkillsNeeded, List<Integer> numProfessionalSkillsNeeded) {
        this.status = status;
        this.appointmentLocals = appointmentLocals;
        this.numBeneficiary = numBeneficiary;
        this.numInterpreters = numInterpreters;
        this.numTimeSlot = numTimeSlot;
        this.numEstablishment = numEstablishment;
        this.numAcademicSkillsNeeded = numAcademicSkillsNeeded;
        this.numProfessionalSkillsNeeded = numProfessionalSkillsNeeded;
    }

    /**
     * @return the status of the appointment
     */
    public String getStatus() {
        return status;
    }

    /**
     * Initialize the status
     * @param status the status of the appointment
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the list of appointment locals
     */
    public List<String> getAppointmentLocals() {
        return appointmentLocals;
    }

    /**
     * Initialize the list of appointment locals
     * @param appointmentLocals the list of appointment locals
     */
    public void setAppointmentLocals(List<String> appointmentLocals) {
        this.appointmentLocals = appointmentLocals;
    }

    /**
     * @return the id of the beneficiary
     */
    public int getNumBeneficiary() {
        return numBeneficiary;
    }

    /**
     * Initialize the id of the beneficiary
     * @param numBeneficiary the id of the beneficiary related to the appointment
     */
    public void setNumBeneficiary(int numBeneficiary) {
        this.numBeneficiary = numBeneficiary;
    }

    /**
     * @return the interpreters identifiers
     */
    public List<Integer> getNumInterpreters() {
        return numInterpreters;
    }

    /**
     * Initialize the list of interpreters identifiers
     * @param numInterpreters the list of interpreters identifiers related to the appointment
     */
    public void setNumInterpreters(List<Integer> numInterpreters) {
        this.numInterpreters = numInterpreters;
    }

    /**
     * @return the id of the time slot
     */
    public int getNumTimeSlot() {
        return numTimeSlot;
    }

    /**
     * Initialize the id of the time slot
     * @param numTimeSlot the id of the time slot related to the appointment
     */
    public void setNumTimeSlot(int numTimeSlot) {
        this.numTimeSlot = numTimeSlot;
    }

    /**
     * @return the id of the establishment
     */
    public int getNumEstablishment() {
        return numEstablishment;
    }

    /**
     * Initialize the id of the establishment
     * @param numEstablishment the id of the establishment related to the appointment
     */
    public void setNumEstablishment(int numEstablishment) {
        this.numEstablishment = numEstablishment;
    }

    /**
     * @return the academic skills identifiers
     */
    public List<Integer> getNumAcademicSkillsNeeded() {
        return numAcademicSkillsNeeded;
    }

    /**
     * Initialize the list of academic skills identifiers
     * @param numAcademicSkillsNeeded the list of academic skills identifiers related to the appointment
     */
    public void setNumAcademicSkillsNeeded(List<Integer> numAcademicSkillsNeeded) {
        this.numAcademicSkillsNeeded = numAcademicSkillsNeeded;
    }

    /**
     * @return the professional skills identifiers
     */
    public List<Integer> getNumProfessionalSkillsNeeded() {
        return numProfessionalSkillsNeeded;
    }

    /**
     * Initialize the list of professional skills identifiers
     * @param numProfessionalSkillsNeeded the list of professional skills identifiers related to the appointment
     */
    public void setNumProfessionalSkillsNeeded(List<Integer> numProfessionalSkillsNeeded) {
        this.numProfessionalSkillsNeeded = numProfessionalSkillsNeeded;
    }
}
