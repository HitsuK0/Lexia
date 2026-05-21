package be.hers.info.ProjetIntegree.DTO;

/**
 * DTO used to pass beneficiaries from the DB to the frontend
 *
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François, Halet Louis, Wellinger Chloé
 */

public class DTOBeneficiaryFormAppointment {
    private int numBeneficiary;
    private String lastName;
    private String firstName;

    /**
     * Initialize a beneficiary with numBeneficiary, login, lastName, firstName
     * @param numBeneficiary the id of the beneficiary
     * @param lastName the last name of the beneficiary
     * @param firstName the first name of the beneficiary
     */
    public DTOBeneficiaryFormAppointment(int numBeneficiary, String lastName, String firstName) {
        this.numBeneficiary = numBeneficiary;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * @return the id of the beneficiary
     */
    public int getNumBeneficiary() {
        return numBeneficiary;
    }

    /**
     * Initialize the id of the beneficiary
     * @param numBeneficiary the id of the beneficiary
     */
    public void setNumBeneficiary(int numBeneficiary) {
        this.numBeneficiary = numBeneficiary;
    }

    /**
     * @return the last name of the beneficiary
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Initialize the last name of the beneficiary
     * @param lastName the last name of the beneficiary
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the first name of the beneficiary
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Initialize the first name of the beneficiary
     * @param firstName the first name of the beneficiary
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
