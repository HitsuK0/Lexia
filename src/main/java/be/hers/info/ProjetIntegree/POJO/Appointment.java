package be.hers.info.ProjetIntegree.POJO;

import be.hers.info.ProjetIntegree.Exceptions.AppointmentException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private String status;
    private List<String> appointementLocals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlotPunctual timeSlotPunctual;
    private TimeSlotBase timeSlotBase;
    private List<AcademicSkill> academicSkillsNeeded;
    private List<BusinessSkill> businessSkillsNeeded;

    public Appointment(Beneficiary beneficiary, List<String> appointementLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<BusinessSkill> businessSkillsNeeded,
                       TimeSlotPunctual timeSlotPunctual, TimeSlotBase timeSlotBase) {
        if(beneficiary == null || appointementLocals == null || interpreters == null || academicSkillsNeeded == null || businessSkillsNeeded == null || timeSlotPunctual == null || timeSlotBase == null) {
            throw new NullPointerException();
        }

        if(appointementLocals.size() == 0 || interpreters.size() == 0 || academicSkillsNeeded.size() == 0 || businessSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.beneficiary = beneficiary;
        this.status = "En attente";
        this.appointementLocals = appointementLocals;
        this.interpreters = interpreters;
        this.timeSlotPunctual = timeSlotPunctual;
        this.timeSlotBase = timeSlotBase;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.businessSkillsNeeded = businessSkillsNeeded;
    }

    public Appointment() {
        status = "En attente";
        appointementLocals = new ArrayList<String>();
        interpreters = null;
        timeSlotPunctual = null;
        timeSlotBase = null;
        academicSkillsNeeded = new ArrayList<AcademicSkill>();
        businessSkillsNeeded = new ArrayList<BusinessSkill>();
    }

    public String getStatus() {

        return status;
    }

    public List<String> getAppointementLocals() {

        return appointementLocals;
    }

    public Beneficiary getBeneficiary() {

        return beneficiary;
    }

    public List<Interpreter> getInterpreters() {

        return interpreters;
    }

    public TimeSlotPunctual getTimeSlotPunctual() {

        return timeSlotPunctual;
    }

    public TimeSlotBase getTimeSlotBase() {

        return timeSlotBase;
    }

    public List<AcademicSkill> getAcademicSkillsNeeded() {

        return academicSkillsNeeded;
    }

    public List<BusinessSkill> getBusiness_skills_needed() {

        return businessSkillsNeeded;
    }

    public void setAppointementLocals(List<String> appointementLocals) {
        if(appointementLocals == null) {
            throw new NullPointerException();
        }

        if(appointementLocals.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointementLocals = appointementLocals;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        if(beneficiary == null) {
            throw new NullPointerException();
        }

        this.beneficiary = beneficiary;
    }

    public void setInterpreters(List<Interpreter> interpreters) {
        if(interpreters == null) {
            throw new NullPointerException();
        }

        if(interpreters.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.interpreters = interpreters;
    }

    public void setTimeSlotPunctual(TimeSlotPunctual timeSlotPunctual) {
        if(timeSlotPunctual == null) {
            throw new NullPointerException();
        }

        this.timeSlotPunctual = timeSlotPunctual;
    }

    public void setTimeSlotBase(TimeSlotBase timeSlotBase) {
        if(timeSlotBase == null) {
            throw new NullPointerException();
        }

        this.timeSlotBase = timeSlotBase;
    }

    public void setAcademicSkillsNeeded(List<AcademicSkill> academicSkillsNeeded) {
        if(academicSkillsNeeded == null) {
            throw new NullPointerException();
        }

        if(academicSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.academicSkillsNeeded = academicSkillsNeeded;
    }

    public void setBusinessSkillsNeeded(List<BusinessSkill> businessSkillsNeeded) {
        if(businessSkillsNeeded == null) {
            throw new NullPointerException();
        }

        if(businessSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.businessSkillsNeeded = businessSkillsNeeded;
    }

    public void setStatus(String status) throws AppointmentException {
        if(this.status.equals("Accepte") || this.status.equals("Refuse")) {
            throw new AppointmentException("Le status ne peut plus etre modifie.");
        }

        if(this.status.equals(status)) {
            throw new AppointmentException("Le status de ce rendez-vous est deja " + status);
        }

        this.status = status;
    }

    public void displayLocals() {

    }

    public String toString() {
        return "Rendez-vous ," +
                "Status : {"+status+"}," +
                ""

    }
}