package be.hers.info.ProjetIntegree.POJO;

/**
 * @author Vatafu Jean, Rosman Loïs
 * @reviewer Halet Louis
 */

import java.util.ArrayList;
import java.util.List;

public class Beneficiary extends User{
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
     */
    public Beneficiary() {
        super();
        this.hourQuota = 0;
        this.educationLevel = EDUCATION_LEVEL_MIN;
        this.interpreter = null;
        this.communicationLanguage = new ArrayList<String>();
        this.appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Initialize a Beneficiary with login, password, lastName, firstName and emailAddress
     * @param login the login of the Beneficiary
     * @param password the password of the Beneficiary
     * @param lastName the last name of the Beneficiary
     * @param firstName the first name of the Beneficiary
     * @param emailAddress the email address of the Beneficiary
     * @throws IllegalArgumentException if the password is null or empty
     */
    public Beneficiary(String login, String password, String lastName, String firstName, String emailAddress) {
        super(login, password, lastName, firstName, emailAddress);
        this.hourQuota = 0;
        this.educationLevel = EDUCATION_LEVEL_MIN;
        this.interpreter = null;
        this.communicationLanguage = new ArrayList<String>();
        this.appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Initialize a Beneficiary with login, password, lastName, firstName, phoneNumber, emailAddress,
     * address, hourQuota, educationLevel, interpreter, communicationLanguage
     * @param login The id login the beneficiary
     * @param password The password of the beneficiary
     * @param lastName the last name of the beneficiary
     * @param firstName the first name of the beneficiary
     * @param phoneNumber the phone number of the beneficiary
     * @param emailAddress the email address of the beneficiary
     * @param address the address of the beneficiary
     * @param hourQuota the quota hours of the beneficiary
     * @param educationLevel the level of education of the beneficiary
     * @param interpreter the interpreter of the beneficiary
     * @param communicationLanguage the list of communication languages used by the beneficiary
     * @throws IllegalArgumentException if the password is null or empty
     */
    public Beneficiary(String login, String password, String lastName, String firstName, String phoneNumber,
                       String emailAddress, Address address, int hourQuota, int educationLevel,
                       Interpreter interpreter, List<String> communicationLanguage) {
        super(login, password, lastName, firstName, phoneNumber, emailAddress, address);
        this.hourQuota = hourQuota;
        this.educationLevel = educationLevel;
        this.interpreter = interpreter;
        this.communicationLanguage = communicationLanguage;
        this.appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Initialize a Beneficiary with login, password, lastName, firstName, phoneNumber, emailAddress,
     * address, hourQuota, educationLevel, interpreter, communicationLanguage and appointmentList
     * @param login The id login of the beneficiary
     * @param password The password of the beneficiary
     * @param lastName the last name of the beneficiary
     * @param firstName the first name of the beneficiary
     * @param phoneNumber the phone number of the beneficiary
     * @param emailAddress the email address of the beneficiary
     * @param address the address of the beneficiary
     * @param hourQuota the quota hours of the beneficiary
     * @param educationLevel the level of education of the beneficiary
     * @param interpreter the interpreter of reference of the beneficiary
     * @param communicationLanguage the list of communication languages used by the beneficiary
     * @param appointmentList the list of Appointments of the beneficiary, can be null
     * @throws IllegalArgumentException if address, communicationLanguage, interpreter or password is null
     *                                  if communicationLanguage or password is empty
     *                                  if hourQuota is negative
     *                                  if educationLevel is smaller than EDUCATION_LEVEL_MIN or greater than EDUCATION_LEVEL_MAX
     */
    public Beneficiary(String login, String password, String lastName, String firstName, String phoneNumber,
                       String emailAddress, Address address, int hourQuota, int educationLevel, Interpreter interpreter,
                       List<String> communicationLanguage, List<Appointment> appointmentList) {

        if(communicationLanguage == null || interpreter == null) {
            throw new IllegalArgumentException("[POJOBeneficiary] L'interprète de référence et langue(s) de communication ne peuvent pas être null");
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

        super(login, password, lastName, firstName, phoneNumber, emailAddress, address);
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
     * @throws IllegalArgumentException if educationLevel is smaller than EDUCATION_LEVEL_MIN or
     * greater than EDUCATION_LEVEL_MAX
     */
    public void setEducationLevel(int educationLevel) {
        if(educationLevel < EDUCATION_LEVEL_MIN || educationLevel > EDUCATION_LEVEL_MAX) {
            throw new IllegalArgumentException("[POJOBeneficiary] Le niveau d'éducation est compris entre " +
                    EDUCATION_LEVEL_MIN + " et " + EDUCATION_LEVEL_MAX);
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
     * @return a String containing the beneficiary ID, name, surname, phone number,
     *         email address, interpreter, address, hour quota, education level, communication languages
     *         and appointments
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bénéficiaire").append(" :\n");
        sb.append(super.toString());

        if(this.interpreter == null) {
            sb.append("Interprète de référence : Non renseignée\n");
        } else {
            sb.append("Interprète de référence : ").append(this.interpreter.toString()).append("\n");
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