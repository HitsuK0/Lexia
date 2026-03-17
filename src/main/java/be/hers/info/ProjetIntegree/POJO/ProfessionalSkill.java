package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class ProfessionalSkill {
    private String designation;
    private List<Interpreter> interpreters;
    private List<Appointment> appointments;

    public ProfessionalSkill(String designation) {
        this.designation = designation;
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public ProfessionalSkill() {
        this.designation = "default";
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Getters & Setters

    public String get_Designation() {
        return designation;
    }

    public void set_Designation(String designation) {
        this.designation = designation;
    }

    public List<Interpreter> get_List_Interpreters() {
        return this.interpreters;
    }

    public void set_List_Interpreters(List<Interpreter> interpreters) {
        this.interpreters = interpreters;
    }

    public List<Appointment> get_List_Appointment(){
        return this.appointments;
    }

    public void set_Appointment(List<Appointment> appointments){
        this.appointments = appointments;
    }

    // Affichage

    public String toString() {
        return "Professional Skill{designation :'" + designation + "'}";
    }
}
