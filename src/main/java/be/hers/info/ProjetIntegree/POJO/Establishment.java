package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Establishment {
    private int id;
    private String nameBuilding;
    private String phoneNumber;
    private List<String> educationLevel;
    private List<Referent> referents;
    private List<Address> addresses;

    /**
     * Initialize an establishment with id, nameBuilding, phoneNumber, educationLevel, referents and addresses
     * @param id the id of the establishment
     * @param nameBuilding the name of the building
     * @param phoneNumber the phone number
     * @param educationLevel all education levels (nursery school, primary, secondary, higher education).
     *                       It can have 1 or some education level
     * @param referents list of referents. It can have 0, 1 or some referents
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws NullPointerException if educationLevel, referents or addresses is null
     *                              if a referent in referents or an address in addresses is null
     * @throws IllegalArgumentException if educationLevel or addresses is empty
     */
    public Establishment(int id, String nameBuilding, String phoneNumber, List<String> educationLevel, List<Referent> referents, List<Address> addresses) {
        if(educationLevel == null || referents == null || addresses == null || referents.contains(null) || addresses.contains(null))
            throw new NullPointerException();

        if(educationLevel.size() == 0 || addresses.size() == 0)
            throw new IllegalArgumentException();

        this.id = id;
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
     * @throws NullPointerException if educationLevel, referents or addresses is null
     *                              if a referent in referents or an address in addresses is null
     * @throws IllegalArgumentException if educationLevel or addresses is empty
     */
    public Establishment(String nameBuilding, String phoneNumber, List<String> educationLevel, List<Referent> referents, List<Address> addresses) {
        if(educationLevel == null || referents == null || addresses == null || referents.contains(null) || addresses.contains(null))
            throw new NullPointerException();

        if(educationLevel.size() == 0 || addresses.size() == 0)
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
        this.nameBuilding = null;
        this.phoneNumber = null;
        this.educationLevel = new ArrayList<>();
        this.referents = new ArrayList<>();
        this.addresses = new ArrayList<>();
    }

    /**
     * @return the id of the establishment
     */
    public int getId() {
        return id;
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
     * @return all education levels (nursery school, primary, secondary, higher education)
     */
    public List<String> getEducationLevel() {
        return educationLevel;
    }

    /**
     * @param educationLevel all education levels (nursery school, primary, secondary, higher education).
     *                       It can have 1 or some education level
     * @throws NullPointerException if educationLevel is null
     * @throws IllegalArgumentException if educationLevel is empty
     */
    public void setEducationLevel(List<String> educationLevel) {
        if(educationLevel == null)
            throw new NullPointerException();

        if(educationLevel.size() == 0)
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
     * @throws NullPointerException if referents is null
     *                              if a referent in referents is null
     */
    public void setReferents(List<Referent> referents) {
        if(referents == null || referents.contains(null))
            throw new NullPointerException();

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
     * @throws NullPointerException if addresses is null
     *                              if an address in addresses is null
     * @throws IllegalArgumentException if addresses is empty
     */
    public void setAddresses(List<Address> addresses) {
        if(addresses == null || addresses.contains(null))
            throw new NullPointerException();

        if(addresses.size() == 0)
            throw new IllegalArgumentException();

        this.addresses = addresses;
    }

    /**
     * @return a String containing the name of the building, the phone number, the education levels, the referents and
     *         the addresses
     */
    public String toString() {
        String strAdresses = "Adresse(s) :\n";
        for(int i = 0; i < this.addresses.size(); i++){
            strAdresses += "- " + this.addresses.get(i).toString() + "\n";
        }
        strAdresses += "\n";

        String strEducationLevel = "Niveau(x) d'éducation :\n";
        for(int i = 0; i < this.educationLevel.size(); i++){
            strEducationLevel += "- " + this.educationLevel.get(i) + "\n";
        }
        strEducationLevel += "\n";

        String strReferents = "";
        if(this.referents.size() == 0)
            strReferents += "Aucun référent attribué\n";
        else{
            strReferents += "Référent(s) :\n";
            for(int i = 0; i < this.referents.size(); i++){
                strReferents += "- " + this.referents.get(i).toString() + "\n";
            }
        }
        strReferents += "\n";

        return "Etablissement " + this.nameBuilding + " :\n" +
                "Numéro de téléphonne : " + this.phoneNumber + "\n" + strAdresses + strEducationLevel + strReferents;

    }
}
