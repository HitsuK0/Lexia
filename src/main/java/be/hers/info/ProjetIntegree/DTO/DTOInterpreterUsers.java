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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

    public List<ProfessionalSkill> getProfessionalSkillsList() {
        return professionalSkillsList;
    }

    public void setProfessionalSkillsList(List<ProfessionalSkill> professionalSkillsList) {
        this.professionalSkillsList = professionalSkillsList;
    }

    public List<AcademicSkill> getAcademicSkillsList() {
        return academicSkillsList;
    }

    public void setAcademicSkillsList(List<AcademicSkill> academicSkillsList) {
        this.academicSkillsList = academicSkillsList;
    }
}
