package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wellinger Chloé
 * @reviewer Nicolas Jean-François, Halet Louis
 */

public class Interpreter {
    private int numInterpreter;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;
    private int weeklyWorkHours;
    private Address address;
    private List<Absence> absences;
    private List<Appointment> appointmentsList;
    private List<ProfessionalSkill> professionalSkillsList;
    private List<AcademicSkill> academicSkillsList;
    private List<Beneficiary> beneficiariesList;

    /**
     * Default Constructor
     */
    public Interpreter() {
        this.lastName = "";
        this.firstName = "";
        this.email = "";
        this.phoneNumber = "";
        this.weeklyWorkHours = 0;
        this.address = new Address();
        this.absences = new ArrayList<>();
        this.appointmentsList = new ArrayList<>();
        this.professionalSkillsList = new ArrayList<>();
        this.academicSkillsList = new ArrayList<>();
        this.beneficiariesList = new ArrayList<>();
    }

    /**
     * Construct an interpreter with several attributes without ID
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     * @throws IllegalArgumentException if address is null / empty and if weeklyWorkHours is negative
     */
    public Interpreter(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address) {
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("[POJOInterpreter] Les heures prestées de la semaine ne peuvent pas être négative.");
        if (address == null)
            throw new IllegalArgumentException("[POJOInterpreter] L'adresse de l'interpète ne peut pas être vide ou null");

        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
        this.address = address;
        this.absences = new ArrayList<>();
        this.appointmentsList = new ArrayList<>();
        this.professionalSkillsList = new ArrayList<>();
        this.academicSkillsList = new ArrayList<>();
        this.beneficiariesList = new ArrayList<>();
    }

    /**
     * Construct an interpreter with several attributes and the ID
     * @param numInterpreter ID of the interpreter
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     * @throws IllegalArgumentException if numInterpreter is negative
     *                                  if weeklyWorkHours is negative
     *                                  if address is null / empty
     */
    public Interpreter(int numInterpreter, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address) {
        if (numInterpreter < 0)
            throw new IllegalArgumentException("[POJOInterpreter] L'identifiant de l'interprète ne peut pas être négatif");
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("[POJOInterpreter] Les heures prestées de la semaine ne peuvent pas être négative.");
        if (address == null)
            throw new IllegalArgumentException("[POJOInterpreter] L'adresse de l'interpète ne peut pas être vide ou null");

        this.numInterpreter = numInterpreter;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
        this.address = address;
        this.absences = new ArrayList<>();
        this.appointmentsList = new ArrayList<>();
        this.professionalSkillsList = new ArrayList<>();
        this.academicSkillsList = new ArrayList<>();
        this.beneficiariesList = new ArrayList<>();
    }

    /**
     * Construct an interpreter with all attributes and ID
     * @param numInterpreter ID of the interpreter
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     * @param absences the absence's list of the interpreter
     * @param appointmentsList the appointment's list of the interpreter
     * @param professionalSkillsList the professional skill's of the interpreter
     * @param academicSkillsList the academic skill's of the interpreter
     * @param beneficiariesList the beneficiaries's list of the interpreter
     * @throws IllegalArgumentException if numInterpreter is negative
     *                                  if weeklyWorkHours is negative
     *                                  if address is null / empty
     */
    public Interpreter(int numInterpreter, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList) {
        if (numInterpreter < 0)
            throw new IllegalArgumentException("[POJOInterpreter] L'identifiant de l'interprète ne peut pas être négatif");
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("[POJOInterpreter] Les heures prestées de la semaine ne peuvent pas être négative.");
        if (address == null)
            throw new IllegalArgumentException("[POJOInterpreter] L'adresse de l'interpète ne peut pas être vide ou null");

        this.numInterpreter = numInterpreter;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
        this.address = address;
        this.absences = absences;
        this.professionalSkillsList = professionalSkillsList;
        this.appointmentsList = appointmentsList;
        this.academicSkillsList = academicSkillsList;
        this.beneficiariesList = beneficiariesList;
    }

    /**
     * Construct an interpreter with all attributes without ID
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     * @param absences the absence's list of the interpreter
     * @param appointmentsList the appointment's list of the interpreter
     * @param professionalSkillsList the professional skill's of the interpreter
     * @param academicSkillsList the academic skill's of the interpreter
     * @param beneficiariesList the beneficiaries's list of the interpreter
     * @throws IllegalArgumentException if weeklyWorkHours is negative
     *                                  if address is null / empty
     */
    public Interpreter(String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList) {
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("[POJOInterpreter] Les heures prestées de la semaine ne peuvent pas être négative.");
        if (address == null)
            throw new IllegalArgumentException("[POJOInterpreter] L'adresse de l'interpète ne peut pas être vide ou null");

        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
        this.address = address;
        this.absences = absences;
        this.professionalSkillsList = professionalSkillsList;
        this.appointmentsList = appointmentsList;
        this.academicSkillsList = academicSkillsList;
        this.beneficiariesList = beneficiariesList;
    }

