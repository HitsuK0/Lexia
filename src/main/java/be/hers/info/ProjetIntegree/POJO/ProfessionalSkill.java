package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a professional skill that can be associated
 * with a list of interpreters and a list of appoitments
 */

public class ProfessionalSkill {
    private int numProfessionalSkill;
    private String designation;
    private List<Interpreter> interpreters;
    private List<Appointment> appointments;

    /**
     * Constructs a ProfessionalSkill with the given designation
     * @param designation The skill of the professional skill
     */
    public ProfessionalSkill(String designation) {
        this.designation = designation;
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Constructs a ProfessionalSkill with the given designation and ID numProfessionalSkill
     * @param designation The skill of the professional skill
     * @param numProfessionalSkill The ID of the professional skill
     */
    public ProfessionalSkill(String designation, int numProfessionalSkill) {
        this.designation = designation;
        this.numProfessionalSkill = numProfessionalSkill;
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Default constructor, sets designation to "default"
     * and initalizes both lists as empty ArrayList.
     */
    public ProfessionalSkill() {
        this.designation = "default";
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Returns the designation of this professional skill.
     * @return the designation string
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Set the skill's designation
     * @param designation The new designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * @return The ID of this professional skill
     */
    public int getNumProfessionalSkill(){
        return numProfessionalSkill;
    }

    /**
     * @return Returns ths list of interpreters associated with this skill
     */
    public List<Interpreter> getListInterpreters() {
        return this.interpreters;
    }

    /**
     * Replaces ths entire list of interpreters
     * @param interpreters The new list of Interpreters
     */
    public void setListInterpreters(List<Interpreter> interpreters) {
        this.interpreters = interpreters;
    }

    /**
     * @return Returns the list of appointments associated with this skill.
     */
    public List<Appointment> getListAppointment(){
        return this.appointments;
    }

    /**
     * Replaces the entire appointments list
     * @param appointments The new list of Appointments
     */
    public void setAppointment(List<Appointment> appointments){
        this.appointments = appointments;
    }

    /**
     * @return a string representation of this ProfessionalSkill.
     * Ex : Professionnal Skill{designation : 'translation'}
     */
    public String toString() {
        return "Professional Skill{designation :'" + designation + "', ID : '" + numProfessionalSkill + "'}";
    }
}
