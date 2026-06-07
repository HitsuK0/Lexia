package be.hers.info.ProjetIntegree.DTO;

import java.util.List;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getPostOfficeBox() {
        return postOfficeBox;
    }

    public void setPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
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

    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    public void setWeeklyWorkHours(int weeklyWorkHours) {
        this.weeklyWorkHours = weeklyWorkHours;
    }

    public List<String> getCommunicationLanguage() {
        return communicationLanguage;
    }

    public void setCommunicationLanguage(List<String> communicationLanguage) {
        this.communicationLanguage = communicationLanguage;
    }
}
