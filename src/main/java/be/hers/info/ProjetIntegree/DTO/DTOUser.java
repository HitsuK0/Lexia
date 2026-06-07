package be.hers.info.ProjetIntegree.DTO;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François, Halet Louis
 */

public class DTOUser {
    private int numUser;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;

    /**
     * Initialize a DTOUser with num, lastName, firstName, phoneNumber, emailAddress
     * @param numUser the id of the DTOUser
     * @param lastName the lastName of the DTOUser
     * @param firstName the firstName of the DTOUser
     * @param phoneNumber the phone number of the DTOUser
     * @param emailAddress the email address of the DTOUser
     */
    public DTOUser(int numUser, String lastName, String firstName, String phoneNumber, String emailAddress) {
        this.numUser = numUser;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * @return the id
     */
    public int getNumUser() {
        return numUser;
    }

    /**
     * Set the numUser
     * @param numUser the numUser to set
     */
    public void setNumUser(int numUser) {
        this.numUser = numUser;
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
}
