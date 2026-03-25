package be.hers.info.ProjetIntegree.POJO;

public class AcademicSkill
{

    private String designation;


    /**
     * Create an AcademicSkill.
     * @param designation the name of the academic skill
     * @throws IllegalArgumentException if educationLevel or addresses is empty
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
        this.designation = "default";

    }

    /**
     * @param designation  the name of the AcademicSkill
     */
    public void setDesignation(String designation) {
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
    public String toString(){
        return "Designation : "+ designation + "\n";
    }
}
