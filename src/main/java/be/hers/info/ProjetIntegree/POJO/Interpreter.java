package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

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
     * Construct an interpreter with several attributes
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     */
    public Interpreter(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.weeklyWorkHours = weeklyWorkHours;
        this.address = address;
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
     */
    public Interpreter(int numInterpreter, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address) {
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
     * @return The absence of the interpreter
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
        this.weeklyWorkHours = weeklyWorkHours;
    }

    /**
     * Set the address of the interpreter
     * @param address The address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }
}
