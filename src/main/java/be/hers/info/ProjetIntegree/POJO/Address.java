package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Address {
    private int numAddress;
    private int postcode;
    private String postOfficeBox;
    private String locality;
    private String numStreet;
    private String hamlet;
    private Establishment establishment;
    private List<Beneficiary> beneficiaries; //0..*
    private List<Interpreter> interpreters; //0..*

    /**
     * Initialize an address with numAddress, postcode, postOfficeBox, locality, numStreet, hamlet, establishment, beneficiaries, interpreters
     * @param numAddress the id
     * @param postcode the postcode
     * @param postOfficeBox the post office box. It can only have 1 post office box
     * @param locality the locality. It can only have 1 locality
     * @param numStreet the number street. It can only have 1 number street
     * @param hamlet the hamlet
     * @param establishment the establishment linked. It can have 0 or 1 establishment
     * @param beneficiaries the list of beneficiary linked. It can have 0, 1 or some beneficiaries
     * @param interpreters the list of interpreter linked. It can have 0, 1 or some interpreters
     * @throws NoSuchElementException if postOfficeBox, locality, numStreet, beneficiaries or interpreters is null
     */
    public Address(int numAddress, int postcode, String postOfficeBox, String locality, String numStreet, String hamlet, Establishment establishment, List<Beneficiary> beneficiaries, List<Interpreter> interpreters) {
        if(postOfficeBox == null || locality == null || numStreet == null || beneficiaries == null || interpreters == null){
            throw new NoSuchElementException();
        }

        this.numAddress = numAddress;
        this.postcode = postcode;
        this.postOfficeBox = postOfficeBox;
        this.locality = locality;
        this.numStreet = numStreet;
        this.hamlet = hamlet;
        this.establishment = establishment;
        this.beneficiaries = beneficiaries;
        this.interpreters = interpreters;
    }

    /**
     * Initialize an address with numAddress, postcode, postOfficeBox, locality, numStreet, hamlet, establishment, beneficiaries, interpreters
     * @throws NoSuchElementException if postOfficeBox, locality, numStreet, beneficiaries or interpreters is null
     */
    public Address(int numAddress, int postcode, String postOfficeBox, String locality, String numStreet, String hamlet, Establishment establishment, List<Beneficiary> beneficiaries, List<Interpreter> interpreters) {
        if(postOfficeBox == null || locality == null || numStreet == null || beneficiaries == null || interpreters == null){
            throw new NoSuchElementException();
        }

        this.numAddress = numAddress;
        this.postcode = postcode;
        this.postOfficeBox = postOfficeBox;
        this.locality = locality;
        this.numStreet = numStreet;
        this.hamlet = hamlet;
        this.establishment = establishment;
        this.beneficiaries = beneficiaries;
        this.interpreters = interpreters;
    }

    /**
     * Initialize an address with no elements
     */
    public Address() {
        this.numAddress = -1;
        this.postcode = -1;
        this.postOfficeBox = "";
        this.locality = "";
        this.numStreet = "";
        this.hamlet = "";
        this.establishment = null;
        this.beneficiaries = new ArrayList<>();
        this.interpreters = new ArrayList<>();
    }

    /**
     * @return the id
     */
    public int getNumAddress() {
        return numAddress;
    }

    /**
     * @return the postcode
     */
    public int getPostcode() {
        return postcode;
    }

    /**
     * @param postcode the postcode
     */
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    /**
     * @return the post office box
     */
    public String getPostOfficeBox() {
        return postOfficeBox;
    }

    /**
     * @param postOfficeBox the post office box. It can only have 1 post office box
     * @throws NoSuchElementException if postOfficeBox is null
     */
    public void setPostOfficeBox(String postOfficeBox) {
        if(postOfficeBox == null){
            throw new NoSuchElementException();
        }

        this.postOfficeBox = postOfficeBox;
    }

    /**
     * @return the locality
     */
    public String getLocality() {
        return locality;
    }

    /**
     * @param locality the locality. It can only have 1 locality
     * @throws NoSuchElementException if locality is null
     */
    public void setLocality(String locality) {
        if(locality == null){
            throw new NoSuchElementException();
        }

        this.locality = locality;
    }

    /**
     * @return the number street
     */
    public String getNumStreet() {
        return numStreet;
    }

    /**
     * @param numStreet the number street. It can only have 1 number street
     * @throws NoSuchElementException if numStreet is null
     */
    public void setNumStreet(String numStreet) {
        if(numStreet == null){
            throw new NoSuchElementException();
        }

        this.numStreet = numStreet;
    }

    /**
     * @return the hamlet
     */
    public String getHamlet() {
        return hamlet;
    }

    /**
     * @param hamlet the hamlet
     */
    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

    /**
     * @return the establishment linked
     */
    public Establishment getEstablishment() {
        return establishment;
    }

    /**
     * @param establishment the establishment linked
     */
    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    /**
     * @return the list of beneficiary linked
     */
    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    /**
     * @param beneficiaries the list of beneficiary linked. It can have 0, 1 or some beneficiaries
     * @throws NoSuchElementException if beneficiaries is null
     */
    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        if(beneficiaries == null){
            throw new NoSuchElementException();
        }

        this.beneficiaries = beneficiaries;
    }

    /**
     * @return the list of interpreter
     */
    public List<Interpreter> getInterpreters() {
        return interpreters;
    }

    /**
     * @param interpreters the list of interpreter linked. It can have 0, 1 or some interpreters
     * @throws NoSuchElementException if interpreters is null
     */
    public void setInterpreters(List<Interpreter> interpreters) {
        if(interpreters == null){
            throw new NoSuchElementException();
        }

        this.interpreters = interpreters;
    }

    /**
     * @return a String containing the numAddress, postcode, postOfficeBox, locality, numStreet, hamlet, establishment,
     *         beneficiaries, interpreters
     */
    public String toString() {
        String strBeneficiaries = "";
        if(this.beneficiaries.size() == 0)
            strBeneficiaries += "Aucun bénédiciaire lié\n";
        else {
            strBeneficiaries = "Bénéficiaire(s) lié(s) : \n";
            for (Beneficiary beneficiary : this.beneficiaries) {
                strBeneficiaries += "- " + beneficiary.toString() + "\n";
            }
        }
        strBeneficiaries += "\n";

        String strInterpreters = "";
        if(this.interpreters.size() == 0)
            strInterpreters += "Aucun interprète lié\n";
        else{
            strInterpreters += "Interprète(s) lié(s) :\n";
            for(Interpreter interpreter : this.interpreters){
                strInterpreters += "- " + interpreter.toString() + "\n";
            }
        }
        strInterpreters += "\n";

        return "Adresse n°" + this.numAddress + " :\n" +
                "Code postal : " + this.postcode + "\n" +
                "Boite postale : " + this.postOfficeBox + "\n" +
                "Localité : " + this.locality + "\n" +
                "Numéro de la maison : " + this.numStreet + "\n" +
                "Lieu dit : " + this.hamlet + "\n" +
                "Etablissement lié : " + this.establishment.toString() + "\n" + strBeneficiaries + strInterpreters;
    }
}
