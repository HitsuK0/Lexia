package be.hers.info.ProjetIntegree.POJO;

import be.hers.info.ProjetIntegree.Exceptions.AppointmentException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int appointment_ID;
    private String status;
    private List<String> appointement_locals; // TODO : supression ?
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlotPunctual time_slot_punctual;
    private TimeSlotBase time_slot_base;
    private List<AcademicSkill> academic_skills_needed;
    private List<BusinessSkill> business_skills_needed;

    public Appointment() {}

    public Appointment(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
        this.status = "En attente";
        this.appointement_locals = new ArrayList<String>();
        this.interpreters = new ArrayList<Interpreter>();
        this.academic_skills_needed = new ArrayList<AcademicSkill>();
        this.business_skills_needed = new ArrayList<BusinessSkill>();
    }

    public int get_appointment_ID() {
        return appointment_ID;
    }

    public String get_status() {
        return status;
    }

    public List<String> get_appointement_locals() {
        return appointement_locals;
    }

    public Beneficiary get_beneficiary() {
        return beneficiary;
    }

    public List<Interpreter> get_interpreters() { return interpreters;}

    public TimeSlotPunctual get_time_slot_punctual() { return time_slot_punctual;}

    public TimeSlotBase get_time_slot_base() { return time_slot_base; }

    public void set_status(String status) throws AppointmentException {
        if(this.status.equals("Accepte") || this.status.equals("Refuse")) {
            throw new AppointmentException("Le status ne peut plus etre modifie.");
        }

        if(this.status.equals(status)) {
            throw new AppointmentException("Le status de ce rendez-vous est deja " + status);
        }

        this.status = status;
    }

    public void set_appointement_start_time(LocalTime sT) {this.appointement_start_time = sT;}

    public void set_sppointement_end_time(LocalTime eT) {
        this.appointement_end_time = eT;
    }

    public String toString() {
        return "Appointment {}";
    }
}