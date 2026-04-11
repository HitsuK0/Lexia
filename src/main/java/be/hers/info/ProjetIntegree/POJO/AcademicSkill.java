package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Leroy Rodriguez Ainhoa
 * @reviewer Nicolas Jean-François, Halet Louis
 */


public class AcademicSkill
{

    private String designation;

    /**
     * Create an AcademicSkill.
     * @param designation the name of the academic skill
     * @throws NullPointerException if designation is null
     */
    public AcademicSkill(String designation){

        if(designation == null){
            throw new IllegalArgumentException("[POJOAcademicSkill] la désignation ne peut pas être null ou vide");
        }
        this.designation = designation;
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
     * @return the designation of the AcademicSkill
     */
    public String getDesignation() {
        return designation;
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
