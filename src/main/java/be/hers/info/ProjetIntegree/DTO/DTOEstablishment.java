package be.hers.info.ProjetIntegree.DTO;

import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.POJO.Referrer;

import java.util.Iterator;
import java.util.List;

/**
 * DTO used for the page etablissements.html
 * A minimalist object of Establishment.
 */
public class DTOEstablishment {
    private String nameBuilding;
    private String phoneNumber;
    private String educationLevel;
    private String referrers;
    private String locality;
    private String address;


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
        this.nameBuilding = etablissement.getNameBuilding();
        this.phoneNumber = etablissement.getPhoneNumber();
        StringBuilder levelSchool = new StringBuilder();
        for (Integer level : etablissement.getEducationLevel()) {
            if(!levelSchool.isEmpty()){
                levelSchool.append(", ");
            }
            switch (level) {
                case 0 -> levelSchool.append("autre");
                case 1 -> levelSchool.append("maternelle");
                case 2 -> levelSchool.append("primaire");
                case 3 -> levelSchool.append("secondaire");
                case 4 -> levelSchool.append("supérieur");
            }
        }
        this.educationLevel = levelSchool.substring(0, 1).toUpperCase() + levelSchool.substring(1);
        // Premiere lettre du niveau d'éducation en majuscule.
        this.address = etablissement.getAddresses().getFirst().toStringFront();
        this.locality = etablissement.getAddresses().getFirst().getLocality();
        StringBuilder referrersSchool = new StringBuilder();
        List<Referrer> referrersLst = etablissement.getReferrers();
        Iterator<Referrer> iterator = referrersLst.iterator();
        while(iterator.hasNext()){
            Referrer referrer = iterator.next();
            referrersSchool.append(referrer.getName().toUpperCase());
            referrersSchool.append(" ");
            referrersSchool.append(referrer.getSurname());
            if(iterator.hasNext()){
                referrersSchool.append(", ");
            }
        }
        this.referrers = referrersSchool.toString();
    }

    /**
     *
     * @return the name of the building in a String.
     */
    public String getNameBuilding() {
        return nameBuilding;
    }

    /**
     *
     * @return the phone number in a String.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @return the level of education of the Establishment
     */
    public String getEducationLevel() {
        return educationLevel;
    }

    /**
     *
     * @return the name of all the referrer registered into the Establishment.
     */
    public String getReferrers() {
        return referrers;
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
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set the New Education Level for the Establishment.
     * Education level can be multiple and is separate with a ",".
     * @param educationLevel represent all the Level of Education of the Establishment (autre, primaire, secondaire, supérieur).
     */
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    /**
     * Set the Referrers for the Establishment with the given param.
     * @param referrers represent all the new referrers (can be mulitple if multiple than separate with a ",")
     */
    public void setReferrers(String referrers) {
        this.referrers = referrers;
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
}
