package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Beneficiary {
    private int beneficiaryID;
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
    private Address address;
    private int hourQuota;
    private int educationLevel;
    private List<String> communicationLanguage;
    private List<Appointment> appointmentList;

    // Not present in the class diagram, but it might be easier to add this attribute ?
    private Interpreter referenceInterpreter;

    private static final int DEFAULT_HOUR_QUOTA = 12;


    /**
     * Initialize a Beneficiary with no elements
     * beneficiaryID is set to 0 by default
     */
    public Beneficiary() {
        this.beneficiaryID = 0;
        this.name = null;
        this.surname = null;
        this.phoneNumber = null;
        this.emailAddress = null;
        this.address = null;
        this.hourQuota = DEFAULT_HOUR_QUOTA;
        this.educationLevel = 0;
        this.communicationLanguage = new ArrayList<String>();
        this.appointmentList = new ArrayList<Appointment>();
        this.referenceInterpreter = null;
    }

    /**
     * Initialize a Beneficiary with beneficiaryID, emailAddress, name and surname
     * @param beneficiaryID the id of the Beneficiary
     * @param emailAddress the email address of the Beneficiary
     * @param name the name of the Beneficiary
     * @param surname the surname of the Beneficiary
     * @throws NullPointerException if emailAddress, name or surname is null
     */
    public Beneficiary(int beneficiaryID, String emailAddress, String name, String surname) {
        if(emailAddress == null || name == null || surname == null) {
            throw new NullPointerException("L'adresse email, le nom et le prénom ne peuvent pas être nuls");
        }

        this.beneficiaryID = beneficiaryID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = null;
        this.address = null;
        this.hourQuota = DEFAULT_HOUR_QUOTA;
        this.educationLevel = 0;
        this.appointmentList = new ArrayList<Appointment>();
        this.communicationLanguage = new ArrayList<String>();
        this.referenceInterpreter = null;
    }

    /**
     * Initialize a Beneficiary with beneficiaryID, name, surname, phoneNumber, emailAddress,
     * address, educationLevel, communicationLanguage, appointmentList and referenceInterpreter
     * @param beneficiaryID the id of the Beneficiary
     * @param name the name
     * @param surname the surname
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param address the address
     * @param educationLevel the level of education
     * @param communicationLanguage the list of communication languages used by the beneficiary
     * @param appointmentList the list of Appointments, can be null
     * @param referenceInterpreter the Interpreter of reference
     * @throws NullPointerException if the name, surname, phoneNumber, emailAddress, address, referenceInterpreter or communicationLanguage is null
     * @throws IllegalArgumentException if communicationLanguage is empty
     */
    public Beneficiary(int beneficiaryID, String name, String surname, String phoneNumber, String emailAddress, Address address, int educationLevel,
                       List<String> communicationLanguage, List<Appointment> appointmentList, Interpreter referenceInterpreter) {

        if(name == null || surname == null || phoneNumber == null || emailAddress == null || address == null || communicationLanguage == null ||
        referenceInterpreter == null) {
            throw new NullPointerException("Le nom, prénom, numéro de téléphone, adresse email, adresse, interprète de référence et langue(s) de communication ne peuvent pas être nuls");
        }

        if(communicationLanguage.size() == 0) {
            throw new IllegalArgumentException("La liste des langues de communication ne peut pas être vide");
        }

        this.beneficiaryID = beneficiaryID;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.educationLevel = educationLevel;
        this.hourQuota = DEFAULT_HOUR_QUOTA;
        this.communicationLanguage = communicationLanguage;

        if(appointmentList == null) {
            this.appointmentList = new ArrayList<Appointment>();
        } else {
            this.appointmentList = appointmentList;
        }

        this.referenceInterpreter = referenceInterpreter;
    }

    /**
     * @return the id of the Beneficiary
     */
    public int getBeneficiaryID() {
        return beneficiaryID;
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
     * @return the email address
     */
    public String getEmailAddress() {

        return emailAddress;
    }

    /**
     * @return the address
     */
    public Address getAddress() {

        return address;
    }

    /**
     * @return the number of quota hours
     */
    public int getHourQuota() {

        return hourQuota;
    }

    /**
     * @return the education level
     */
    public int getEducationLevel() {

        return educationLevel;
    }

    /**
     * @return the communication languages
     */
    public List<String> getCommunicationLanguage() {

        return communicationLanguage;
    }

    /**
     * @return the Interpreter of reference
     */
    public Interpreter getReferenceInterpreter() {

        return referenceInterpreter;
    }

    /**
     * @param appointment the appointment to search for
     * @return the appointment searched for
     * @throws NullPointerException if appointment is null
     * @throws IllegalArgumentException if appointment not found
     */
    public Appointment getAppointment(Appointment appointment) {
        if(appointment == null) {
            throw new NullPointerException("Le rendez-vous ne peut pas être nul");
        }

        int index = appointmentList.indexOf(appointment);
        if(index != -1) {
            return appointmentList.get(index);
        }

        throw new IllegalArgumentException("Le rendez-vous n'a pas été trouvé");
    }

    /**
     * @return the list of appointments
     */
    public List<Appointment> getAppointmentList() {

        return appointmentList;
    }

    /**
     * @param beneficiaryID the id to set
     * @throws IllegalArgumentException if beneficiaryID is negative
     */
    public void setBeneficiaryID(int beneficiaryID) {
        if(beneficiaryID < 0) {
            throw new IllegalArgumentException("L'identifiant du bénéficiaire ne peut pas être négatif");
        }

        this.beneficiaryID = beneficiaryID;
    }

    /**
     * @param phoneNumber the phone number to set
     * @throws NullPointerException if phoneNumber is null
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            throw new NullPointerException("Le numéro de téléphone ne peut pas être nul");
        }

        this.phoneNumber = phoneNumber;
    }

    /**
     * @param emailAddress the email address to set
     * @throws NullPointerException if emailAddress is null
     */
    public void setEmailAddress(String emailAddress) {
        if(emailAddress == null) {
            throw new NullPointerException("L'adresse email ne peut pas être nulle");
        }

        this.emailAddress = emailAddress;
    }

    /**
     * @param address the address to set
     * @throws NullPointerException if address is null
     */
    public void setAddress(Address address) {
        if(address == null) {
            throw new NullPointerException("L'adresse ne peut pas être nulle");
        }

        this.address = address;
    }

    /**
     * @param hourQuota the hour quota to set
     * @throws IllegalArgumentException if hourQuota is negative
     */
    public void setHourQuota(int hourQuota) { // TODO : maybe modify ?
        if(hourQuota < 0) {
            throw new IllegalArgumentException("Le quota d'heures ne peut pas être négatif");
        }

        this.hourQuota = hourQuota;
    }

    /**
     * @param educationLevel the level of education to set
     * @throws IllegalArgumentException if educationLevel is negative
     */
    public void setEducationLevel(int educationLevel) {
        if(educationLevel < 0) {
            throw new IllegalArgumentException("Le niveau d'éducation ne peut pas être négatif");
        }

        this.educationLevel = educationLevel;
    }

    /**
     * @param communicationLanguage the list of communication languages to set
     * @throws NullPointerException if communicationLanguages is null
     * @throws IllegalArgumentException if communicationLanguages is empty
     */
    public void setCommunicationLanguage(List<String> communicationLanguage) {
        if(communicationLanguage == null) {
            throw new NullPointerException("La liste des langues de communication ne peut pas être nulle");
        }

        if(communicationLanguage.size() == 0) {
            throw new IllegalArgumentException("La liste des langues de communication ne peut pas être vide");
        }

        this.communicationLanguage = communicationLanguage;
    }

    /**
     * @param appointmentList the list of appointments to set
     * @throws NullPointerException if appointmentList is null
     * @throws IllegalArgumentException if appointmentList is empty
     */
    public void setAppointmentList(List<Appointment> appointmentList) {
        if(appointmentList == null) {
            throw new NullPointerException("La liste des rendez-vous ne peut pas être nulle");
        }

        if(appointmentList.size() == 0) {
            throw new IllegalArgumentException("La liste des rendez-vous ne peut pas être vide");
        }

        this.appointmentList = appointmentList;
    }

    /**
     * @param name the name to set
     * @throws NullPointerException if name is null
     */
    public void setName(String name) {
        if(name == null) {
            throw new NullPointerException("Le nom ne peut pas être nul");
        }

        this.name = name;
    }

    /**
     * @param surname the surname to set
     * @throws NullPointerException if surname is null
     */
    public void setSurname(String surname) {
        if(surname == null) {
            throw new NullPointerException("Le prénom ne peut pas être nul");
        }

        this.surname = surname;
    }

    /**
     * @param interpreter the interpreter of reference to set
     * @throws NullPointerException if interpreter is null
     */
    public void setReferenceInterpreter(Interpreter interpreter) {
        if(interpreter == null) {
            throw new NullPointerException("L'interprète de référence ne peut pas être nul");
        }

        referenceInterpreter = interpreter;
    }

    /**
     * @return a String containing the beneficiary ID, name, surname, phone number,
     *         email address, address, hour quota, education level, communication languages,
     *         reference interpreter and appointments
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bénéficiaire n°").append(this.beneficiaryID).append(" :\n");
        sb.append("Nom : ").append(this.name).append("\n");
        sb.append("Prénom : ").append(this.surname).append("\n");

        if(this.phoneNumber == null) {
            sb.append("Numéro de téléphone : Non renseigné\n");
        } else {
            sb.append("Numéro de téléphone : ").append(this.phoneNumber).append("\n");
        }

        sb.append("Adresse email : ").append(this.emailAddress).append("\n");

        if(this.address == null) {
            sb.append("Adresse : Non renseignée\n");
        } else {
            sb.append("Adresse : ").append(this.address.toString()).append("\n");
        }

        sb.append("Quota d'heures : ").append(this.hourQuota).append("\n");
        sb.append("Niveau d'éducation : ").append(this.educationLevel).append("\n");

        sb.append("Langue(s) de communication :\n");
        for(int i = 0; i < this.communicationLanguage.size(); i++) {
            sb.append("- ").append(this.communicationLanguage.get(i)).append("\n");
        }

        if(this.referenceInterpreter == null) {
            sb.append("Aucun interprète de référence\n");
        } else {
            sb.append("Interprète de référence : ").append(this.referenceInterpreter.toString()).append("\n");
        }

        if(this.appointmentList.size() == 0) {
            sb.append("Aucun rendez-vous\n");
        } else {
            sb.append("Rendez-vous :\n");
            for(int i = 0; i < this.appointmentList.size(); i++) {
                sb.append("- ").append(this.appointmentList.get(i).toString()).append("\n");
            }
        }

        return sb.toString();
    }
}