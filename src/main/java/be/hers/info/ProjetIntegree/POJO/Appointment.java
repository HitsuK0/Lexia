package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vatafu Jean
 * @reviewer Nicolas Jean-François
 */

public class Appointment {
    private int numAppointment;
    private String status;
    private List<String> appointmentLocals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlot timeSlot;
    private List<AcademicSkill> academicSkillsNeeded;
    private List<ProfessionalSkill> professionalSkillsNeeded;

    /**
     * Initialize an Appointment with no elements
     * The parameter numAppointment can only be initialized with setNumAppointment
     */
    public Appointment() {
        status = "en attente";
        appointmentLocals = new ArrayList<String>();
        timeSlot = null;
        beneficiary = null;
        academicSkillsNeeded = new ArrayList<AcademicSkill>();
        professionalSkillsNeeded = new ArrayList<ProfessionalSkill>();
        interpreters = new ArrayList<Interpreter>();
    }

    /**
     * Initialize an Appointment with beneficiary, appointmentLocals, interpreters, specialists, academicSkillsNeeded, professionalSkillsNeeded,
     * timeSlot and sets the status to 'en attente' by default
     * @param numAppointment The id of the Appointment
     * @param beneficiary The Beneficiary concerned
     * @param appointmentLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpretes that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param professionalSkillsNeeded List of business skills needed
     * @param timeSlot For every repetitive or non-repetitive Appointment
     * @throws IllegalArgumentException If beneficiary, professionalSkillsNeeded, interpreters or timeSlot is null
     *                                  If interpreters or professionalSkillsNeeded is empty
     */
    public Appointment(int numAppointment, Beneficiary beneficiary, List<String> appointmentLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> professionalSkillsNeeded
            ,TimeSlot timeSlot) {
        if(beneficiary == null || interpreters == null || professionalSkillsNeeded == null || timeSlot == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le bénéficiaire, les interprètes, la tranche horaire et les compétences professionnelles ne peuvent pas être null.");
        }

        if(interpreters.isEmpty()|| professionalSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] Les listes d'interprètes et de compétences professionnelles ne peuvent pas être vides.");
        }

