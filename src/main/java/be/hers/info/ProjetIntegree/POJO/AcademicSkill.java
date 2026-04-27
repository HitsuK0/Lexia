package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Leroy Rodriguez Ainhoa
 * @reviewer Nicolas Jean-François, Halet Louis
 */


public class AcademicSkill
{
    private int numAcademicSkill;
    private String designation;

    /**
     * Create an AcademicSkill.
     * @param designation the name of the academic skill
     * @throws NullPointerException if designation is null
     */
    public AcademicSkill(int num, String designation){

        if(designation == null){
            throw new IllegalArgumentException("[POJOAcademicSkill] la désignation ne peut pas être null ou vide");
        }
        this.designation = designation;
        this.numAcademicSkill = num;
    }

    /**
     *  Initialize an AcademicSkill designated by the name ""
     */
    public AcademicSkill(){
        this.designation = "";

    }

    /**
     * Set the numAcademicSkill field with num
     * @param num is the id to put in numAcademicSkill.
     */
    public void setNumAcademicSkill(int num){
        this.numAcademicSkill = num;
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
     * @return the designation of the AcademicSkill
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * @return the ID of the AcademicSkill
     */
    public int getNumAcademicSkill() { return numAcademicSkill; }

    /**
     * @return a String containing the designation
     */
    @Override
    public String toString(){
        return "Compétence académique"+
               "\nDesignation : "+ designation + "\n";
    }
}
