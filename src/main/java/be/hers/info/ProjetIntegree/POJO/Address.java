package be.hers.info.ProjetIntegree.POJO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-François
 */

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
    private List<Beneficiary> beneficiaries;
    private List<Interpreter> interpreters;

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
     * @throws IllegalArgumentException if postOfficeBox, locality, numStreet, beneficiaries or interpreters is null
     */
    public Address(int numAddress, int postcode, String postOfficeBox, String locality, String numStreet, String hamlet, Establishment establishment, List<Beneficiary> beneficiaries, List<Interpreter> interpreters) {
        if(postOfficeBox == null || locality == null || numStreet == null || beneficiaries == null || interpreters == null){
            throw new IllegalArgumentException("[POJOAddress] : postOfficeBox et/ou locality et/ou numStreet et/ou beneficiaries et/ou interpreters est null");
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
     * Initialize an address with postcode, postOfficeBox, locality, numStreet, hamlet, establishment, beneficiaries, interpreters
     * @param postcode the postcode
     * @param postOfficeBox the post office box. It can only have 1 post office box
     * @param locality the locality. It can only have 1 locality
     * @param numStreet the number street. It can only have 1 number street
     * @param hamlet the hamlet
     * @param establishment the establishment linked. It can have 0 or 1 establishment
     * @param beneficiaries the list of beneficiary linked. It can have 0, 1 or some beneficiaries
     * @param interpreters the list of interpreter linked. It can have 0, 1 or some interpreters
     * @throws IllegalArgumentException if postOfficeBox, locality, numStreet, beneficiaries or interpreters is null
     */
    public Address(int postcode, String postOfficeBox, String locality, String numStreet, String hamlet, Establishment establishment, List<Beneficiary> beneficiaries, List<Interpreter> interpreters) {
        if(postOfficeBox == null || locality == null || numStreet == null || beneficiaries == null || interpreters == null){
            throw new IllegalArgumentException("[POJOAddress] : postOfficeBox et/ou locality et/ou numStreet et/ou beneficiaries et/ou interpreters est null");
        }

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
     * @param numAddress the id
     * @param postcode the postcode
     * @param postOfficeBox the post office box. It can only have 1 post office box
     * @param locality the locality. It can only have 1 locality
     * @param hamlet the hamlet
     * @param establishment the establishment linked. It can have 0 or 1 establishment
     * @throws IllegalArgumentException if postOfficeBox, locality, numStreet, beneficiaries or interpreters is null
     */
    public Address(int numAddress, int postcode, String postOfficeBox, String locality, String hamlet, Establishment establishment) {
        if(postOfficeBox == null || locality == null){
            throw new IllegalArgumentException("[POJOAddress] : postOfficeBox et/ou locality est null");
        }

        this.numAddress = numAddress;
        this.postcode = postcode;
        this.postOfficeBox = postOfficeBox;
        this.locality = locality;
        this.hamlet = hamlet;
        this.establishment = establishment;
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
     * @param numAddress the id
     */
    public void setNumAddress(int numAddress) {
        this.numAddress = numAddress;
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
     * @throws IllegalArgumentException if postOfficeBox is null
     */
    public void setPostOfficeBox(String postOfficeBox) {
        if(postOfficeBox == null){
            throw new IllegalArgumentException("[POJOAddress] : On ne peut pas set la boite postale à null");
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
     * @throws IllegalArgumentException if locality is null
     */
    public void setLocality(String locality) {
        if(locality == null){
            throw new IllegalArgumentException("[POJOAddress] : On ne peut pas set la localité à null");
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
     * @throws IllegalArgumentException if numStreet is null
     */
    public void setNumStreet(String numStreet) {
        if(numStreet == null){
            throw new IllegalArgumentException("[POJOAddress] : On ne peut pas set le numéro de la rue à null");
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
     * @throws IllegalArgumentException if beneficiaries is null
     */
    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        if(beneficiaries == null){
            throw new IllegalArgumentException("[POJOAddress] : On ne peut pas set la liste des bénéficiaires à null");
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
     * @throws IllegalArgumentException if interpreters is null
     */
    public void setInterpreters(List<Interpreter> interpreters) {
        if(interpreters == null){
            throw new IllegalArgumentException("[POJOAddress] : On ne peut pas set la liste des interprètes à null");
        }

        this.interpreters = interpreters;
    }

    /**
     * @return a String containing the numAddress, postcode, postOfficeBox, locality, numStreet, hamlet, establishment,
     *         beneficiaries, interpreters
     */
    public String toString() {
        StringBuilder strBeneficiaries = new StringBuilder("");
        if(this.beneficiaries.isEmpty())
            strBeneficiaries.append("Aucun bénédiciaire lié\n");
        else {
            strBeneficiaries.append("Bénéficiaire(s) lié(s) : \n");
            for (Beneficiary beneficiary : this.beneficiaries) {
                strBeneficiaries.append("- " + beneficiary.toString() + "\n");
            }
        }
        strBeneficiaries.append("\n");

        StringBuilder strInterpreters = new StringBuilder("");
        if(this.interpreters.isEmpty())
            strInterpreters.append("Aucun interprète lié\n");
        else{
            strInterpreters.append("Interprète(s) lié(s) :\n");
            for(Interpreter interpreter : this.interpreters){
                strInterpreters.append("- " + interpreter.toString() + "\n");
            }
        }
        strInterpreters.append("\n");

        return "Adresse" +
                "\nId : " + this.numAddress +
                "\n" +
                "\nCode postal : " + this.postcode +
                "\nBoite postale : " + this.postOfficeBox +
                "\nLocalité : " + this.locality +
                "\nNuméro de la maison : " + this.numStreet +
                "\nLieu dit : " + this.hamlet +
                "\nEtablissement lié : " + (this.establishment == null ? "Aucun" : this.establishment.toString()) +
                "\n" + strBeneficiaries + strInterpreters;
    }
}
