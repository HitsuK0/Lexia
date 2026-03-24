package be.hers.info.ProjetIntegree.POJO;

public class AcademicSkill
{

    private String designation;


    /**
     * Create an AcademicSkill.
     * @param designation the name of the academic skill
     */
    public AcademicSkill(String designation){
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
}
