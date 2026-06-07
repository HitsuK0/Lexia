package be.hers.info.ProjetIntegree.POJO;

import be.hers.info.ProjetIntegree.DTO.DTOUserAdd;

import java.util.List;

/**
    @author Halet Louis
    @reviewer Nicolas Jean-Francois
*/

public class Coordinator extends Interpreter {
    private int numCoordinator;
    private boolean isAdmin;

    /**
     * Create a Coordinator object. It uses the default constructor in Interpreter
     */
    public Coordinator() {
        super();
        this.isAdmin = false;
    }

    /**
     * Create a resa coordinator without his numCoordinator. There isn't the numInterpreter.
     * @param dtoUserAdd The user to add
     * @param address The address of the user
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public Coordinator(DTOUserAdd dtoUserAdd, Address address, boolean isAdmin) {
        super(dtoUserAdd, address);
        this.isAdmin = isAdmin;
    }

    /**
     * Create a resa coordinator without his numCoordinator. There isn't the numInterpreter.
     * The numCoordinator and isAdmin must be initialised with a setter
     * @param login the login so that the interpreter can log in
     * @param password the interpreter’s hashed password
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     */
    public Coordinator(String login, String password, String lastName, String firstName, String phoneNumber, String email, int weeklyWorkHours, Address address) {
        super(login,password,lastName,firstName, phoneNumber, email,weeklyWorkHours,address);
        this.isAdmin = false;
    }

    /**
     * Create a resa coordinator without his numCoordinator.
     * The numCoordinator and isAdmin must be initialised with a setter
     * @param numInterpreter ID of the interpreter
     * @param login the login so that the interpreter can log in
     * @param password the interpreter’s hashed password
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param email the email of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     */
    public Coordinator(int numInterpreter,String login, String password, String lastName, String firstName, String phoneNumber, String email,
                       int weeklyWorkHours, Address address) {
        super(numInterpreter, login, password,lastName,firstName, phoneNumber, email, weeklyWorkHours,address);
        this.isAdmin = false;
    }
    /**
     * Create a coordinator with his list of Appointment, ProfessionalSkill, AcademicSkill and Beneficiary without his numCoordinator
     * The numCoordinator and isAdmin must be initialised with a setter
     * @param numInterpreter ID of the interpreter
     * @param login the login so that the interpreter can log in
     * @param password the interpreter’s hashed password
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
     */
    public Coordinator(int numInterpreter,String login, String password, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList ){
        super(numInterpreter, login, password,lastName,firstName,phoneNumber,email,address,weeklyWorkHours,absences, appointmentsList, professionalSkillsList,academicSkillsList,beneficiariesList);
        this.isAdmin = false;
    }

    /**
     * Create a complete coordinator.
     * @param numInterpreter ID of the interpreter
     * @param login the login so that the interpreter can log in
     * @param password the interpreter’s hashed password
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
     * @param numCoordinator the numero of the coordinator
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public Coordinator(int numInterpreter,String login, String password, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList,int numCoordinator,boolean isAdmin ){
        super(numInterpreter, login, password,lastName,firstName,phoneNumber,email,address,weeklyWorkHours,absences, appointmentsList, professionalSkillsList,academicSkillsList,beneficiariesList);
        this.numCoordinator=numCoordinator;
        this.isAdmin = isAdmin;
    }
    /**
     *
     * @return If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     *
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     *
     * @return the numero of the coordinator
     */
    public int getNumCoordinator() {
        return numCoordinator;
    }

    /**
     *
     * @param numCoordinator the numero of the coordinator
     */
    public void setNumCoordinator(int numCoordinator){
        if (numCoordinator < 0)
            throw new IllegalArgumentException("[POJOCoordinator] Le numéro du coordinateur ne peut pas être négatif");
        this.numCoordinator=numCoordinator;
    }
    /**
     *
     * @return a string that contains the interpreter and a textual interpretation of isAdmin.
     */
    @Override
    public String toString() {
        return "Coordinateur/Coordinatrice" +
                "\nNumCoordinator : " + numCoordinator +
                "\nRole : " + (isAdmin ? "Coordinatrice/Coordinateur principale" : "Resa") +
                super.toString();
    }
}
