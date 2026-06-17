package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Leroy Rodriguez Ainhoa
 * @reviewer Nicolas Jean-François, Halet Louis
 */

public class Referrer {

    private String name;
    private String surname;
    private String phoneNumber;
    private String addressMail;
    private int numReferrer;
    private Establishment refEstablishment;


    /**
     * Initialize a Referrer with refEstablishment, addressMail, phoneNumber, surname and name
     *
     * @param refEstablishment the establishment where the referrer works
     * @param addressMail      the mail of the referrer
     * @param phoneNumber      the phone number
     * @param surname          the surname of the referrer
     * @param name             the name of the referrer
     * @throws IllegalArgumentException if addressMail, phoneNumber, surname or name is null, empty or only with spaces
     */
    public Referrer(Establishment refEstablishment, String addressMail, String phoneNumber, String surname, String name) {
        if (addressMail == null || addressMail.isBlank() || phoneNumber == null || phoneNumber.isBlank() ||
            surname == null || surname.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.refEstablishment = refEstablishment;
        this.addressMail = addressMail;
        this.phoneNumber = phoneNumber;
        this.surname = surname;
        this.name = name;
    }

    /**
     * Initialize a Referrer with no elements
     */
    public Referrer() {

        this.refEstablishment = new Establishment();
        this.addressMail = "";
        this.phoneNumber = "";
        this.surname = "";
        this.name = "";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the address mail
     */
    public String getAddressMail() {
        return addressMail;
    }

    /**
     * @return the establishment
     */
    public Establishment getRefEstablishment() {
        return refEstablishment;
    }

    /**
     * @return the id of the referrer
     */
    public int getNumReferrer() {
        return numReferrer;
    }


    /**
     * @param name the name
     * @throws IllegalArgumentException if name is null, empty or only with spaces
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[POJOReferrer] Le prénom ne peut pas être null, vide ou ne contenir que des espaces");
        }
        this.name = name;
    }

    /**
     * @param surname the surname
     * @throws IllegalArgumentException if surname is null, empty or only with spaces
     */
    public void setSurname(String surname) {
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("[POJOReferrer] Le nom ne peut pas être null, vide ou ne contenir que des espaces");
        }
        this.surname = surname;
    }

    /**
     * @param phoneNumber the phone number
     * @throws IllegalArgumentException if phoneNumber is null, empty or only with spaces
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("[POJOReferrer] Le numéro de téléphone ne peut pas être null, vide ou ne contenir que des espaces");
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     * @param addressMail the mail address
     * @throws IllegalArgumentException if addressMail is null, empty or only with spaces
     */
    public void setAddressMail(String addressMail) {
        if (addressMail == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("[POJOReferrer] L'adresse mail ne peut pas être null, vide ou ne contenir que des espaces");
        }
        this.addressMail = addressMail;
    }

    /**
     * @param refEstablishment the establishment
     */
    public void setRefEstablishment(Establishment refEstablishment) {
        this.refEstablishment = refEstablishment;
    }


    /**
     * @param numReferrer the id of the referrer
     */
    public void setNumReferrer(int numReferrer) {
        this.numReferrer = numReferrer;
    }

    /**
     * @return a String containing the name and the surname of the referrer, the phone number, the mail address, the id of the referrer
     * and the establishment.
     */
    public String toString() {

        return "Rérérant" +
                "\nNom : " + this.name +
                "\nPrenom : " + this.surname +
                "\nNuméro de téléphone : " + this.phoneNumber +
                "\nAddresse mail : " + this.addressMail +
                "\nNuméro Référent  :\n" + this.numReferrer +
                "\nEtablissement  :\n" + (refEstablishment != null ? refEstablishment.toString() : "Aucun établissement");
    }

}