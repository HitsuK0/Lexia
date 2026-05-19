package be.hers.info.ProjetIntegree.POJO;

/*
@author Rosman Loïs
@reviewer Nicolas Jean-François
 */

public class Address {
    private int numAddress;
    private int postcode;
    private String postOfficeBox;
    private String locality;
    private String hamlet;
    private Establishment establishment;

    /**
     * Initialize an address with postcode, postOfficeBox, locality, hamlet, establishment
     * @param postcode the postcode
     * @param postOfficeBox the post office box. It can only have 1 post office box
     * @param locality the locality. It can only have 1 locality
     * @param hamlet the hamlet
     * @param establishment the establishment linked. It can have 0 or 1 establishment
     * @throws IllegalArgumentException if postOfficeBox or locality is null
     */
    public Address(int postcode, String postOfficeBox, String locality, String hamlet, Establishment establishment) {
        if(postOfficeBox == null || locality == null){
            throw new IllegalArgumentException("[POJOAddress] : postOfficeBox et/ou locality est null");
        }

        this.postcode = postcode;
        this.postOfficeBox = postOfficeBox;
        this.locality = locality;
        this.hamlet = hamlet;
        this.establishment = establishment;
    }

    /**
     * Initialize an address with numAddress, postcode, postOfficeBox, locality, hamlet, establishment
     * @param numAddress the id
     * @param postcode the postcode
     * @param postOfficeBox the post office box. It can only have 1 post office box
     * @param locality the locality. It can only have 1 locality
     * @param hamlet the hamlet
     * @param establishment the establishment linked. It can have 0 or 1 establishment
     * @throws IllegalArgumentException if postOfficeBox or locality is null
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
        this.hamlet = "";
        this.establishment = null;
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
     * @return a String containing the numAddress, postcode, postOfficeBox, locality, hamlet and establishment
     */
    public String toString() {
        return "Adresse" +
                "\nId : " + this.numAddress +
                "\n" +
                "\nCode postal : " + this.postcode +
                "\nBoite postale : " + this.postOfficeBox +
                "\nLocalité : " + this.locality +
                "\nLieu dit : " + this.hamlet +
                "\nEtablissement lié : " + (this.establishment == null ? "Aucun" : this.establishment.toString());
    }

}
