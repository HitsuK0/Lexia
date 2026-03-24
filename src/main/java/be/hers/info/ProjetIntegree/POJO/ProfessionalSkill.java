package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a professional skill that can be associated
 * with a list of interpreters and a list of appoitments
 */

public class ProfessionalSkill {
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
    public String get_Designation() {
        return designation;
    }

    /**
     * Set the skill's designation
     * @param designation The new designation to set
     */
    public void set_Designation(String designation) {
        this.designation = designation;
    }

    /**
     * @return Returns ths list of interpreters associated with this skill
     */
    public List<Interpreter> get_List_Interpreters() {
        return this.interpreters;
    }

    /**
     * Replaces ths entire list of interpreters
     * @param interpreters The new list of Interpreters
     */
    public void set_List_Interpreters(List<Interpreter> interpreters) {
        this.interpreters = interpreters;
    }

    /**
     * @return Returns the list of appointments associated with this skill.
     */
    public List<Appointment> get_List_Appointment(){
        return this.appointments;
    }

    /**
     * Replaces the entire appointments list
     * @param appointments The new list of Appointments
     */
    public void set_Appointment(List<Appointment> appointments){
        this.appointments = appointments;
    }

    /**
     * @return a string representation of this ProfessionalSkill.
     * Ex : Professionnal Skill{designation : 'translation'}
     */
    public String toString() {
        return "Professional Skill{designation :'" + designation + "'}";
    }
}
