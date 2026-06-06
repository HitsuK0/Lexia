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

    /**
     * Initialize a DTOBeneficiaryUsers with num, login, lastName, firstName, phoneNumber and emailAddress
     * @param numBeneficiary the id of the DTOBeneficiaryUsers
     * @param login the login of the DTOBeneficiaryUsers
     * @param lastName the lastName of the DTOBeneficiaryUsers
     * @param firstName the firstName of the DTOBeneficiaryUsers
     * @param phoneNumber the phone number of the DTOBeneficiaryUsers
     * @param emailAddress the email address of the DTOBeneficiaryUsers
     * @param address the address of the DTOBeneficiaryUsers
     * @param hourQuota the quota hours of the DTOBeneficiaryUsers
     * @param educationLevel the level of education of the DTOBeneficiaryUsers
     * @param interpreter the interpreter of the DTOBeneficiaryUsers
     * @param communicationLanguage the list of communication languages used by the DTOBeneficiaryUsers
     */
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

    /**
     * @return the id
     */
    public int getNumBeneficiary() {
        return numBeneficiary;
    }

    /**
     * Set the numBeneficiary
     * @param numBeneficiary the numBeneficiary to set
     */
    public void setNumBeneficiary(int numBeneficiary) {
        this.numBeneficiary = numBeneficiary;
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
     * @return the number of quota hours
     */
    public int getHourQuota() {
        return hourQuota;
    }

    /**
     * Set the hour quota
     * @param hourQuota the hour quota to set
     */
    public void setHourQuota(int hourQuota) {
        this.hourQuota = hourQuota;
    }

    /**
     * @return the education level
     */
    public int getEducationLevel() {
        return educationLevel;
    }

    /**
     * Set the level of education
     * @param educationLevel the level of education to set
     */
    public void setEducationLevel(int educationLevel) {
        this.educationLevel = educationLevel;
    }

    /**
     * @return the interpreter
     */
    public Interpreter getInterpreter() {
        return interpreter;
    }

    /**
     * Set the interpreter
     * @param interpreter the interpreter of reference
     */
    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * @return the communication languages
     */
    public List<String> getCommunicationLanguage() {
        return communicationLanguage;
    }

    /**
     * Set the list of communication languages
     * @param communicationLanguage the list of communication languages to set
     */
    public void setCommunicationLanguage(List<String> communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }
}
