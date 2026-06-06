package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;

import java.util.List;

public class DTOInterpreterUsers {
    private int numInterpreter;
    private String login;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;
    private Address address;
    private int weeklyWorkHours;
    private List<ProfessionalSkill> professionalSkillsList;
    private List<AcademicSkill> academicSkillsList;

    /**
     * Initialize a DTOInterpreterUsers with num, login, lastName, firstName, phoneNumber, emailAddress, address,
     * quantity of working hours each week, all his professional skills and all his academic skills
     * @param numInterpreter the id of the DTOInterpreterUsers
     * @param login the login of the DTOInterpreterUsers
     * @param lastName the lastName of the DTOInterpreterUsers
     * @param firstName the firstName of the DTOInterpreterUsers
     * @param phoneNumber the phone number of the DTOInterpreterUsers
     * @param emailAddress the email address of the DTOInterpreterUsers
     * @param address the address of the DTOInterpreterUsers
     * @param weeklyWorkHours the quantity of working hours each week for the DTOInterpreterUsers
     * @param professionalSkillsList the professional skills of the DTOInterpreterUsers
     * @param academicSkillsList the academic skills of the DTOInterpreterUsers
     */
    public DTOInterpreterUsers(int numInterpreter, String login, String lastName, String firstName, String phoneNumber,
                               String emailAddress, Address address, int weeklyWorkHours,
                               List<ProfessionalSkill> professionalSkillsList, List<AcademicSkill> academicSkillsList) {
        this.numInterpreter = numInterpreter;
        this.login = login;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
        this.weeklyWorkHours = weeklyWorkHours;
        this.professionalSkillsList = professionalSkillsList;
        this.academicSkillsList = academicSkillsList;
    }

    /**
     * @return the id
     */
    public int getNumInterpreter() {
        return numInterpreter;
    }

    /**
     * Set the numInterpreter
     * @param numInterpreter the numInterpreter to set
     */
    public void setNumInterpreter(int numInterpreter) {
        this.numInterpreter = numInterpreter;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Set the login
     * @param login The login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the email address
     * @param emailAddress The email address to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the address
     * @param address The address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the quantity of working hours each week
     */
    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    /**
     * Set the quantity of working hours each week
     * @param weeklyWorkHours the quantity of working hours each week
     */
    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

    /**
     * @return the professional skills
     */
    public List<ProfessionalSkill> getProfessionalSkillsList() {
        return professionalSkillsList;
    }

    /**
     * Set the professional skills
     * @param professionalSkillsList the professional skills
     */
    public void setProfessionalSkillsList(List<ProfessionalSkill> professionalSkillsList) {
        this.professionalSkillsList = professionalSkillsList;
    }

    /**
     * @return the academic skills
     */
    public List<AcademicSkill> getAcademicSkillsList() {
        return academicSkillsList;
    }

    /**
     * Set the academic skills
     * @param academicSkillsList the academic skills
     */
    public void setAcademicSkillsList(List<AcademicSkill> academicSkillsList) {
        this.academicSkillsList = academicSkillsList;
    }
}
