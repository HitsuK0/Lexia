package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Wellinger Chloé, Rosman Loïs
 * @reviewer Nicolas Jean-François, Halet Louis
 */

public class Interpreter extends User{
    private String login;
    private String password;
    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String emailAddress;
    private Address address;
    private int weeklyWorkHours;
    private List<Absence> absences;
    private List<Appointment> appointmentsList;
    private List<ProfessionalSkill> professionalSkillsList;
    private List<AcademicSkill> academicSkillsList;
    private List<Beneficiary> beneficiariesList;

    /**
     * Default Constructor
     */
    public Interpreter() {
        super();
        this.weeklyWorkHours = 0;
        this.absences = new ArrayList<>();
        this.appointmentsList = new ArrayList<>();
        this.professionalSkillsList = new ArrayList<>();
        this.academicSkillsList = new ArrayList<>();
        this.beneficiariesList = new ArrayList<>();
    }

    /**
     * Construct an interpreter with several attributes
     * @param login the login so that the interpreter can log in
     * @param password the interpreter’s password
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param emailAddress the email address of the interpreter
     * @param address the address of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @throws IllegalArgumentException if weeklyWorkHours is negative
     *                                  if address is null
     *                                  if password is null or empty
     */
    public Interpreter(String login, String password, String lastName, String firstName, String phoneNumber,
                       String emailAddress, int weeklyWorkHours, Address address) {
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("[POJOInterpreter] Les heures prestées de la semaine ne peuvent pas être négative.");

        super(login, password, lastName, firstName, phoneNumber, emailAddress, address);
        this.weeklyWorkHours = weeklyWorkHours;
        this.absences = new ArrayList<>();
        this.appointmentsList = new ArrayList<>();
        this.professionalSkillsList = new ArrayList<>();
        this.academicSkillsList = new ArrayList<>();
        this.beneficiariesList = new ArrayList<>();
    }

    /**
     * Construct an interpreter with all attributes
     * @param login the login so that the interpreter can log in
     * @param password the interpreter’s password
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param emailAddress the email address of the interpreter
     * @param address the address of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param absences the absence's list of the interpreter
     * @param appointmentsList the appointment's list of the interpreter
     * @param professionalSkillsList the professional skill's of the interpreter
     * @param academicSkillsList the academic skill's of the interpreter
     * @param beneficiariesList the beneficiaries's list of the interpreter
     * @throws IllegalArgumentException if weeklyWorkHours is negative
     *                                  if address is null
     *                                  if password is null or empty
     */
    public Interpreter(String login, String password, String lastName, String firstName, String phoneNumber,
                       String emailAddress, Address address, int weeklyWorkHours, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList) {
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("[POJOInterpreter] Les heures prestées de la semaine ne peuvent pas être négative.");

        super(login, password, lastName, firstName, phoneNumber, emailAddress, address);
        this.weeklyWorkHours = weeklyWorkHours;
        this.absences = absences;
        this.professionalSkillsList = professionalSkillsList;
        this.appointmentsList = appointmentsList;
        this.academicSkillsList = academicSkillsList;
        this.beneficiariesList = beneficiariesList;
    }

    /**
     * @return The number of hours worked over the week
     */
    public int getWeeklyWorkHours() {
        return weeklyWorkHours;
    }

    /**
     * @return The list of absences
     */
    public List<Absence> getAbsences() {
        return absences;
    }

    /**
     * @return The list of the interpreter’s professional skills
     */
    public List<ProfessionalSkill> getProfessionalSkillsList() {
        return professionalSkillsList;
    }

    /**
     * @return The interpreter’s appointment list
     */
    public List<Appointment> getAppointmentsList() {
        return appointmentsList;
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
     * Set the number of hours worked this week
     * @param weeklyWorkHours The number of hours worked this week
     */
    public void setWeeklyWorkHours(int weeklyWorkHours) {
        if (weeklyWorkHours < 0)
            throw new IllegalArgumentException("weeklyWorkHours cannot be negative");
        this.weeklyWorkHours = weeklyWorkHours;
    }

    /**
     * Set the absence's list for the interpreter
     * @param absences The list to set
     */
    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    /**
     * Set the professional skill's list for the interpreter
     * @param professionalSkillsList The list to set
     */
    public void setProfessionalSkillsList(List<ProfessionalSkill> professionalSkillsList) {
        this.professionalSkillsList = professionalSkillsList;
    }

    /**
     * Set the appointment's list for the interpreter
     * @param appointmentsList The list to set
     */
    public void setAppointmentsList(List<Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
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
     * @return a String containing the interpreter login, password, last name, first name, phone number, email address,
     *         address, weekly work hours, list of absences, list of professional skill, list of appointment,
     *         list of academic skill and list of beneficiaries
     */
    @Override
    public String toString() {
        return "Interpreter" +
                "\n" + super() +
                "\nNombres d'heures prestées cette semaine : " + weeklyWorkHours +
                "\nListes d'absences : " + absences +
                "\nListes de rendez-vous : " + appointmentsList +
                "\nListe de compétences métiers : " + professionalSkillsList +
                "\nListe de compétences académiques : " + academicSkillsList +
                "\nListe des bénéficiaires référents : " + beneficiariesList;
    }
}
