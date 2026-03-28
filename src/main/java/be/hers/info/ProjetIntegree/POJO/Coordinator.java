package be.hers.info.ProjetIntegree.POJO;

import java.util.List;

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
     * The numCoordinator and isAdmin must be initialised with a setter
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     */
    public Coordinator(String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address) {
        super(lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.isAdmin = false;

    }

    /**
     * Create a resa coordinator without his numCoordinator.
     * The numCoordinator and isAdmin must be initialised with a setter
     * @param numInterpreter ID of the interpreter
     * @param lastName the last name of the interpreter
     * @param firstName the first name of the interpreter
     * @param email the email of the interpreter
     * @param phoneNumber the phone number of the interpreter
     * @param weeklyWorkHours the number of hours worked over the week
     * @param address the address of the interpreter
     */
    public Coordinator(int numInterpreter,String lastName, String firstName, String email, String phoneNumber, int weeklyWorkHours, Address address) {
        super(numInterpreter,lastName,firstName,email,phoneNumber,weeklyWorkHours,address);
        this.isAdmin = false;

    }
    /**
     * Create a coordinator with his list of Appointment, ProfessionalSkill, AcademicSkill and Beneficiary without his numCoordinator
     * The numCoordinator and isAdmin must be initialised with a setter
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
     */
    public Coordinator(int numInterpreter, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList ){
        super(numInterpreter, lastName,firstName,email,phoneNumber,weeklyWorkHours,address,absences, appointmentsList, professionalSkillsList,academicSkillsList,beneficiariesList);
        this.isAdmin = false;
    }



    /**
     * Create a complete coordinator.
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
     * @param numCoordinator the numero of the coordinator
     * @param isAdmin If isAdmin is true, the coordinator will be the main coordinator. false if it's a resa
     */
    public Coordinator(int numInterpreter, String lastName, String firstName, String email, String phoneNumber,
                       int weeklyWorkHours, Address address, List<Absence> absences,
                       List<Appointment> appointmentsList, List<ProfessionalSkill> professionalSkillsList,
                       List<AcademicSkill> academicSkillsList, List<Beneficiary> beneficiariesList,int numCoordinator,boolean isAdmin ){
        super(numInterpreter, lastName,firstName,email,phoneNumber,weeklyWorkHours,address,absences, appointmentsList, professionalSkillsList,academicSkillsList,beneficiariesList);
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
        this.numCoordinator=numCoordinator;
    }
    /**
     *
     * @return a string that contains the interpreter and a textual interpretation of isAdmin.
     */
    @Override
    public String toString() {
        return "NumCoordinator : " + numCoordinator + (isAdmin ? " Coordinatrice/Coordinateur principale" : " Resa") + "\n" + super.toString();
    }
}
