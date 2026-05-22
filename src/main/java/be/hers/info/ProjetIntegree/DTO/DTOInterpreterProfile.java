package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.util.List;
/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois, Wellinger Chloé
 */
public class DTOInterpreterProfile {
    private int numInterpreter;
    private String login;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;
    private int weeklyWorkHours;

    private List<ProfessionalSkill> professionalSkillListInterpreter;
    private List<AcademicSkill> academicSkillListInterpreter;

    private List<ProfessionalSkill> professionalSkillList;
    private int numProfessionalSkillSelected;
    private List<AcademicSkill> academicSkillList;
    private int numAcademicSkillSelected;

    // Address fields flattened for form binding
    private String postOfficeBox;
    private int postcode;
    private String locality;
    private String hamlet;

    /**
     * Creates an empty DTOBeneficiaryProfile.
     */
    public DTOInterpreterProfile() {
    }

    /**
     * Creates a fully initialised DTOInterpreterProfile.
     * @param numInterpreter the id of the interpreter (read-only, for identification)
     * @param login          the login of the interpreter (read-only)
     * @param lastName       the last name of the interpreter
     * @param firstName      the first name of the interpreter
     * @param phoneNumber    the phone number of the interpreter
     * @param emailAddress   the email address of the interpreter
     * @param weeklyWorkHours the weeklyWorkHours of the interpreter
     * @param professionalSkillList the complete list with all professionalSkill
     * @param academicSkillList the complete list with all academicSkill
     * @param professionalSkillListInterpreter the list of professionalSkill of the interpreter
     * @param academicSkillListInterpreter the list of academicSkill of the interpreter
     * @param numProfessionalSkillSelected the numProfessionalSkill selected in drop-down list
     * @param numAcademicSkillSelected the numAcademicSkill selected in drop-down list
     * @param postOfficeBox  the street and number of the address
     * @param postcode       the postal code of the address
     * @param locality       the city of the address
     * @param hamlet         the hamlet of the address (optional)
     */
    public DTOInterpreterProfile(int numInterpreter, String login, String lastName, String firstName, String phoneNumber,
                                 String emailAddress,int weeklyWorkHours, List<ProfessionalSkill> professionalSkillList,
                                 List<AcademicSkill> academicSkillList, List<ProfessionalSkill> professionalSkillListInterpreter, List<AcademicSkill> academicSkillListInterpreter,
                                 int numProfessionalSkillSelected,int numAcademicSkillSelected,  String postOfficeBox, int postcode, String locality, String hamlet) {
        this.numInterpreter = numInterpreter;
        this.login = login;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.weeklyWorkHours = weeklyWorkHours;
        this.professionalSkillListInterpreter = professionalSkillListInterpreter;
        this.academicSkillListInterpreter = academicSkillListInterpreter;
        this.professionalSkillList = professionalSkillList;
        this.numProfessionalSkillSelected = numProfessionalSkillSelected;
        this.academicSkillList = academicSkillList;
        this.numAcademicSkillSelected = numAcademicSkillSelected;
        this.postOfficeBox = postOfficeBox;
        this.postcode = postcode;
        this.locality = locality;
        this.hamlet = hamlet;
    }
    /**
     * Returns the id of the interpreter.
     * @return the id of the interpreter
     */
    public int getNumInterpreter() {
        return numInterpreter;
    }
    /**
     * Sets the id of the interpreter.
     * @param numInterpreter the id of the interpreter
     */
    public void setNumInterpreter(int numInterpreter) {
        this.numInterpreter = numInterpreter;
    }
    /**
     * Returns the login of the interpreter.
     * @return the login of the interpreter
     */
    public String getLogin() {
        return login;
    }
    /**
     * Sets the login of the interpreter.
     * @param login the login of the interpreter
     */
    public void setLogin(String login) {
        this.login = login;
    }
    /**
     * Returns the last name of the interpreter.
     * @return the last name of the interpreter
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Sets the last name of the interpreter.
     * @param lastName the last name of the interpreter
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Returns the first name of the interpreter.
     * @return the first name of the interpreter
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Sets the first name of the interpreter.
     * @param firstName the first name of the interpreter
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Returns the phone number of the interpreter.
     * @return the phone number of the interpreter
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Sets the phone number of the interpreter.
     * @param phoneNumber the phone number of the interpreter
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * Returns the email address of the interpreter.
     * @return the email address of the interpreter
     */
    public String getEmailAddress() {
        return emailAddress;
    }
    /**
     * Sets the email address of the interpreter.
     * @param emailAddress the email address of the interpreter
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    /**
     * Returns the complete list of professional skills.
     * @return the complete list of professional skills
     */
    public List<ProfessionalSkill> getProfessionalSkillList() {
        return professionalSkillList;
    }
    /**
     * Sets the complete list of professional skills.
     * @param professionalSkillList the complete list of professional skills
     */
    public void setProfessionalSkillList(List<ProfessionalSkill> professionalSkillList) {
        this.professionalSkillList = professionalSkillList;
    }
    /**
     * Returns the complete list of academic skills.
     * @return the complete list of academic skills
     */
    public List<AcademicSkill> getAcademicSkillList() {
        return academicSkillList;
    }
    /**
     * Sets the complete list of academic skills.
     * @param academicSkillList the complete list of academic skills
     */
    public void setAcademicSkillList(List<AcademicSkill> academicSkillList) {
        this.academicSkillList = academicSkillList;
    }
    /**
     * Returns the post office box of the address.
     * @return the post office box of the address
     */
    public String getPostOfficeBox() {
        return postOfficeBox;
    }
    /**
     * Sets the post office box of the address.
     * @param postOfficeBox the post office box of the address
     */
    public void setPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
    }
    /**
     * Returns the postcode of the address.
     * @return the postcode of the address
     */
    public int getPostcode() {
        return postcode;
    }
    /**
     * Sets the postcode of the address.
     * @param postcode the postcode of the address
     */
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }
    /**
     * Returns the locality of the address.
     * @return the locality of the address
     */
    public String getLocality() {
        return locality;
    }
    /**
     * Sets the locality of the address.
     * @param locality the locality of the address
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }
    /**
     * Returns the hamlet of the address.
     * @return the hamlet of the address
     */
    public String getHamlet() {
        return hamlet;
    }
    /**
     * Sets the hamlet of the address.
     * @param hamlet the hamlet of the address
     */
    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }
    /**
     * Returns the weekly work hours of the interpreter.
     * @return the weekly work hours of the interpreter
     */
    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }
    /**
     * Sets the weekly work hours of the interpreter.
     * @param weeklyWorkHours the weekly work hours of the interpreter
     */
    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }
    /**
     * Returns the professional skills of the interpreter.
     * @return the professional skills of the interpreter
     */
    public List<ProfessionalSkill> getProfessionalSkillListInterpreter() {
        return professionalSkillListInterpreter;
    }
    /**
     * Sets the professional skills of the interpreter.
     * @param professionalSkillListInterpreter the professional skills of the interpreter
     */
    public void setProfessionalSkillListInterpreter(List<ProfessionalSkill> professionalSkillListInterpreter) {
        this.professionalSkillListInterpreter = professionalSkillListInterpreter;
    }
    /**
     * Returns the academic skills of the interpreter.
     * @return the academic skills of the interpreter
     */
    public List<AcademicSkill> getAcademicSkillListInterpreter() {
        return academicSkillListInterpreter;
    }
    /**
     * Sets the academic skills of the interpreter.
     * @param academicSkillListInterpreter the academic skills of the interpreter
     */
    public void setAcademicSkillListInterpreter(List<AcademicSkill> academicSkillListInterpreter) {
        this.academicSkillListInterpreter = academicSkillListInterpreter;
    }
    /**
     * Returns the selected professional skill id.
     * @return the selected professional skill id
     */
    public int getNumProfessionalSkillSelected() {
        return numProfessionalSkillSelected;
    }
    /**
     * Sets the selected professional skill id.
     * @param numProfessionalSkillSelected the selected professional skill id
     */
    public void setNumProfessionalSkillSelected(int numProfessionalSkillSelected) {
        this.numProfessionalSkillSelected = numProfessionalSkillSelected;
    }
    /**
     * Returns the selected academic skill id.
     * @return the selected academic skill id
     */
    public int getNumAcademicSkillSelected() {
        return numAcademicSkillSelected;
    }
    /**
     * Sets the selected academic skill id.
     * @param numAcademicSkillSelected the selected academic skill id
     */
    public void setNumAcademicSkillSelected(int numAcademicSkillSelected) {
        this.numAcademicSkillSelected = numAcademicSkillSelected;
    }

    /**
     *
     * Search for the professional skill where the ID parameter matches a numProfessionalSkill in the professionalSkillList
     * @param id the id of ProfessionalSkill
     * @return The professional skill found in the list, null if not found
     */
    public ProfessionalSkill findProfessionalSkillById(int id) {
        ProfessionalSkill psFind = null;
        if (id > 0){
            for (ProfessionalSkill ps : professionalSkillList) {
                if (ps != null && id == ps.getNumProfessionalSkill()) {
                    psFind = ps;
                    break;
                }
            }
        }
        return psFind;
    }
    /**
     * Search for the AcademicSkill where the ID parameter matches a numAcademicSkill in the academicSkillList
     * @param id the id of AcademicSkill
     * @return The AcademicSkill found in the list, null if not found
     */
    public AcademicSkill findAcademicSkillById(int id) {
        AcademicSkill asFind = null;
        if (id > 0){
            for (AcademicSkill as : academicSkillList) {
                if (as != null && id == as.getNumAcademicSkill()) {
                    asFind = as;
                    break;
                }
            }
        }
        return asFind;
    }

}
