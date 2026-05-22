package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.Referrer;

/**
 * @author Quentin Vanderheyden
 * @reviewer Nicolas Jean-Francois, Halet Louis
 */
public class DTOReferrer {

    private int numEstablishement;
    private String nameNewReferrer;
    private String surnameNewReferrer;
    private String phoneNumberReferrer;
    private String mailReferrer;

    /**
     * Initialize an empty DTOReferrer
     */
    public DTOReferrer() {

    }

    /**
     * Initialize a DTOReferrer with all the field.
     * @param name is the name of the referrer.
     * @param surname is the surname of the referrer.
     * @param phoneNumber is the phone number of the referrer.
     * @param mailReferrer is the mail address of the referrer.
     */
    public DTOReferrer(String name, String surname, String phoneNumber, String mailReferrer) {
        this.nameNewReferrer = name;
        this.surnameNewReferrer = surname;
        this.phoneNumberReferrer = phoneNumber;
        this.mailReferrer = mailReferrer;
    }

    /**
     * Initialise a DTOReferrer by using a param of type Referrer.
     * @param referrer is reference to create the this.
     */
    public DTOReferrer(Referrer referrer) {
        this(referrer.getName(), referrer.getSurname(), referrer.getPhoneNumber(), referrer.getAddressMail());
    }

    /**
     * Get the name of the Referrer
     * @return nameNewReffer.
     */
    public String getNameNewReferrer() {
        return nameNewReferrer;
    }

    /**
     * Get the surname of the Referrer.
     * @return surnameNewReffer.
     */
    public String getSurnameNewReferrer() {
        return surnameNewReferrer;
    }

    /**
     * Get the phone number of the Referrer.
     * @return phonenNumber.
     */
    public String getPhoneNumberReferrer() {
        return phoneNumberReferrer;
    }

    /**
     * Get the mail address of the Referrer.
     * @return mailReferrer
     */
    public String getMailReferrer() {
        return mailReferrer;
    }

    /**
     * Get the num of the Establishment the referrer work in.
     * @return numEstablishment.
     */
    public int getNumEstablishement() {return numEstablishement; }

    /**
     * Set the new numEstablishment with the given param.
     * @param numEstablishement is the new numEstablishment.
     */
    public void setNumEstablishement(int numEstablishement) {this.numEstablishement = numEstablishement;}

    /**
     * Set the name of the Referrer.
     * @param nameNewReferrer is the new name.
     */
    public void setNameNewReferrer(String nameNewReferrer) {
        this.nameNewReferrer = nameNewReferrer;
    }

    /**
     * Set the new surname of the referrer.
     * @param surnameNewReferrer is the new surname.
     */
    public void setSurnameNewReferrer(String surnameNewReferrer) {
        this.surnameNewReferrer = surnameNewReferrer;
    }

    /**
     * Set the new phone number of the referrer.
     * @param phoneNumberReferrer is the new phone number.
     */
    public void setPhoneNumberReferrer(String phoneNumberReferrer) {
        this.phoneNumberReferrer = phoneNumberReferrer;
    }

    /**
     * Set the new mail address of the referrer.
     * @param mailReferrer is the new mail address.
     */
    public void setMailReferrer(String mailReferrer) {
        this.mailReferrer = mailReferrer;
    }
}
