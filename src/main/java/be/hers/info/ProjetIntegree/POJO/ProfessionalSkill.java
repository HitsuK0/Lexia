package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wellinger Chloé
 * @reviewer Nicolas Jean-François, Halet Louis
 */

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
     * Default constructor
     */
    public ProfessionalSkill() {
        this.designation = "";
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Constructs a ProfessionalSkill with several attributs without ID
     * @param designation The skill of the professional skill
     */
    public ProfessionalSkill(String designation) {
        this.designation = designation;
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Constructs a ProfessionalSkill with several attributs and ID
     * @param numProfessionalSkill The ID of the professional skill
     * @param designation The skill of the professional skill
     * @throws IllegalArgumentException if numProfessionalSkill is negative
     */
    public ProfessionalSkill(int numProfessionalSkill, String designation) {
        if (numProfessionalSkill < 0)
            throw new IllegalArgumentException("[POJOProfessionalSkill] L'identifiant de la compétence métier ne peut pas être négatif");

        this.numProfessionalSkill = numProfessionalSkill;
        this.designation = designation;
        this.interpreters = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Constructs a ProfessionalSkill with all attributs and ID
     * @param numProfessionalSkill
     * @param designation
     * @param interpreters
     * @param appointments
     * @throws IllegalArgumentException if numProfessionalSkill is negative
     */
    public ProfessionalSkill(int numProfessionalSkill, String designation,
                             List<Interpreter> interpreters, List<Appointment> appointments) {
        if (numProfessionalSkill < 0)
            throw new IllegalArgumentException("[POJOProfessionalSkill] L'identifiant de la compétence métier ne peut pas être négatif");

        this.numProfessionalSkill = numProfessionalSkill;
        this.designation = designation;
        this.interpreters = interpreters;
        this.appointments = appointments;
    }

    /**
     * Constructs a ProfessionalSkill with all attributs without ID
     * @param designation
     * @param interpreters
     * @param appointments
     * @throws IllegalArgumentException if numProfessionalSkill is negative
     */
    public ProfessionalSkill(String designation,
                             List<Interpreter> interpreters, List<Appointment> appointments) {

        this.designation = designation;
        this.interpreters = interpreters;
        this.appointments = appointments;
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
     * Set the identifiant for the professional skill
     * @param numProfessionalSkill The new identifiant to set
     */
    public void setNumProfessionalSkill(int numProfessionalSkill){
        if (numProfessionalSkill < 0)
            throw new IllegalArgumentException("[POJOProfessionalSkill] L'identifiant de la compétence métier ne peut pas être négatif");

        this.numProfessionalSkill = numProfessionalSkill;
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
     * @return a String containing the professional skill ID, designation, list of interpreters, list of appointments
     */
    @Override
    public String toString() {
        return "Compétence Métier" +
                "\nNumero ID: " + numProfessionalSkill +
                "\nDésignation : " + designation +
                "\nListe des interprètes associés : " + interpreters +
                "\nListe des rendez-vous : " + appointments;
    }
}
