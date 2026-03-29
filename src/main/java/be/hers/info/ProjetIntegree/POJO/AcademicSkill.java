package be.hers.info.ProjetIntegree.POJO;

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
            throw new NullPointerException();
        }
        this.designation = designation;
    }

    /**
     *  Initialize an AcademicSkill designated by the name "default"
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
            throw new IllegalArgumentException("[POJOAcademicSkill] Valeur invalide : la désignation ne peut pas contenir null");
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
        return "Designation : "+ designation + "\n";
    }
}