    /**
     * @return the identifiant of the interpreter
     */
    public int getNumInterpreter() {
        return numInterpreter;
    }

    /**
     * @return the first name of the interpreter
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the last name of the interpreter
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the email of the interpreter
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the phone number of the interpreter
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the address of the interpreter
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @return The interpreter’s appointment list
     */
    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
    }

    /**
     * @return The list of the interpreter’s professional skills
     */
    public List<ProfessionalSkill> getProfessionalSkillsList() {
        return professionalSkillsList;
    }

    /**
     * @return The list of the interpreter’s academic skills
     */
    public List<AcademicSkill> getAcademicSkillsList() {
        return academicSkillsList;
    }

    /**
     * @return The list of beneficiaries referred to by the interpreter
     */
    public List<Beneficiary> getBeneficiariesList() {
        return beneficiariesList;
    }

    /**
     * @return The list of absences
     */
    public List<Absence> getAbsences() {
        return absences;
    }

    /**
     * @return The number of hours worked over the week
     */
    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    /**
     * Set the identifiant of the interpreter
     * @param numInterpreter The identifiant to set
     */
    public void setNumInterpreter(int numInterpreter) {
        if (numInterpreter < 0)
            throw new IllegalArgumentException("[POJOInterpreter] L'identifiant de l'interprète ne peut pas être négatif");
        this.numInterpreter = numInterpreter;
    }

    /**
     * Set the last name of the interpreter
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the first name of the interpreter
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the email of the interpreter
     * @param email The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the phone number of the interpreter
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set the number of hours worked this week
     * @param weeklyWorkHours The number of hours worked this week
     */
    public void setWeeklyWorkHours(int weeklyWorkHours) {
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("weeklyWorkHours cannot be negative");
        this.weeklyWorkHours = weeklyWorkHours;
    }

    /**
     * Set the address of the interpreter
     * @param address The address to set
     * @throws IllegalArgumentException if address is null or empty
     */
    public void setAddress(Address address) {
        if (address == null)
            throw new IllegalArgumentException("[POJOInterpreter] L'adresse de l'interpète ne peut pas être vide ou null");
        this.address = address;
    }

    /**
     * Set the absence's list for the interpreter
     * @param absences The list to set
     */
    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    /**
     * Set the appointment's list for the interpreter
     * @param appointmentsList The list to set
     */
    public void setAppointmentsList(List<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    /**
     * Set the professional skill's list for the interpreter
     * @param professionalSkillsList The list to set
     */
    public void setProfessionalSkillsList(List<ProfessionalSkill> professionalSkillsList) {
        this.professionalSkillsList = professionalSkillsList;
    }

    /**
     * Set the academic skill's list for the interpreter
     * @param academicSkillsList The list to set
     */
    public void setAcademicSkillsList(List<AcademicSkill> academicSkillsList) {
        this.academicSkillsList = academicSkillsList;
    }

    /**
     *  Set the beneficiaries's list for the interpreter
     * @param beneficiariesList The list to set
     */
    public void setBeneficiariesList(List<Beneficiary> beneficiariesList) {
        this.beneficiariesList = beneficiariesList;
    }

    /**
     * @return a String containing the interpreter ID, last name, first name, email,
     *         phone number, weekly work hours, address, list of absences, list of appointment,
     *         list of professional skill, list of academic skill and list of beneficiaries
     */
    @Override
    public String toString() {
        return "Interpreter" +
                "\nNumero ID: " + numInterpreter +
                "\nNom : " + lastName +
                "\nPrenom : " + firstName +
                "\nMail : " + email +
                "\nNuméro Téléphone : " + phoneNumber +
                "\nNombres d'heures prestées cette semaine : " + weeklyWorkHours +
                "\nAdresse : " + address +
                "\nListes d'absences : " + absences +
                "\nListes de rendez vous : " + appointmentsList +
                "\nListe de compétences métiers : " + professionalSkillsList +
                "\nListe de compétences académiques : " + academicSkillsList +
                "\nListe des bénéficiaires référents : " + beneficiariesList;
    }
}
