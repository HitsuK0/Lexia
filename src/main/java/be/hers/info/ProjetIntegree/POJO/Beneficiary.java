package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Vatafu Jean
 * @reviewer Halet Louis
 */

import java.util.ArrayList;
import java.util.List;

public class Beneficiary {
    private int numBeneficiary;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String emailAddress;
    private Address address;
    private int hourQuota;
    private int educationLevel;
    private Interpreter interpreter;
    private List<String> communicationLanguage;
    private List<Appointment> appointmentList;

    /** Minimum education level (does not concern a school) */
    private static final int EDUCATION_LEVEL_MIN = 0;

    /** Maximum education level (4 = higher education) */
    private static final int EDUCATION_LEVEL_MAX = 4;

    /**
     * Initialize a Beneficiary with no elements
     * The parameter numBeneficiary can only be initialized with setNumBeneficiary
     */
    public Beneficiary() {
        this.login = "";
        this.password = "";
        this.name = "";
        this.surname = "";
        this.phoneNumber = "";
        this.emailAddress = "";
        this.address = null;
        this.hourQuota = 0;
        this.educationLevel = EDUCATION_LEVEL_MIN;
        this.interpreter = null;
        this.communicationLanguage = new ArrayList<String>();
        this.appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Initialize a Beneficiary with numBeneficiary, name, surname, phoneNumber, emailAddress,
     * address, educationLevel, communicationLanguage and appointmentList
     * @param numBeneficiary the id of the Beneficiary
     * @param login The id login the user
     * @param password The password of the user
     * @param name the name
     * @param surname the surname
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param hourQuota the quota hours
     * @param educationLevel the level of education
     * @param communicationLanguage the list of communication languages used by the beneficiary
     */
    public Beneficiary(int numBeneficiary, String login, String password, String name, String surname, String phoneNumber, Address address, int hourQuota, String emailAddress, int educationLevel,
                       Interpreter interpreter, List<String> communicationLanguage) {
        this.numBeneficiary = numBeneficiary;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.educationLevel = educationLevel;
        this.address = address;
        this.hourQuota = hourQuota;
        this.interpreter = interpreter;
        this.communicationLanguage = communicationLanguage;
        this.appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Initialize a Beneficiary with emailAddress, name and surname.
     * The parameter numBeneficiary can only be initialized with setNumBeneficiary
     * @param login the login of the user
     * @param password the password of the user
     * @param emailAddress the email address of the Beneficiary
     * @param name the name of the Beneficiary
     * @param surname the surname of the Beneficiary
     */
    public Beneficiary(String login, String password, String emailAddress, String name, String surname) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = "";
        this.emailAddress = emailAddress;
        this.address = null;
        this.hourQuota = 0;
        this.educationLevel = EDUCATION_LEVEL_MIN;
        this.interpreter = null;
        this.communicationLanguage = new ArrayList<String>();
        this.appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Initialize a Beneficiary with numBeneficiary, emailAddress, name and surname
     * @param numBeneficiary the id of the Beneficiary
     * @param login the login of the user
     * @param password the password of the user
     * @param emailAddress the email address of the Beneficiary
     * @param name the name of the Beneficiary
     * @param surname the surname of the Beneficiary
     */
    public Beneficiary(int numBeneficiary, String login, String password, String emailAddress, String name, String surname) {
        this.numBeneficiary = numBeneficiary;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = "";
        this.address = null;
        this.hourQuota = 0;
        this.educationLevel = EDUCATION_LEVEL_MIN;
        this.interpreter = null;
        this.appointmentList = new ArrayList<Appointment>();
        this.communicationLanguage = new ArrayList<String>();
    }

    /**
     * Initialize a Beneficiary with numBeneficiary, name, surname, phoneNumber, emailAddress,
     * address, educationLevel, communicationLanguage and appointmentList
     * @param numBeneficiary the id of the Beneficiary
     * @param login The id login the user
     * @param password The password of the user
     * @param name the name
     * @param surname the surname
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param address the address
     * @param hourQuota the quota hours
     * @param educationLevel the level of education
     * @param interpreter the interpreter of reference
     * @param communicationLanguage the list of communication languages used by the beneficiary
     * @param appointmentList the list of Appointments, can be null
     * @throws IllegalArgumentException if address or communicationLanguage is null
     *                                  if communicationLanguage is empty
     *                                  if hourQuota is negative
     *                                  if educationLevel is smaller than EDUCATION_LEVEL_MIN or greater than EDUCATION_LEVEL_MAX
     */
    public Beneficiary(int numBeneficiary, String login, String password, String name, String surname, String phoneNumber, int hourQuota, String emailAddress, Address address, int educationLevel,
                       Interpreter interpreter, List<String> communicationLanguage, List<Appointment> appointmentList) {

        if(address == null || communicationLanguage == null || interpreter == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] L'adresse, l'interprète de référence et langue(s) de communication ne peuvent pas être null");
        }

        if(communicationLanguage.isEmpty()) {
            throw new IllegalArgumentException("[POJOBeneficiary] La liste des langues de communication ne peut pas être vide");
        }

        if(hourQuota < 0) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le quota d'heures ne peut pas être négatif");
        }

        if(educationLevel < EDUCATION_LEVEL_MIN || educationLevel > EDUCATION_LEVEL_MAX) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le niveau d'éducation doit être compris entre "+EDUCATION_LEVEL_MIN+" et "+EDUCATION_LEVEL_MAX);
        }

