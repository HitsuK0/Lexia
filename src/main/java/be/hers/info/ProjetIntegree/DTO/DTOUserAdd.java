package be.hers.info.ProjetIntegree.DTO;

import java.util.List;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François
 */
public class DTOUserAdd {

    private String password;
    private String lastName;
    private String firstName;
    private String emailAddress;
    private String phoneNumber;
    private int postcode;
    private String postOfficeBox;
    private String locality;
    private String hamlet;
    private int hourQuota;
    private int educationLevel;
    private int weeklyWorkHours;
    private List<String> communicationLanguage;

    /**
     * Initialize a default DTOUserAdd
     */
    public DTOUserAdd() {
    }

    /**
     * Initialize a DTOUserAdd
     *
     * @param password              the password of the user
     * @param lastName              the lastName of the user
     * @param firstName             the firstName of the user
     * @param emailAddress          the email address of the user
     * @param phoneNumber           the phone number of the user
     * @param postcode              the postcode of the address
     * @param postOfficeBox         the post office box of the address
     * @param locality              the locality of the address
     * @param hamlet                the hamlet of the address
     * @param hourQuota             the quota hours of the beneficiary. Present if the user is a beneficiary
     * @param educationLevel        the level of education of the beneficiary. Present if the user is a beneficiary
     * @param weeklyWorkHours       the number of hours worked over the week.
     *                              Present if the user is an interpreter, a resa or a ccordinator
     * @param communicationLanguage the list of communication languages used by the beneficiary.
     *                              Present if the user is a beneficiary
     */
    public DTOUserAdd(String password, String lastName, String firstName, String emailAddress, String phoneNumber,
                      int postcode, String postOfficeBox, String locality, String hamlet, int hourQuota,
                      int educationLevel, int weeklyWorkHours, List<String> communicationLanguage) {
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.postcode = postcode;
        this.postOfficeBox = postOfficeBox;
        this.locality = locality;
        this.hamlet = hamlet;
        this.hourQuota = hourQuota;
        this.educationLevel = educationLevel;
        this.weeklyWorkHours = weeklyWorkHours;
        this.communicationLanguage = communicationLanguage;
    }

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name of the user
     *
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name of the user
     *
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the email address of the user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the email address of the user
     *
     * @param emailAddress The email address to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number of the user
     *
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the postcode of the address
     */
    public int getPostcode() {
        return postcode;
    }

    /**
     * Set the postcode of the address
     *
     * @param postcode the postcode of the address
     */
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the post office box of the address
     */
    public String getPostOfficeBox() {
        return postOfficeBox;
    }

    /**
     * Set the postOfficeBox of the address
     *
     * @param postOfficeBox the post office box of the address
     */
    public void setPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
    }

    /**
     * @return the locality of the address
     */
    public String getLocality() {
        return locality;
    }

    /**
     * Set the locality of the address
     *
     * @param locality the locality of the address
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }

    /**
     * @return the hamlet of the address
     */
    public String getHamlet() {
        return hamlet;
    }

    /**
     * Set the hamlet of the address
     *
     * @param hamlet the hamlet of the address
     */
    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

    /**
     * @return the number of quota hours of the beneficiary
     */
    public int getHourQuota() {
        return hourQuota;
    }

    /**
     * Set the hour quota of the beneficiary
     *
     * @param hourQuota the hour quota of the beneficiary
     */
    public void setHourQuota(int hourQuota) {
        this.hourQuota = hourQuota;
    }

    /**
     * @return the education level of the beneficiary
     */
    public int getEducationLevel() {
        return educationLevel;
    }

    /**
     * Set the level of education of the beneficiary
     *
     * @param educationLevel the level of education of the beneficiary
     */
    public void setEducationLevel(int educationLevel) {
        this.educationLevel = educationLevel;
    }

    /**
     * @return The number of hours worked over the week by an interpreter
     */
    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    /**
     * Set the number of hours worked this week by an interpreter
     *
     * @param weeklyWorkHours the number of hours worked this week by an interpreter
     */
    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

    /**
     * @return the communication languages of the beneficiary
     */
    public List<String> getCommunicationLanguage() {
        return communicationLanguage;
    }

    /**
     * Set the list of communication languages of the beneficiary
     *
     * @param communicationLanguage the list of communication languages of the beneficiary
     */
    public void setCommunicationLanguage(List<String> communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }
}
