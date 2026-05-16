package be.hers.info.ProjetIntegree.DTO;

/**
 * DTO used to carry the beneficiary's editable profile fields between the HTML form and the controller.
 * The login and numBeneficiary are read-only and carried for identification only.
 * The password is NOT included here — use DTOPasswordChange for that.
 *
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
public class DTOBeneficiaryProfile {

    private int numBeneficiary;
    private String login;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;

    // Address fields flattened for form binding
    private String postOfficeBox;
    private int postcode;
    private String locality;
    private String hamlet;

    /**
     * Creates an empty DTOBeneficiaryProfile.
     */
    public DTOBeneficiaryProfile() {
    }

    /**
     * Creates a fully initialised DTOBeneficiaryProfile.
     *
     * @param numBeneficiary the id of the beneficiary (read-only, for identification)
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
    public DTOBeneficiaryProfile(int numBeneficiary, String login, String lastName, String firstName, String phoneNumber,
                                 String emailAddress, String postOfficeBox, int postcode, String locality, String hamlet) {
        this.numBeneficiary = numBeneficiary;
        this.login = login;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.postOfficeBox = postOfficeBox;
        this.postcode = postcode;
        this.locality = locality;
        this.hamlet = hamlet;
    }

    /** @return the id of the beneficiary */
    public int getNumBeneficiary() { return numBeneficiary; }

    /** @param numBeneficiary the id of the beneficiary */
    public void setNumBeneficiary(int numBeneficiary) { this.numBeneficiary = numBeneficiary; }

    /** @return the login of the beneficiary */
    public String getLogin() { return login; }

    /** @param login the login of the beneficiary */
    public void setLogin(String login) { this.login = login; }

    /** @return the last name of the beneficiary */
    public String getLastName() { return lastName; }

    /** @param lastName the last name of the beneficiary */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return the first name of the beneficiary */
    public String getFirstName() { return firstName; }

    /** @param firstName the first name of the beneficiary */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return the phone number of the beneficiary */
    public String getPhoneNumber() { return phoneNumber; }

    /** @param phoneNumber the phone number of the beneficiary */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /** @return the email address of the beneficiary */
    public String getEmailAddress() { return emailAddress; }

    /** @param emailAddress the email address of the beneficiary */
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    /** @return the street and number of the address */
    public String getPostOfficeBox() { return postOfficeBox; }

    /** @param postOfficeBox the street and number of the address */
    public void setPostOfficeBox(String postOfficeBox) { this.postOfficeBox = postOfficeBox; }

    /** @return the postal code of the address */
    public int getPostcode() { return postcode; }

    /** @param postcode the postal code of the address */
    public void setPostcode(int postcode) { this.postcode = postcode; }

    /** @return the city of the address */
    public String getLocality() { return locality; }

    /** @param locality the city of the address */
    public void setLocality(String locality) { this.locality = locality; }

    /** @return the hamlet of the address */
    public String getHamlet() { return hamlet; }

    /** @param hamlet the hamlet of the address */
    public void setHamlet(String hamlet) { this.hamlet = hamlet; }
}