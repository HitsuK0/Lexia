package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wellinger Chloé
 * @reviewer Nicolas Jean-François, Halet Louis
 */

/**
 * Represents a professional skill
 */

public class ProfessionalSkill {
    private int numProfessionalSkill;
    private String designation;

    /**
     * Default constructor
     */
    public ProfessionalSkill() {
        this.designation = "";
    }

    /**
     * Constructs a ProfessionalSkill with several attributs without ID
     * @param designation The skill of the professional skill
     */
    public ProfessionalSkill(String designation) {
        this.designation = designation;
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
     * @return a String containing the professional skill ID, designation
     */
    @Override
    public String toString() {
        return "Compétence Métier" +
                "\nNumero ID: " + numProfessionalSkill +
                "\nDésignation : " + designation;
    }

    public boolean equals(ProfessionalSkill obj) {
        if(obj == null) return false;
        else if (this == obj) return true;
        else return this.numProfessionalSkill == obj.numProfessionalSkill;
    }
}
