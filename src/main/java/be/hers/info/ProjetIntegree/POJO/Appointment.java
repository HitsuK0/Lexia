package be.hers.info.ProjetIntegree.POJO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Appointment {
    private int appointment_ID;
    private LocalTime appointement_start_time;
    private LocalTime appointement_end_time;
    private AppointmentStatus status;
    private List<String> appointement_locals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlotPunctual time_slot_punctual;
    private List<academicSkill> academic_skills_needed;
    private List<businessSkill> business_skills_needed;

    // TODO : Replace all exceptions by personalised ones


    public Appointment() {}

    public Appointment(Beneficiary beneficiary, LocalTime appointementStartTime, LocalTime appointementEndTime) {
        this.beneficiary = beneficiary;
        this.appointement_start_time = appointementStartTime;
        this.appointement_end_time = appointementEndTime;
        this.status = AppointmentStatus.EN_ATTENTE;
        this.appointement_locals = new ArrayList<String>();
        this.interpreters = new ArrayList<Interpreter>();
        this.academic_skills_needed = new ArrayList<academicSkill>();
        this.business_skills_needed = new ArrayList<businessSkill>();
    }

    public int getAppointmentID() {
        return appointment_ID;
    }

    public LocalTime getAppointementStartTime() {
        return appointement_start_time;
    }

    public LocalTime getAppointementEndTime() {
        return appointement_end_time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public List<String> getAppointementLocals() {
        return appointement_locals;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public List<Interpreter> getInterpreters() {
        return interpreters;
    }

    public TimeSlotPunctual getTimeSlotPunctual() {
        return time_slot_punctual;
    }

    public void setStatus(AppointmentStatus status) throws IllegalArgumentException { // TODO : add throws for unwanted status setter, only {Accepte, Refuse, En attente}
        if(this.status == AppointmentStatus.ACCEPTE || this.status == AppointmentStatus.REFUSE) {
            throw new IllegalArgumentException("Le status ne peut plus etre modifie.");
        }

        if(this.status == status) {
            throw new IllegalArgumentException("Le status de ce rendez-vous est deja " + status); // TODO : att toString to status for lower case?
        }

        this.status = status; // TODO : PAS COMPLET
    }

    public void addLocal(String l) throws IllegalArgumentException {
        if(appointement_locals.contains(l)) {
            throw new IllegalArgumentException("Ce local a deja ete ajoute a ce rendez-vous.");
        }

        appointement_locals.add(l);
    }

    public void removeLocal(String l) throws IllegalArgumentException {
        if(!appointement_locals.contains(l)) {
            throw new IllegalArgumentException("Ce local ne fait pas partie de ce rendez-vous.");
        }

        appointement_locals.remove(l);
    }

    public void addInterpreter(Interpreter i) {
        // TODO : i need Interpreter class to do this
    }

    public void removeInterpreter(Interpreter i) {
        // TODO : same here
    }

    public void addAcademicSkills(academicSkill a) {
        // TODO : i need academicSkills class to do this
    }

    public void removeAcademicSkill(academicSkill a) {
        // TODO : i need academicSkills class to do this
    }

    public void addBusinessSkill(businessSkill b) {
        // TODO : i need academicSkills class to do this
    }

    public void removeBusinessSkill(businessSkill b) {
        // TODO : i need academicSkills class to do this
    }


    // TODO : maybe incomplete ?
    public void setAppointementStartTime(LocalTime sT) {
        this.appointement_start_time = sT;
    }

    public void setAppointementEndTime(LocalTime eT) {
        this.appointement_end_time = eT;
    }
}