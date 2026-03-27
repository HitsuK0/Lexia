package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int appointmentID;
    private String status;
    private List<String> appointementLocals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlotPunctual timeSlotPunctual;
    private TimeSlotBase timeSlotBase;
    private List<AcademicSkill> academicSkillsNeeded;
    private List<ProfessionalSkill> businessSkillsNeeded;

    /**
     * Initialize an Appointment with beneficiary, appointementLocals, interpreters, specialists, academicSkillsNeeded, businessSkillsNeeded,
     * timeSlotPunctual, timeSlotBase and sets the status to 'On hold' by default
     * @param appointmentID The id of the Appointment
     * @param beneficiary The Beneficiary concerned
     * @param appointementLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpretes that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param businessSkillsNeeded List of business skills needed
     * @param timeSlotPunctual For every non-repetitive Appointment, can be null
     * @param timeSlotBase For every repetitive Appointment, can be null
     *                     Note: an Appointment is either a timeSlotPunctual or a timeSlotBase, but not both at the same time
     * @throws NullPointerException if beneficiary, appointementLocals, businessSkillsNeeded, interpreters is null
     * @throws IllegalArgumentException If interpreters or businessSkillsNeeded is empty
     *                                  If timeSlotPunctual and timeSlotBase are not null
     *                                  If appointmentID is negative
     */
    public Appointment(int appointmentID, Beneficiary beneficiary, List<String> appointementLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> businessSkillsNeeded,
                       TimeSlotPunctual timeSlotPunctual, TimeSlotBase timeSlotBase) {
        if(beneficiary == null || appointementLocals == null || interpreters == null || businessSkillsNeeded == null || (timeSlotPunctual == null && timeSlotBase == null)) {
            throw new NullPointerException();
        }

        if(timeSlotPunctual != null && timeSlotBase != null) {
            throw new IllegalArgumentException();
        }

        if(appointmentID < 0) {
            throw new IllegalArgumentException();
        }

        if(interpreters.size() == 0 || businessSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointmentID = appointmentID;
        this.beneficiary = beneficiary;
        this.status = "en attente";
        this.appointementLocals = appointementLocals;
        this.interpreters = interpreters;
        this.timeSlotPunctual = timeSlotPunctual;
        this.timeSlotBase = timeSlotBase;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.businessSkillsNeeded = businessSkillsNeeded;
    }

    /**
     * Initialize an Appointment with no elements
     */
    public Appointment() {
        status = "en attente";
        appointementLocals = new ArrayList<String>();
        interpreters = null;
        timeSlotPunctual = null;
        timeSlotBase = null;
        academicSkillsNeeded = new ArrayList<AcademicSkill>();
        businessSkillsNeeded = new ArrayList<ProfessionalSkill>();
    }

    /**
     * @return the id of the Appointment
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @return the status of the Appointment
     */
    public String getStatus() {

        return status;
    }

    /**
     * @return the list of locals of the Appointment
     */
    public List<String> getAppointementLocals() {

        return appointementLocals;
    }

    /**
     * @return the beneficiary concerned by the Appointment
     */
    public Beneficiary getBeneficiary() {

        return beneficiary;
    }

    /**
     * @return the list of interpretes concerned by the Appointment
     */
    public List<Interpreter> getInterpreters() {

        return interpreters;
    }

    /**
     * @return the time slot punctual, only for non-repetitive Appointments
     * @throws NullPointerException if timeSlotPunctual is null
     */
    public TimeSlotPunctual getTimeSlotPunctual() {
        if(timeSlotPunctual == null) {
            throw new NullPointerException();
        }

        return timeSlotPunctual;
    }

    /**
     * @return the time slot base, only for repetitive Appointments
     * @throws NullPointerException if timeSlotBase is null
     */
    public TimeSlotBase getTimeSlotBase() {
        if(timeSlotBase == null ){
            throw new NullPointerException();
        }

        return timeSlotBase;
    }

    /**
     * @return the list of academic skills needed for the Appointment
     * @throws NullPointerException if there's no academic skill nedded for the Appointment
     */
    public List<AcademicSkill> getAcademicSkillsNeeded() {
        if(academicSkillsNeeded == null) {
            throw new NullPointerException();
        }

        return academicSkillsNeeded;
    }

    /**
     * @return the list of business skills needed for the Appointment
     */
    public List<ProfessionalSkill> getBusinessSkillsNeeded() {

        return businessSkillsNeeded;
    }

    /**
     * @param id the id
     * @throws IllegalArgumentException if the id is negative
     */
    public void setAppointmentID(int id) {
        if(id < 0) {
            throw new IllegalArgumentException();
        }
        this.appointmentID = id;
    }

    /**
     * @param appointementLocals list of locals
     * @throws IllegalArgumentException if appointementLocals is empty
     * @throws NullPointerException if appointementLocals is null
     */
    public void setAppointementLocals(List<String> appointementLocals) {
        if(appointementLocals == null) {
            throw new NullPointerException();
        }

        if(appointementLocals.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointementLocals = appointementLocals;
    }

    /**
     * @param beneficiary the beneficiary concerned
     * @throws NullPointerException if beneficiary is null
     */
    public void setBeneficiary(Beneficiary beneficiary) {
        if(beneficiary == null) {
            throw new NullPointerException();
        }

        this.beneficiary = beneficiary;
    }

    /**
     * @param interpreters the list of interpreters
     * @throws NullPointerException if interpreters is null
     * @throws IllegalArgumentException interpreters is empty
     */
    public void setInterpreters(List<Interpreter> interpreters) {
        if(interpreters == null) {
            throw new NullPointerException();
        }

        if(interpreters.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.interpreters = interpreters;
    }

    /**
     * @param timeSlotPunctual the time slot punctual
     * @throws NullPointerException if timeSlotPunctual is null
     */
    public void setTimeSlotPunctual(TimeSlotPunctual timeSlotPunctual) {
        if(timeSlotPunctual == null) {
            throw new NullPointerException();
        }

        this.timeSlotPunctual = timeSlotPunctual;
    }

    /**
     * @param timeSlotBase the time slot base
     * @throws NullPointerException if timeSlotBase is null
     */
    public void setTimeSlotBase(TimeSlotBase timeSlotBase) {
        if(timeSlotBase == null) {
            throw new NullPointerException();
        }

        this.timeSlotBase = timeSlotBase;
    }

    /**
     * @param academicSkillsNeeded the list of academic skills needed
     * @throws NullPointerException if academicSkillsNeeded is null
     * @throws IllegalArgumentException if academicSkillsNeeded is empty
     */
    public void setAcademicSkillsNeeded(List<AcademicSkill> academicSkillsNeeded) {
        if(academicSkillsNeeded == null) {
            throw new NullPointerException();
        }

        if(academicSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.academicSkillsNeeded = academicSkillsNeeded;
    }

    /**
     * @param businessSkillsNeeded the list of business skills needed
     * @throws NullPointerException if businessSkillsNeeded is null
     * @throws IllegalArgumentException if businessSkillsNeeded is empty
     */
    public void setBusinessSkillsNeeded(List<ProfessionalSkill> businessSkillsNeeded) {
        if(businessSkillsNeeded == null) {
            throw new NullPointerException();
        }

        if(businessSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.businessSkillsNeeded = businessSkillsNeeded;
    }

    /**
     * @param status the status
     * @throws NullPointerException if status is null
     * @throws BadStatusException if status is different from 'accepted' or 'refused'
     *                            if status is the same as the already set status
     *                            if the current status is not on hold
     */
    public void setStatus(String status) throws BadStatusException {
        if(status == null) {
            throw new NullPointerException();
        }

        if(!(status.equals("accepte") || status.equals("refuse")) || this.status.equals(status)) {
            throw new BadStatusException();
        }

        if(!this.status.equals("en attente")) {
            throw new BadStatusException();
        }

        this.status = status;
    }

    public String toString() {
        return ;
    }
}