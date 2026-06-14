package be.hers.info.ProjetIntegree.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO used to carry the appointment's editable profile fields between the HTML form and the controller.
 *
 * @author Rosman Loïs
 * @reviewer Nicolas Jean-François
 */

public class DTOAppointmentForm {

    private List<String> appointmentLocals;

    // Each field allows the identification of its own object. This is the form binding.
    private int numBeneficiary;
    private int numEstablishment;
    private List<Integer> numAcademicSkillsNeeded;
    private List<Integer> numProfessionalSkillsNeeded;

    // Each field allows the identification of the TimeSlot. This is the form binding.
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Creates an empty DTOAppointment
     */
    public DTOAppointmentForm() {
        numAcademicSkillsNeeded = new ArrayList<>();
        numProfessionalSkillsNeeded = new ArrayList<>();
    }

    /**
     * Creates a fully initialised DTOAppointment
     *
     * @param appointmentLocals           the list of appointment locals
     * @param numBeneficiary              the id of the beneficiary related to the appointment
     * @param numEstablishment            the id of the establishment related to the appointment
     * @param numAcademicSkillsNeeded     the list of academic skills identifiers related to the appointment
     * @param numProfessionalSkillsNeeded the list of professional skills identifiers related to the appointment
     * @param startDate                   the date start of the time slot
     * @param endDate                     the date end of the time slot
     * @param startTime                   the start time of the time slot
     * @param endTime                     the end time of the time slot
     */
    public DTOAppointmentForm(List<String> appointmentLocals, int numBeneficiary,
                              int numEstablishment, List<Integer> numAcademicSkillsNeeded,
                              List<Integer> numProfessionalSkillsNeeded,
                              LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.appointmentLocals = appointmentLocals;
        this.numBeneficiary = numBeneficiary;
        this.numEstablishment = numEstablishment;
        this.numAcademicSkillsNeeded = numAcademicSkillsNeeded;
        this.numProfessionalSkillsNeeded = numProfessionalSkillsNeeded;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * @return the list of appointment locals
     */
    public List<String> getAppointmentLocals() {
        return appointmentLocals;
    }

    /**
     * Initialize the list of appointment locals
     *
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
     *
     * @param numBeneficiary the id of the beneficiary related to the appointment
     */
    public void setNumBeneficiary(int numBeneficiary) {
        this.numBeneficiary = numBeneficiary;
    }

    /**
     * @return the id of the establishment
     */
    public int getNumEstablishment() {
        return numEstablishment;
    }

    /**
     * Initialize the id of the establishment
     *
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
     * If this list in parameter is null. An empty list is initialize
     * @param numAcademicSkillsNeeded the list of academic skills identifiers related to the appointment
     */
    public void setNumAcademicSkillsNeeded(List<Integer> numAcademicSkillsNeeded) {
        if (numAcademicSkillsNeeded == null)
            this.numAcademicSkillsNeeded = new ArrayList<>();
        else
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
     * If this list in parameter is null. An empty list is initialize
     * @param numProfessionalSkillsNeeded the list of professional skills identifiers related to the appointment
     */
    public void setNumProfessionalSkillsNeeded(List<Integer> numProfessionalSkillsNeeded) {
        if (numAcademicSkillsNeeded == null)
            this.numProfessionalSkillsNeeded = new ArrayList<>();
        else
            this.numProfessionalSkillsNeeded = numProfessionalSkillsNeeded;
    }

    /**
     * @return the date start of the time slot
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Initialize the date start of the time slot
     *
     * @param startDate the date start of the time slot
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the date end of the time slot
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Initialize the date end of the time slot
     *
     * @param endDate the date end of the time slot
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the time start of the time slot
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Initialize the start time of the time slot
     *
     * @param startTime the start time of the time slot
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the time end of the time slot
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Initialize the end time of the time slot
     *
     * @param endTime the end time of the time slot
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
