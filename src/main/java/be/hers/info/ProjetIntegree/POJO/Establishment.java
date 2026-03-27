package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Establishment {
    private int numEstablishment;
    private String nameBuilding;
    private String phoneNumber;
    private List<Integer> educationLevel;
    private List<Referent> referents;
    private List<Address> addresses;

    /**
     * Initialize an establishment with numEstablishment, nameBuilding, phoneNumber, educationLevel, referents and addresses
     * @param numEstablishment the id of the establishment
     * @param nameBuilding the name of the building
     * @param phoneNumber the phone number
     * @param educationLevel all education levels (nursery school - 1, primary - 2, secondary - 3, higher education - 4).
     *                       It can have 1 or some education level
     * @param referents list of referents. It can have 0, 1 or some referents
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws IllegalArgumentException if educationLevel contains at least one element < 1 or > 4
     *                                  if educationLevel, referents or addresses is null
     *                                  if educationLevel or addresses is empty
     */
    public Establishment(int numEstablishment, String nameBuilding, String phoneNumber, List<Integer> educationLevel,
                         List<Referent> referents, List<Address> addresses) {
        if(educationLevel == null || educationLevel.isEmpty() ||
                educationLevel.stream().anyMatch(level -> level < 1 || level > 4) || referents == null ||
                addresses == null || addresses.isEmpty())
            throw new IllegalArgumentException();

        this.numEstablishment = numEstablishment;
        this.nameBuilding = nameBuilding;
        this.phoneNumber = phoneNumber;
        this.educationLevel = educationLevel;
        this.referents = referents;
        this.addresses = addresses;
    }

    /**
     * Initialize an establishment with nameBuilding, phoneNumber, educationLevel, referents and addresses
     * @param nameBuilding the name of the building
     * @param phoneNumber the phone number
     * @param educationLevel all education levels (nursery school, primary, secondary, higher education).
     *                       It can have 1 or some education level
     * @param referents list of referents. It can have 0, 1 or some referents
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws IllegalArgumentException if educationLevel contains at least one element < 1 or > 4
     *                                  if educationLevel, referents or addresses is null
     *                                  if educationLevel or addresses is empty
     */
    public Establishment(String nameBuilding, String phoneNumber, List<Integer> educationLevel,
                         List<Referent> referents,List<Address> addresses) {
        if(educationLevel == null || educationLevel.isEmpty() ||
                educationLevel.stream().anyMatch(level -> level < 1 || level > 4) || referents == null ||
                addresses == null || addresses.isEmpty())
            throw new IllegalArgumentException();

        this.nameBuilding = nameBuilding;
        this.phoneNumber = phoneNumber;
        this.educationLevel = educationLevel;
        this.referents = referents;
        this.addresses = addresses;
    }

    /**
     * Initialize an establishment with no elements
     */
    public Establishment() {
        this.nameBuilding = "";
        this.phoneNumber = "";
        this.educationLevel = new ArrayList<>();
        this.referents = new ArrayList<>();
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
     *                                  if educationLevel is null or educationLevel is empty
     */
    public void setEducationLevel(List<Integer> educationLevel) {
        if(educationLevel == null || educationLevel.isEmpty() ||
                educationLevel.stream().anyMatch(level -> level < 1 || level > 4))
            throw new IllegalArgumentException();

        this.educationLevel = educationLevel;
    }

    /**
     * @return list of referents
     */
    public List<Referent> getReferents() {
        return referents;
    }

    /**
     * @param referents list of referents. It can have 0, 1 or some referents
     * @throws IllegalArgumentException if referents is null
     */
    public void setReferents(List<Referent> referents) {
        if(referents == null)
            throw new IllegalArgumentException();

        this.referents = referents;
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
            throw new IllegalArgumentException();

        this.addresses = addresses;
    }

    /**
     * @return a String containing the name of the building, the phone number, the education levels, the referents and
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

        String strReferents = "";
        if(this.referents.isEmpty())
            strReferents += "Aucun référent attribué\n";
        else{
            strReferents += "Référent(s) :\n";
            for(Referent referent : referents){
                strReferents += "- " + referent.toString() + "\n";
            }
        }
        strReferents += "\n";

        return "Etablissement n°" + this.numEstablishment + " nommé " + this.nameBuilding + " :\n" +
                "Numéro de téléphonne : " + this.phoneNumber + "\n" + strAdresses + strEducationLevel + strReferents;

    }
}