        this.numAppointment = numAppointment;
        this.beneficiary = beneficiary;
        this.status = "en attente";
        this.appointmentLocals = appointmentLocals;
        this.interpreters = interpreters;
        this.timeSlot = timeSlot;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.professionalSkillsNeeded = professionalSkillsNeeded;
    }

    /**
     * Initialize an Appointment with beneficiary, appointmentLocals, interpreters, specialists, academicSkillsNeeded, professionalSkillsNeeded,
     * timeSlot and sets the status to 'en attente' by default
     * @param beneficiary The Beneficiary concerned
     * @param appointmentLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpretes that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param professionalSkillsNeeded List of business skills needed
     * @param timeSlot For every repetitive and non-repetitive Appointment
     * @throws IllegalArgumentException If beneficiary, professionalSkillsNeeded, interpreters or timeSlot is null
     *                                  If interpreters or professionalSkillsNeeded is empty
     */
    public Appointment(Beneficiary beneficiary, List<String> appointmentLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> professionalSkillsNeeded
            ,TimeSlot timeSlot) {
        if(beneficiary == null || interpreters == null || professionalSkillsNeeded == null || timeSlot == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le bénéficiaire, les interprètes, la tranche horaire et les compétences professionnelles ne peuvent pas être null.");
        }

        if(interpreters.isEmpty() || professionalSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] Les listes d'interprètes et de compétences professionnelles ne peuvent pas être vides.");
        }

        this.beneficiary = beneficiary;
        this.status = "en attente";
        this.appointmentLocals = appointmentLocals;
        this.interpreters = interpreters;
        this.timeSlot = timeSlot;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.professionalSkillsNeeded = professionalSkillsNeeded;
    }

    /**
     * @return the id of the Appointment
     */
    public int getNumAppointment() {

        return numAppointment;
    }

    /**
     * @return the status of the Appointment
     */
    public String getStatus() {

        return status;
    }

    /**
     * @return the list of locals of the Appointment
     */
    public List<String> getAppointmentLocals() {

        return appointmentLocals;
    }

    /**
     * @return the beneficiary concerned by the Appointment
     */
    public Beneficiary getBeneficiary() {

        return beneficiary;
    }

    /**
     * @return the list of interpretes concerned by the Appointment
     */
    public List<Interpreter> getInterpreters() {

        return interpreters;
    }

    /**
     * @return the time slot
     */
    public TimeSlot getTimeSlot() {

        return timeSlot;
    }

    /**
     * @return the list of academic skills needed for the Appointment
     */
    public List<AcademicSkill> getAcademicSkillsNeeded() {

        return academicSkillsNeeded;
    }

    /**
     * @return the list of business skills needed for the Appointment
     */
    public List<ProfessionalSkill> getProfessionalSkillsNeeded() {

        return professionalSkillsNeeded;
    }

    /**
     * @param id the id
     */
    public void setNumAppointment(int id) {

        this.numAppointment = id;
    }

    /**
     * @param appointmentLocals list of locals
     * @throws IllegalArgumentException if appointmentLocals is empty
     *                                  if appointmentLocals is null
     */
    public void setAppointmentLocals(List<String> appointmentLocals) {
        if(appointmentLocals == null) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des locaux ne peut pas être null.");
        }

        if(appointmentLocals.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des locaux ne peut pas être vide.");
        }

        this.appointmentLocals = appointmentLocals;
    }

    /**
     * @param beneficiary the beneficiary concerned
     * @throws IllegalArgumentException if beneficiary is null
     */
    public void setBeneficiary(Beneficiary beneficiary) {
        if(beneficiary == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le bénéficiaire ne peut pas être null.");
        }

        this.beneficiary = beneficiary;
    }

    /**
     * @param interpreters the list of interpreters
     * @throws IllegalArgumentException if interpreters is null
     *                                  if interpreters is empty
     */
    public void setInterpreters(List<Interpreter> interpreters) {
        if(interpreters == null) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des interprètes ne peut pas être null.");
        }

        if(interpreters.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des interprètes ne peut pas être vide.");
        }

        this.interpreters = interpreters;
    }

    /**
     * @param timeSlot the time slot
     * @throws IllegalArgumentException if timeSlot is null
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        if(timeSlot == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le créneau ne peut pas être null.");
        }

        this.timeSlot = timeSlot;
    }

    /**
     * @param academicSkillsNeeded the list of academic skills needed
     * @throws IllegalArgumentException if academicSkillsNeeded is null
     *                                  if academicSkillsNeeded is empty
     */
    public void setAcademicSkillsNeeded(List<AcademicSkill> academicSkillsNeeded) {
        if(academicSkillsNeeded == null) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des compétences académiques ne peut pas être null.");
        }

        if(academicSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des compétences académiques ne peut pas être vide.");
        }

        this.academicSkillsNeeded = academicSkillsNeeded;
    }

    /**
     * @param professionalSkillsNeeded the list of business skills needed
     * @throws IllegalArgumentException if professionalSkillsNeeded is null
     *                                  if professionalSkillsNeeded is empty
     */
    public void setProfessionalSkillsNeeded(List<ProfessionalSkill> professionalSkillsNeeded) {
        if(professionalSkillsNeeded == null) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des compétences professionnelles ne peut pas être null.");
        }

        if(professionalSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des compétences professionnelles ne peut pas être vide.");
        }

        this.professionalSkillsNeeded = professionalSkillsNeeded;
    }

    /**
     * @param status the status
     * @throws IllegalArgumentException if status is null
     * @throws BadStatusException if status is different from 'accepte' or 'refuse'
     *                            if status is the same as the already set status
     *                            if the current status is not equals to 'en attente'
     */
    public void setStatus(String status) throws BadStatusException {
        if(status == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le statut ne peut pas être null.");
        }

        if(this.status.equals(status)) {
            throw new BadStatusException();
        }

        if(!(status.equals("accepte") || status.equals("refuse"))) {
            throw new BadStatusException();
        }

        if(!this.status.equals("en attente")) {
            throw new BadStatusException();
        }

        this.status = status;
    }

    /**
     * @return a String containing the appointment ID, the status, the beneficiary, the interpreters,
     *         the locals, the academic skills needed, the business skills needed and
     *         the time slot
     */
    public String toString() {

        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("Rendez-vous\n");
        stringBuild.append("Id : ").append(this.numAppointment).append("\n");
        stringBuild.append("Statut : ").append(this.status).append("\n");
        stringBuild.append("Bénéficiaire : ").append(this.beneficiary).append("\n");
        stringBuild.append("Créneau : ").append(this.timeSlot).append("\n");

        stringBuild.append("Local :\n");
        if(this.appointmentLocals == null || this.appointmentLocals.isEmpty()) {
            stringBuild.append("Aucun local attribué\n");
        } else {
            for (String local : this.appointmentLocals) {
                stringBuild.append(local).append("\n");
            }
        }
        stringBuild.append("\n");

        stringBuild.append("Interprète(s):\n");
        if(this.interpreters == null || this.interpreters.isEmpty()) {
            stringBuild.append("Aucun interprète attribué\n");
        } else {
            for (Interpreter interpreter : this.interpreters) {
                stringBuild.append("- ").append(interpreter).append("\n");
            }
        }
        stringBuild.append("\n");

        stringBuild.append("Compétence(s) académique(s) :\n");
        if(this.academicSkillsNeeded == null || this.academicSkillsNeeded.isEmpty()) {
            stringBuild.append("Aucune compétence académique requise.\n");
        } else {
            for (AcademicSkill academicSkill : this.academicSkillsNeeded) {
                stringBuild.append("- ").append(academicSkill).append("\n");
            }
        }
        stringBuild.append("\n");

        stringBuild.append("Compétence(s) professionnelle(s) :\n");
        for (ProfessionalSkill professionalSkill : this.professionalSkillsNeeded) {
            stringBuild.append("- ").append(professionalSkill).append("\n");
        }
        stringBuild.append("\n");

        return stringBuild.toString();
    }
}