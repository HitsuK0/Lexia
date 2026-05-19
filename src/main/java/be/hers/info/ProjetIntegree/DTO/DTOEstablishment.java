package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DTO used for the page etablissements.html
 * A minimalist object of Establishment.
 */
public class DTOEstablishment {

    private int numEstablishment; // pour ajouter referant.

    // Establishment
    private String nameBuilding;
    private String phoneNumberEstablishment;
    private String locality;
    private int postcode;
    private String postOfficeBox;
    private String hamlet;
    private List<String> educationLevel;
    private List<DTOReferrer>  dtoReferrers;

    //Affichage :
    private String displayReferrer;
    private String address; // pour l'affichage.


    private final String ZERO = "autre";
    private final String ONE = "maternelle";
    private final String TWO = "primaire";
    private final String THREE = "secondaire";
    private final String FOUR = "supérieur";


    /**
     * Initialize a DTOEsablishment without any fields.
     */
    public DTOEstablishment() {

    }

    /**
     * Create a DTOEstablishment using an Establishment in param.
     * The educationLevel is initialiaze with the following data
     * (autre, maternelle, primaire, secondaire, supérieur) multiple data is possible
     * The name of the referrers are in uppercase (not the surname)
     *
     * @param etablissement is the Establishment using to initialize the this.
     */
    public DTOEstablishment(Establishment etablissement){
        this.numEstablishment = etablissement.getNumEstablishment();
        this.nameBuilding = etablissement.getNameBuilding();
        this.phoneNumberEstablishment = etablissement.getPhoneNumber();
        educationLevel = new ArrayList<>();
        for (Integer level : etablissement.getEducationLevel()) {
            switch (level) {
                case 0 -> educationLevel.add(ZERO);
                case 1 -> educationLevel.add(ONE);
                case 2 -> educationLevel.add(TWO);
                case 3 -> educationLevel.add(THREE);
                case 4 -> educationLevel.add(FOUR);
            }
        }
        this.locality = etablissement.getAddresses().getFirst().getLocality();
        this.postOfficeBox = etablissement.getAddresses().getFirst().getPostOfficeBox();
        this.hamlet = etablissement.getAddresses().getFirst().getHamlet();
        this.address = toStringFront();

        // initialiser referant pour display
        dtoReferrers = new ArrayList<>();
        List<Referrer> referrersLst = etablissement.getReferrers();
        Iterator<Referrer> iterator = referrersLst.iterator();
        StringBuilder referrersSchool = new StringBuilder();
        while(iterator.hasNext()){
            Referrer referrer = iterator.next();
            dtoReferrers.add(new  DTOReferrer(referrer));
            referrersSchool.append(referrer.getName().toUpperCase());
            referrersSchool.append(" ");
            referrersSchool.append(referrer.getSurname());
            if(iterator.hasNext()){
                referrersSchool.append(", ");
            }
        }
        this.displayReferrer = referrersSchool.toString();
    }


    /**
     * @return a String containing the locality, the postcode, the hamlet
     * this function make a string for the front.
     */
    public String toStringFront() {
        StringBuilder front = new StringBuilder();
        front.append(this.postOfficeBox);
        front.append(",<br>");
        if(this.hamlet != null){
            front.append(this.hamlet);
            front.append(" ");
        }
        front.append(this.postcode); // mis à 0 dans BD.
        front.append(" ");
        front.append(this.locality);
        return front.toString();
    }

    /**
     * Get the num of the Establishment.
     * @return the num of the Establishment in a int.
     */
    public int getNumEstablishment() {
        return numEstablishment;
    }




    /**
     * Get a string representation of the referrers.
     * @return the displayReferrer.
     */
    public String getDisplayReferrer() {
        return displayReferrer;
    }

    /**
     * Get the name of the Building
     * @return the name of the building in a String.
     */
    public String getNameBuilding() {
        return nameBuilding;
    }

    /**
     *
     * @return the phone number in a String of the establishment.
     */
    public String getPhoneNumber() {
        return phoneNumberEstablishment;
    }

    /**
     *
     * @return the level of education of the Establishment
     */
    public List<String> getEducationLevel() {
        return educationLevel;
    }


    /**
     * Get a list of eduaction level with the value in integer.
     * @return a list of Integer with the value of all the education level possible.
     */
    public List<Integer> getEducationLevelInt(){
        List<Integer> educationLevelInt = new ArrayList<>();
        for (String level : this.educationLevel) {
            switch (level) {
                case ZERO -> educationLevelInt.add(0);
                case ONE -> educationLevelInt.add(1);
                case TWO -> educationLevelInt.add(2);
                case THREE -> educationLevelInt.add(3);
                case FOUR -> educationLevelInt.add(4);
            }
        }
        return educationLevelInt;
    }


    /**
     *
     * @return the address of the Establishment in a String.
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return the locality of the Establishment.
     */
    public String getLocality() {
        return locality;
    }


    /**
     * Get the post code of the this.
     * @return the postcode.
     */
    public int getPostcode() {
        return postcode;
    }


    /**
     * Get the post office box.
     * @return postOfficeBox.
     */
    public String getPostOfficeBox() {
        return postOfficeBox;
    }

    /**
     * Get the hamlet of the current Establishment.
     * @return the hamlet
     */
    public String getHamlet() {
        return hamlet;
    }

    /**
     * Set the num of the Establishment with the given param.
     * @param numEstablishment is the new num of the Establishment.
     */
    public void setNumEstablishment(int numEstablishment) {
        this.numEstablishment = numEstablishment;
    }


    /**
     * Set the String for the displayReferrer.
     * @param displayReferrer is the new displayReferrer.
     */
    public void setDisplayReferrer(String displayReferrer) {
        this.displayReferrer = displayReferrer;
    }

    /**
     * Set the new name of the building given in param.
     * @param nameBuilding the new name of the building
     */
    public void setNameBuilding(String nameBuilding) {
        this.nameBuilding = nameBuilding;
    }

    /**
     * Set a new phoneNumber with the given param.
     * @param phoneNumber is the new phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumberEstablishment = phoneNumber;
    }


    /**
     * Set the new education level of the Establishment with the given param.
     * @param educationLevel is the new educationLevel.
     */
    public void setEducationLevel(List<String> educationLevel) {
        this.educationLevel = educationLevel;
    }

    /**
     * Set the new address with the given param.
     * @param address is the new address of this.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Set the new locality of the Establishment with the given param.
     * @param locality is the new locality of this.
     */
    public void setLocality(String locality) {
        this.locality = locality;
    }


    /**
     * Set the new post code for the this.
     * @param postcode is the new post code given in param.
     */
    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    /**
     * Set the post office box for the this.
     * @param postOfficeBox is the new post office box given in param.
     */
    public void setPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
    }

    /**
     * Set a new value for hamlet.
     * @param hamlet is the new value.
     */
    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

}
