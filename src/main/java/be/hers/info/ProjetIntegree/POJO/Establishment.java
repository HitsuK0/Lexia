package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Establishment {
    private String nameBuilding;
    private String phoneNumber;
    private List<String> educationLevel;
    private List<Specialist> specialists;
    private List<Address> addresses;

    /**
     * Initialize an establishment with nameBuilding, phoneNumber, educationLevel, specialists and addresses
     * @param nameBuilding the name of the building
     * @param phoneNumber the phone number
     * @param educationLevel all education levels (nursery school, primary, secondary, higher education).
     *                       It can have 1 or some education level
     * @param specialists list of specialists. It can have 0, 1 or some specialists
     * @param addresses list of Addresses. It can have 1 or some addresses
     * @throws NullPointerException if educationLevel, specialists or addresses is null
     *                              if a specialist in specialists or an address in addresses is null
     * @throws IllegalArgumentException if educationLevel or addresses is empty
     */
    public Establishment(String nameBuilding, String phoneNumber, List<String> educationLevel, List<Specialist> specialists, List<Address> addresses) {
        if(educationLevel == null || specialists == null || addresses == null || specialists.contains(null) || addresses.contains(null))
            throw new NullPointerException();

        if(educationLevel.size() == 0 || addresses.size() == 0)
            throw new IllegalArgumentException();

        this.nameBuilding = nameBuilding;
        this.phoneNumber = phoneNumber;
        this.educationLevel = educationLevel;
        this.specialists = specialists;
        this.addresses = addresses;
    }

    /**
     * Initialize an establishment with no elements
     */
    public Establishment() {
        this.nameBuilding = null;
        this.phoneNumber = null;
        this.educationLevel = new ArrayList<>();
        this.specialists = new ArrayList<>();
        this.addresses = new ArrayList<>();
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
     * @return list of specialists
     */
    public List<Specialist> getSpecialists() {
        return specialists;
    }

    /**
     * @param specialists list of specialists. It can have 0, 1 or some specialists
     * @throws NullPointerException if specialists is null
     *                              if a specialist in specialists is null
     */
    public void setSpecialists(List<Specialist> specialists) {
        if(specialists == null || specialists.contains(null))
            throw new NullPointerException();

        this.specialists = specialists;
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
     * @return a String containing the name of the building, the phone number, the education levels, the specialists and
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

        String strSpecialists = "";
        if(this.specialists.size() == 0)
            strSpecialists += "Aucun référent attribué\n";
        else{
            strSpecialists += "Référent(s) :\n";
            for(int i = 0; i < this.specialists.size(); i++){
                strSpecialists += "- " + this.specialists.get(i).toString() + "\n";
            }
        }
        strSpecialists += "\n";

        return "Etablissement " + this.nameBuilding + " :\n" +
                "Numéro de téléphonne : " + this.phoneNumber + "\n" + strAdresses + strEducationLevel + strSpecialists;

    }
}
