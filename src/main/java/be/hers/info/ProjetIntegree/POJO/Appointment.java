package be.hers.info.ProjetIntegree.POJO;

import be.hers.info.ProjetIntegree.Exceptions.AppointmentException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private String status;
    private List<String> appointement_locals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlotPunctual time_slot_punctual;
    private TimeSlotBase time_slot_base;
    private List<AcademicSkill> academic_skills_needed;
    private List<BusinessSkill> business_skills_needed;

    public Appointment(Beneficiary beneficiary, List<String> appointement_locals, List<Interpreter> interpreters, List<AcademicSkill> academic_skills_needed, List<BusinessSkill> business_skills_needed,
                       TimeSlotPunctual time_slot_punctual, TimeSlotBase time_slot_base) {
        if(beneficiary == null || appointement_locals == null || interpreters == null || academic_skills_needed == null || business_skills_needed == null || time_slot_punctual == null || time_slot_base == null) {
            throw new NullPointerException();
        }

        if(appointement_locals.size() == 0 || interpreters.size() == 0 || academic_skills_needed.size() == 0 || business_skills_needed.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.beneficiary = beneficiary;
        this.status = "En attente";
        this.appointement_locals = appointement_locals;
        this.interpreters = interpreters;
        this.time_slot_punctual = time_slot_punctual;
        this.time_slot_base = time_slot_base;
        this.academic_skills_needed = academic_skills_needed;
        this.business_skills_needed = business_skills_needed;
    }

    public Appointment() {
        status = "En attente";
        appointement_locals = new ArrayList<String>();
        interpreters = null;
        time_slot_punctual = null;
        time_slot_base = null;
        academic_skills_needed = new ArrayList<AcademicSkill>();
        business_skills_needed = new ArrayList<BusinessSkill>();
    }

    public String getStatus() {
        return status;
    }

    public List<String> getAppointement_locals() {
        return appointement_locals;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public List<Interpreter> getInterpreters() {
        return interpreters;
    }

    public TimeSlotPunctual getTime_slot_punctual() {
        return time_slot_punctual;
    }

    public TimeSlotBase getTime_slot_base() {
        return time_slot_base;
    }

    public List<AcademicSkill> getAcademic_skills_needed() {
        return academic_skills_needed;
    }

    public List<BusinessSkill> getBusiness_skills_needed() {
        return business_skills_needed;
    }

    public void set_appointement_start_time(LocalTime sT) {
        if(sT == null) {
            throw new NullPointerException();
        }

        this.appointement_start_time = sT;
    }

    public void set_sppointement_end_time(LocalTime eT) {
        if(eT == null) {
            throw new NullPointerException();
        }

        this.appointement_end_time = eT;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAppointement_locals(List<String> appointement_locals) {
        if(appointement_locals == null) {
            throw new NullPointerException();
        }

        if(appointement_locals.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointement_locals = appointement_locals;
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

    public void setTime_slot_punctual(TimeSlotPunctual time_slot_punctual) {
        if(time_slot_punctual == null) {
            throw new NullPointerException();
        }

        this.time_slot_punctual = time_slot_punctual;
    }

    public void setTime_slot_base(TimeSlotBase time_slot_base) {
        if(time_slot_base == null) {
            throw new NullPointerException();
        }

        this.time_slot_base = time_slot_base;
    }

    public void setAcademic_skills_needed(List<AcademicSkill> academic_skills_needed) {
        if(academic_skills_needed == null) {
            throw new NullPointerException();
        }

        if(academic_skills_needed.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.academic_skills_needed = academic_skills_needed;
    }

    public void setBusiness_skills_needed(List<BusinessSkill> business_skills_needed) {
        if(business_skills_needed == null) {
            throw new NullPointerException();
        }

        if(business_skills_needed.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.business_skills_needed = business_skills_needed;
    }

    public void set_status(String status) throws AppointmentException {
        if(this.status.equals("Accepte") || this.status.equals("Refuse")) {
            throw new AppointmentException("Le status ne peut plus etre modifie.");
        }

        if(this.status.equals(status)) {
            throw new AppointmentException("Le status de ce rendez-vous est deja " + status);
        }

        this.status = status;
    }

    public String toString() {
        return "Appointment {}";
    }
}