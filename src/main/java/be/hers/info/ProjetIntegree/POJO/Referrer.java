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
    private Establishment refEstablishment;

    /**
     * Initialize a Referrer with refEstablishment, addressMail, phoneNumber, surname and name
     * @param refEstablishment the establishment where the referrer works
     * @param addressMail the mail of the referrer
     * @param phoneNumber the phone number
     * @param surname the surname of the referrer
     * @param name the name of the referrer
     * @throws IllegalArgumentException if refEstablishment, addressMail, phoneNumber,surname or name is null
     */
    public Referrer(Establishment refEstablishment, String addressMail, String phoneNumber, String surname, String name) {
        if(refEstablishment == null || addressMail == null || phoneNumber == null || surname == null || name == null ) {
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
     * @param name the name
     * @throws IllegalArgumentException if name is null
     */
    public void setName(String name) {
        if(name == null ) {
            throw new IllegalArgumentException("[POJOReferrer] Le prénom ne peut pas être null");
        }
        this.name = name;
    }

    /**
     * @param surname the surname
     * @throws IllegalArgumentException if surname is null
     */
    public void setSurname(String surname) {
        if(surname == null ) {
            throw new IllegalArgumentException("[POJOReferrer] Le nom ne peut pas être null");
        }
        this.surname = surname;
    }

    /**
     * @param phoneNumber the phone number
     * @throws IllegalArgumentException if phoneNumber is null
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null ) {
            throw new IllegalArgumentException("[POJOReferrer] Le numéro de téléphone ne peut pas être null");
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     * @param addressMail the mail address
     * @throws IllegalArgumentException if addressMail is null
     */
    public void setAddressMail(String addressMail) {
        if(addressMail == null ) {
            throw new IllegalArgumentException("[POJOReferrer] L'adresse mail ne peut pas être null");
        }
        this.addressMail = addressMail;
    }

    /**
     * @param refEstablishment the establishment
     * @throws IllegalArgumentException if refEstablishment is null
     */
    public void setRefEstablishment(Establishment refEstablishment) {
        if(refEstablishment == null ) {
            throw new IllegalArgumentException("[POJOReferrer] L'établissement ne peut pas être null ou vide");
        }
        this.refEstablishment = refEstablishment;
    }

    /**
     * @return a String containing the name and the surname of the referrer, the phone number, the mail address,
     * and the establishment.
     */
    public String toString(){

        return "Rérérant" +
                "\nNom : " + this.name +
                "\nPrenom : " + this.surname +
                "\nNuméro de téléphone : " + this.phoneNumber +
                "\nAddresse mail : " + this.addressMail +
                "\nEtablissement  :\n" + (refEstablishment != null ? refEstablishment.toString() : "Aucun établissement");
    }
}