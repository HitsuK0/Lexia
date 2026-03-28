package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Establishment {
    private int numEstablishment;
    private String nameBuilding;
    private String phoneNumber;
    private List<Integer> educationLevel;
    private List<Referrer> referrers;
    private List<Address> addresses;

    /**
     * Initialize an establishment with numEstablishment, nameBuilding, phoneNumber, educationLevel, referrers and addresses
     * @param numEstablishment the id of the establishment
     * @param nameBuilding the name of the building
     * @param phoneNumber the phone number
     * @param educationLevel all education levels (nursery school - 1, primary - 2, secondary - 3, higher education - 4).
     *                       It can have 0, 1 or some education level
     * @param referrers list of referrers. It can have 0, 1 or some referrers
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws IllegalArgumentException if educationLevel contains at least one element < 1 or > 4
     *                                  if educationLevel, referrers or addresses is null
     *                                  if addresses is empty
     */
    public Establishment(int numEstablishment, String nameBuilding, String phoneNumber, List<Integer> educationLevel,
                         List<Referrer> referrers, List<Address> addresses) {
        if(educationLevel == null || educationLevel.stream().anyMatch(level -> level < 1 || level > 4) || referrers == null ||
                addresses == null || addresses.isEmpty())
            throw new IllegalArgumentException("[POJOEstablishment] : educationLevel et/ou referrers et/ou addresses == null et/ou addresses est vide et/ou educationLevel ne contient que des niveaux entre 1 et 4 compris");

        this.numEstablishment = numEstablishment;
        this.nameBuilding = nameBuilding;
        this.phoneNumber = phoneNumber;
        this.educationLevel = educationLevel;
        this.referrers = referrers;
        this.addresses = addresses;
    }

    /**
     * Initialize an establishment with nameBuilding, phoneNumber, educationLevel, referrers and addresses
     * @param nameBuilding the name of the building
     * @param phoneNumber the phone number
     * @param educationLevel all education levels (nursery school, primary, secondary, higher education).
     *                       It can have 0, 1 or some education level
     * @param referrers list of referrers. It can have 0, 1 or some referrers
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws IllegalArgumentException if educationLevel contains at least one element < 1 or > 4
     *                                  if educationLevel, referrers or addresses is null
     *                                  if addresses is empty
     */
    public Establishment(String nameBuilding, String phoneNumber, List<Integer> educationLevel,
                         List<Referrer> referrers,List<Address> addresses) {
        if(educationLevel == null || educationLevel.stream().anyMatch(level -> level < 1 || level > 4) || referrers == null ||
                addresses == null || addresses.isEmpty())
            throw new IllegalArgumentException("[POJOEstablishment] : educationLevel et/ou referrers et/ou addresses == null et/ou addresses est vide et/ou educationLevel ne contient que des niveaux entre 1 et 4 compris");

        this.nameBuilding = nameBuilding;
        this.phoneNumber = phoneNumber;
        this.educationLevel = educationLevel;
        this.referrers = referrers;
        this.addresses = addresses;
    }

    /**
     * Initialize an establishment with no elements
     */
    public Establishment() {
        this.nameBuilding = "";
        this.phoneNumber = "";
        this.educationLevel = new ArrayList<>();
        this.referrers = new ArrayList<>();
        this.addresses = new ArrayList<>();
    }

    /**
     * @return the id of the establishment
     */
    public int getnumEstablishment() {
        return numEstablishment;
    }

    /**
     * @param numEstablishment the id of the establishment
     */
    public void setNumEstablishment(int numEstablishment) {
        this.numEstablishment = numEstablishment;
    }

    /**
     * @return the name of the building
     */
    public String getNameBuilding() {
        return nameBuilding;
    }

    /**
     * @param nameBuilding the name of the building
     */
    public void setNameBuilding(String nameBuilding) {
        this.nameBuilding = nameBuilding;
    }

    /**
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return all education levels (nursery school - 1, primary - 2, secondary - 3, higher education - 4)
     */
    public List<Integer> getEducationLevel() {
        return educationLevel;
    }

    /**
     * @param educationLevel all education levels (nursery school - 1, primary - 2, secondary - 3, higher education - 4).
     *                       It can have 1 or some education level
     * @throws IllegalArgumentException if educationLevel contains at least one element < 1 or > 4
     *                                  if educationLevel is null
     */
    public void setEducationLevel(List<Integer> educationLevel) {
        if(educationLevel == null || educationLevel.stream().anyMatch(level -> level < 1 || level > 4))
            throw new IllegalArgumentException("[POJOEstablishment] : educationLevel == null et/ou educationLevel ne contient que des niveaux entre 1 et 4 compris");

        this.educationLevel = educationLevel;
    }

    /**
     * @return list of referrers
     */
    public List<Referrer> getReferrers() {
        return referrers;
    }

    /**
     * @param referrers list of referrers. It can have 0, 1 or some referrers
     * @throws IllegalArgumentException if referrers is null
     */
    public void setReferrers(List<Referrer> referrers) {
        if(referrers == null)
            throw new IllegalArgumentException("[POJOEstablishment] : referrers == null");

        this.referrers = referrers;
    }

    /**
     * @return list of Addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws IllegalArgumentException if addresses is null or empty
     */
    public void setAddresses(List<Address> addresses) {
        if(addresses == null || addresses.contains(null))
            throw new IllegalArgumentException("[POJOEstablishment] : addresses == null");

        this.addresses = addresses;
    }

    /**
     * @return a String containing the name of the building, the phone number, the education levels, the referrers and
     *         the addresses
     */
    public String toString() {
        String strAdresses = "Adresse(s) :\n";
        for(Address address : addresses){
            strAdresses += "- " + address.toString() + "\n";
        }
        strAdresses += "\n";

        String strEducationLevel = "Niveau(x) d'éducation :\n";
        for(Integer level : educationLevel){
            strEducationLevel += "- " + level + "\n";
        }
        strEducationLevel += "\n";

        String strReferrers = "";
        if(this.referrers.isEmpty())
            strReferrers += "Aucun référent attribué\n";
        else{
            strReferrers += "Référent(s) :\n";
            for(Referrer referrer : referrers){
                strReferrers += "- " + referrer.toString() + "\n";
            }
        }
        strReferrers += "\n";

        return "Etablissement n°" + this.numEstablishment + " nommé " + this.nameBuilding + " :\n" +
                "Numéro de téléphonne : " + this.phoneNumber + "\n" + strAdresses + strEducationLevel + strReferrers;

    }
}
