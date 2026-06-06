package be.hers.info.ProjetIntegree.DTO;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François, Halet Louis
 */

import be.hers.info.ProjetIntegree.POJO.Address;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.util.List;

public class DTOBeneficiaryUsers {
    private int numBeneficiary;
    private String login;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;
    private Address address;
    private int hourQuota;
    private int educationLevel;
    private Interpreter interpreter;
    private List<String> communicationLanguage;

    public DTOBeneficiaryUsers(int numBeneficiary, String login, String lastName, String firstName, String phoneNumber,
                               String emailAddress, Address address, int hourQuota, int educationLevel,
                               Interpreter interpreter, List<String> communicationLanguage) {
        this.numBeneficiary = numBeneficiary;
        this.login = login;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
        this.hourQuota = hourQuota;
        this.educationLevel = educationLevel;
        this.interpreter = interpreter;
        this.communicationLanguage = communicationLanguage;
    }

    public int getNumBeneficiary() {
        return numBeneficiary;
    }

    public void setNumBeneficiary(int numBeneficiary) {
        this.numBeneficiary = numBeneficiary;
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

    public int getHourQuota() {
        return hourQuota;
    }

    public void setHourQuota(int hourQuota) {
        this.hourQuota = hourQuota;
    }

    public int getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(int educationLevel) {
        this.educationLevel = educationLevel;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public List<String> getCommunicationLanguage() {
        return communicationLanguage;
    }

    public void setCommunicationLanguage(List<String> communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }
}
