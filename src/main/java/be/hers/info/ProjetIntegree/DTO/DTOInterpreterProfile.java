package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.util.List;

public class DTOInterpreterProfile {
    private int numInterpreter;
    private String login;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;

    private int weeklyWorkHours;
    private List<ProfessionalSkill> professionalSkillList;
    private List<AcademicSkill> academicSkillList;
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
     * Creates a fully initialised DTOBeneficiaryProfile.
     *
     * @param numInterpreter the id of the beneficiary (read-only, for identification)
     * @param login          the login of the beneficiary (read-only)
     * @param lastName       the last name of the beneficiary
     * @param firstName      the first name of the beneficiary
     * @param phoneNumber    the phone number of the beneficiary
     * @param emailAddress   the email address of the beneficiary
     * @param postOfficeBox  the street and number of the address
     * @param postcode       the postal code of the address
     * @param locality       the city of the address
     * @param hamlet         the hamlet of the address (optional)
     */
    public DTOInterpreterProfile(int numInterpreter, String login, String lastName, String firstName, String phoneNumber,
                                 String emailAddress,int weeklyWorkHours, List<ProfessionalSkill> professionalSkillList, List<AcademicSkill> academicSkillList, String postOfficeBox, int postcode, String locality, String hamlet) {
        this.numInterpreter = numInterpreter;
        this.login = login;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.weeklyWorkHours = weeklyWorkHours;
        this.professionalSkillList = professionalSkillList;
        this.academicSkillList = academicSkillList;
        this.postOfficeBox = postOfficeBox;
        this.postcode = postcode;
        this.locality = locality;
        this.hamlet = hamlet;
    }

    public int getNumInterpreter() {
        return numInterpreter;
    }

    public void setNumInterpreter(int numInterpreter) {
        this.numInterpreter = numInterpreter;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<ProfessionalSkill> getProfessionalSkillList() {
        return professionalSkillList;
    }

    public void setProfessionalSkillList(List<ProfessionalSkill> professionalSkillList) {
        this.professionalSkillList = professionalSkillList;
    }

    public List<AcademicSkill> getAcademicSkillList() {
        return academicSkillList;
    }

    public void setAcademicSkillList(List<AcademicSkill> academicSkillList) {
        this.academicSkillList = academicSkillList;
    }

    public String getPostOfficeBox() {
        return postOfficeBox;
    }

    public void setPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getHamlet() {
        return hamlet;
    }

    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }


}
