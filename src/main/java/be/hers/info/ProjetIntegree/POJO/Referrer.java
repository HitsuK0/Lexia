package be.hers.info.ProjetIntegree.POJO;

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
     * @throws NullPointerException if refEstablishment, addressMail, phoneNumber,surname or name is null
     */
    public Referrer(Establishment refEstablishment, String addressMail, String phoneNumber, String surname, String name) {
        if(refEstablishment == null || addressMail == null || phoneNumber == null || surname == null || name == null ) {
            throw new NullPointerException();
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
     * @throws NullPointerException if name is null
     */
    public void setName(String name) {
        if(name == null ) {
            throw new NullPointerException("Valeur invalide : le prénom ne doit pas être a null");
        }


        this.name = name;
    }


    /**
     * @param surname the surname
     * @throws NullPointerException if surname is null
     */
    public void setSurname(String surname) {
        if(surname == null ) {
            throw new NullPointerException("Valeur invalide : le nom ne doit pas être a null");
        }
        this.surname = surname;
    }


    /**
     * @param phoneNumber the phone number
     * @throws NullPointerException if phoneNumber is null
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null ) {
            throw new NullPointerException("Valeur invalide : le numéro de téléphone ne doit pas être a null");
        }
        this.phoneNumber = phoneNumber;
    }


    /**
     * @param addressMail the mail address
     * @throws NullPointerException if addressMail is null
     */
    public void setAddressMail(String addressMail) {
        if(addressMail == null ) {
            throw new NullPointerException("Valeur invalide : l'adresse mail ne doit pas être a null");
        }
        this.addressMail = addressMail;
    }

    /**
     * @param refEstablishment the establishment
     * @throws NullPointerException if refEstablishment is null
     */
    public void setRefEstablishment(Establishment refEstablishment) {
        if(refEstablishment == null ) {
            throw new NullPointerException("Valeur invalide : l'établissement ne doit pas être a null");
        }
        this.refEstablishment = refEstablishment;
    }

    /**
     * @return a String containing the name and the surname of the referrer, the phone number, the mail address,
     * and the establishment.
     */
    public String toString(){

        return "Nom : " + this.name + " \n" + "Prenom : " + this.surname + " \n" +
                "Numéro de téléphone : " + this.phoneNumber + " \n" +
                "Addresse mail : " + this.addressMail + " \n" +
                "Etablissement  :\n" + (refEstablishment != null ? refEstablishment.toString() : "Aucun établissement");

    }
}