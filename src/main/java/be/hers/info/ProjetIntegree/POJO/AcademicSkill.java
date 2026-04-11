package be.hers.info.ProjetIntegree.POJO;
// ajout de id avec getter et setter (à supprimer avant merge)
// (supprimer les [QV] avant merge)
/**
 * @author Leroy Rodriguez Ainhoa
 * @reviewer Nicolas Jean-François, Halet Louis
 */


public class AcademicSkill
{

    private int numAcademicSkill; // [QV]
    private String designation;

    /**
     * Create an AcademicSkill.
     * @param designation the name of the academic skill
     * @throws NullPointerException if designation is null
     */
    public AcademicSkill(int id, String designation){

        if(designation == null){
            throw new IllegalArgumentException("[POJOAcademicSkill] la désignation ne peut pas être null ou vide");
        }
        this.designation = designation;
        this.numAcademicSkill = id;
    }

    /**
     *  Initialize an AcademicSkill designated by the name ""
     */
    public AcademicSkill(){
        this.designation = "";

    }

    /**
     * @param designation  the name of the AcademicSkill
     * @throws IllegalArgumentException if the designation is empty
     */
    public void setDesignation(String designation) {
        if(designation == null){
            throw new IllegalArgumentException("[POJOAcademicSkill] la désignation ne peut pas être null");
        }
        this.designation = designation;
    }

    /**
     * @return the id of the academic skill. [QV]
     */
    public int getId() {
        return numAcademicSkill;
    }

    /**
     * @return the designation of the AcademicSkill
     */
    public String getDesignation() {
        return designation;
    }

    /**
     *
     * @param id the id of the academic skill [QV]
     */
    public void setId(int id) {
        if(id < 0){
            throw new IllegalArgumentException("[POJOAcademicSkill] Le numéro de la compétence Academique ne peut pas être négatif");
        }
        this.numAcademicSkill = id;
    }

    /**
     * @return a String containing the designation
     */
    @Override
    public String toString(){
        return "Compétence académique"+
               "\nDesignation : "+ designation + "\n";
    }
}