        this.numBeneficiary = numBeneficiary;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.educationLevel = educationLevel;
        this.hourQuota = hourQuota;
        this.interpreter = interpreter;
        this.communicationLanguage = communicationLanguage;

        if(appointmentList == null) {
            this.appointmentList = new ArrayList<Appointment>();
        } else {
            this.appointmentList = appointmentList;
        }
    }

    /**
     * Initialize a Beneficiary with numBeneficiary, name, surname, phoneNumber, emailAddress,
     * address, educationLevel, communicationLanguage and appointmentList.
     * The parameter numBeneficiary can only be initialized with setNumBeneficiary
     * @param login The login of the user
     * @param password The password of the user
     * @param name the name
     * @param surname the surname
     * @param phoneNumber the phone number
     * @param emailAddress the email address
     * @param address the address
     * @param hourQuota the quota hours
     * @param educationLevel the level of education
     * @param interpreter the interpreter of reference
     * @param communicationLanguage the list of communication languages used by the beneficiary
     * @param appointmentList the list of Appointments, can be null
     * @throws IllegalArgumentException if address or communicationLanguage is null
     *                                  if communicationLanguage is empty
     *                                  if hourQuota is negative
     *                                  if educationLevel is smaller than EDUCATION_LEVEL_MIN or greater than EDUCATION_LEVEL_MAX
     */
    public Beneficiary(String login, String password, String name, String surname, String phoneNumber, int hourQuota, String emailAddress, Address address, int educationLevel,
                       Interpreter interpreter, List<String> communicationLanguage, List<Appointment> appointmentList) {

        if(address == null || communicationLanguage == null || interpreter == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] L'adresse, l'interprète de référence et langue(s) de communication ne peuvent pas être null");
        }

        if(communicationLanguage.isEmpty()) {
            throw new IllegalArgumentException("[POJOBeneficiary] La liste des langues de communication ne peut pas être vide");
        }

        if(hourQuota < 0) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le quota d'heures ne peut pas être négatif");
        }

        if(educationLevel < EDUCATION_LEVEL_MIN || educationLevel > EDUCATION_LEVEL_MAX) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le niveau d'éducation doit être compris entre "+EDUCATION_LEVEL_MIN+" et "+EDUCATION_LEVEL_MAX);
        }

        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.educationLevel = educationLevel;
        this.hourQuota = hourQuota;
        this.interpreter = interpreter;
        this.communicationLanguage = communicationLanguage;

        if(appointmentList == null) {
            this.appointmentList = new ArrayList<Appointment>();
        } else {
            this.appointmentList = appointmentList;
        }
    }

    /**
     * @return the id of the Beneficiary
     */
    public int getNumBeneficiary() {

        return numBeneficiary;
    }

    /**
     * @return the login of the Beneficiary
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return the password of the Beneficiary
     */
    public String getPassword() {
        return password;
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
     * @return the interpreter
     */
    public Interpreter getInterpreter() {

        return interpreter;
    }

    /**
     * @return the communication languages
     */
    public List<String> getCommunicationLanguage() {

        return communicationLanguage;
    }

    /**
     * @param appointment the appointment to search for
     * @return the appointment searched for
     * @throws IllegalArgumentException if appointment is null
     *                                  if appointment not found
     */
    public Appointment getAppointment(Appointment appointment) {
        if(appointment == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le rendez-vous ne peut pas être nul");
        }

        int index = appointmentList.indexOf(appointment);
        if(index != -1) {
            return appointmentList.get(index);
        }

        throw new IllegalArgumentException("[POJOBeneficiary] Le rendez-vous n'a pas été trouvé");
    }

    /**
     * @return the list of appointments
     */
    public List<Appointment> getAppointmentList() {

        return appointmentList;
    }

    /**
     * @param numBeneficiary the id to set
     */
    public void setNumBeneficiary(int numBeneficiary) {

        this.numBeneficiary = numBeneficiary;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {

        this.login = login;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {

        this.password = password;
    }

    /**
     * @param phoneNumber the phone number to set
     * @throws IllegalArgumentException if phoneNumber is null
     */
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le numéro de téléphone ne peut pas être vide");
        }

        this.phoneNumber = phoneNumber;
    }

    /**
     * @param emailAddress the email address to set
     * @throws IllegalArgumentException if emailAddress is null
     */
    public void setEmailAddress(String emailAddress) {
        if(emailAddress == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] L'adresse email ne peut pas être vide");
        }

        this.emailAddress = emailAddress;
    }

    /**
     * @param address the address to set
     * @throws IllegalArgumentException if address is null
     */
    public void setAddress(Address address) {
        if(address == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] L'adresse ne peut pas être nulle");
        }

        this.address = address;
    }

    /**
     * @param hourQuota the hour quota to set
     * @throws IllegalArgumentException if hourQuota is negative
     */
    public void setHourQuota(int hourQuota) {
        if(hourQuota < 0) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le quota d'heures ne peut pas être négatif");
        }

        this.hourQuota = hourQuota;
    }

    /**
     * @param educationLevel the level of education to set
     * @throws IllegalArgumentException if educationLevel is smaller than EDUCATION_LEVEL_MIN or greater than EDUCATION_LEVEL_MAX
     */
    public void setEducationLevel(int educationLevel) {
        if(educationLevel < EDUCATION_LEVEL_MIN || educationLevel > EDUCATION_LEVEL_MAX) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le niveau d'éducation est compris entre "+EDUCATION_LEVEL_MIN+" et "+EDUCATION_LEVEL_MAX);
        }

        this.educationLevel = educationLevel;
    }

    /**
     * @param interpreter the interpreter of reference
     * @throws IllegalArgumentException if interpreter is null
     */
    public void setInterpreter(Interpreter interpreter) {
        if(interpreter == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] L'interprète de référence ne peut pas être null");
        }

        this.interpreter = interpreter;
    }

    /**
     * @param communicationLanguage the list of communication languages to set
     * @throws IllegalArgumentException if communicationLanguages is null
     *                                  if communicationLanguages is empty
     */
    public void setCommunicationLanguage(List<String> communicationLanguage) {
        if(communicationLanguage == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] La liste des langues de communication ne peut pas être nulle");
        }

        if(communicationLanguage.isEmpty()) {
            throw new IllegalArgumentException("[POJOBeneficiary] La liste des langues de communication ne peut pas être vide");
        }

        this.communicationLanguage = communicationLanguage;
    }

    /**
     * @param appointmentList the list of appointments to set
     * @throws IllegalArgumentException if appointmentList is null
     *                                  if appointmentList is empty
     */
    public void setAppointmentList(List<Appointment> appointmentList) {
        if(appointmentList == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] La liste des rendez-vous ne peut pas être nulle");
        }

        if(appointmentList.isEmpty()) {
            throw new IllegalArgumentException("[POJOBeneficiary] La liste des rendez-vous ne peut pas être vide");
        }

        this.appointmentList = appointmentList;
    }

    /**
     * @param name the name to set
     * @throws IllegalArgumentException if name is null
     */
    public void setName(String name) {
        if(name == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le nom ne peut pas être nul");
        }

        this.name = name;
    }

    /**
     * @param surname the surname to set
     * @throws IllegalArgumentException if surname is null
     */
    public void setSurname(String surname) {
        if(surname == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le prénom ne peut pas être nul");
        }

        this.surname = surname;
    }

    /**
     * @return a String containing the beneficiary ID, name, surname, phone number,
     *         email address, interpreter, address, hour quota, education level, communication languages
     *         and appointments
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bénéficiaire n°").append(this.numBeneficiary).append(" :\n");
        sb.append("Login : ").append(this.login).append("\n");
        sb.append("Password : ").append(this.password).append("\n");
        sb.append("Nom : ").append(this.name).append("\n");
        sb.append("Prénom : ").append(this.surname).append("\n");

        if(phoneNumber.isEmpty()) {
            sb.append("Numéro de téléphone : Non renseigné\n");
        } else {
            sb.append("Numéro de téléphone : ").append(this.phoneNumber).append("\n");
        }

        if(emailAddress.isEmpty()) {
            sb.append("Adresse email : Non renseigné\n");
        } else {
            sb.append("Adresse email : ").append(this.emailAddress).append("\n");
        }

        if(this.interpreter == null) {
            sb.append("Interprète de référence : Non renseignée\n");
        } else {
            sb.append("Interprète de référence : ").append(this.interpreter.toString()).append("\n");
        }

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

        if(this.appointmentList.isEmpty()) {
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