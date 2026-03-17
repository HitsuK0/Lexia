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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    
}
