package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vatafu Jean
 * @reviewer Nicolas Jean-François, Halet Louis
 */

public class Appointment {
    private int numAppointment;
    private String description;
    private String status;
    private List<String> appointmentLocals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlot timeSlot;
    private Establishment establishment;
    private List<AcademicSkill> academicSkillsNeeded;
    private List<ProfessionalSkill> professionalSkillsNeeded;

    /**
     * Initialize an Appointment with no elements
     * The parameter numAppointment can only be initialized with setNumAppointment
     */
    public Appointment() {
        this.status = "en attente";
        this.description = "Aucune description";
        this.appointmentLocals = new ArrayList<String>();
        this.timeSlot = null;
        this.establishment = null;
        this.beneficiary = null;
        this.academicSkillsNeeded = new ArrayList<AcademicSkill>();
        this.professionalSkillsNeeded = new ArrayList<ProfessionalSkill>();
        this.interpreters = new ArrayList<Interpreter>();
    }

    /**
     * Initialize an Appointment with numAppointment, description, status,
     * beneficiary, appointmentLocals, interpreters, academicSkillsNeeded, professionalSkillsNeeded,
     * timeSlot and establishment
     * @param numAppointment The id of the Appointment
     * @param description The description of the Appointement
     * @param status The status
     * @param beneficiary The Beneficiary concerned
     * @param appointmentLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpreters that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param professionalSkillsNeeded List of professional skills needed
     * @param timeSlot For every repetitive or non-repetitive Appointment
     * @param establishment The establishment, can be null
     * @throws IllegalArgumentException If beneficiary, academicSkillsNeeded, professionalSkillsNeeded, interpreters or timeSlot is null
     *                                  If professionalSkillsNeeded is empty
     */
    public Appointment(int numAppointment, String description, String status, Beneficiary beneficiary, List<String> appointmentLocals, List<Interpreter> interpreters,
                       List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> professionalSkillsNeeded, TimeSlot timeSlot, Establishment establishment) {
        if(beneficiary == null || interpreters == null || academicSkillsNeeded == null || professionalSkillsNeeded == null || timeSlot == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le bénéficiaire, les interprètes, la tranche horaire, les compétences académiques et les compétences professionnelles ne peuvent pas être null.");
        }

        if(professionalSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] Les listes d'interprètes et de compétences professionnelles ne peuvent pas être vides.");
        }

        this.numAppointment = numAppointment;
        this.description = description;
        this.status = status;
        this.beneficiary = beneficiary;
        this.appointmentLocals = appointmentLocals;
        this.interpreters = interpreters;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.professionalSkillsNeeded = professionalSkillsNeeded;
        this.timeSlot = timeSlot;

        if(establishment != null) {
            this.establishment = establishment;
        }
    }

    /**
     * Initialize an Appointment with numAppointment, status and appointmentLocals
     * @param numAppointment The id of the Appointment
     * @param status The status of the Appointment
     * @param appointmentLocals List of local(s) where the Appointment will take place
     * @param beneficiary The Beneficiary concerned
     * @param timeSlot For every repetitive or non-repetitive Appointment
     * @param establishment The establishment, can be null
     */
    public Appointment(int numAppointment, String description, String status, List<String> appointmentLocals, Beneficiary beneficiary, TimeSlot timeSlot,
                       Establishment establishment) {
        this.description = description;
        this.numAppointment = numAppointment;
        this.status = status;
        this.appointmentLocals = appointmentLocals;
        this.timeSlot = timeSlot;
        this.beneficiary = beneficiary;
        this.interpreters = new ArrayList<Interpreter>();
        this.academicSkillsNeeded= new ArrayList<AcademicSkill>();
        this.professionalSkillsNeeded = new ArrayList<ProfessionalSkill>();

        if(establishment != null) {
            this.establishment = establishment;
        }
    }

    /**
     * Initialize an Appointment with beneficiary, appointmentLocals, interpreters, specialists, academicSkillsNeeded, professionalSkillsNeeded,
     * timeSlot and sets the status to 'en attente' by default
     * @param numAppointment The id of the Appointment
     * @param beneficiary The Beneficiary concerned
     * @param appointmentLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpreters that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param professionalSkillsNeeded List of professional skills needed
     * @param timeSlot For every repetitive or non-repetitive Appointment
     * @param establishment The establishment, can be null
     * @throws IllegalArgumentException If beneficiary, professionalSkillsNeeded, interpreters or timeSlot is null
     *                                  If professionalSkillsNeeded is empty
     */
    public Appointment(int numAppointment, String description, Beneficiary beneficiary, List<String> appointmentLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> professionalSkillsNeeded
            ,TimeSlot timeSlot, Establishment establishment) {
        if(beneficiary == null || interpreters == null || professionalSkillsNeeded == null || timeSlot == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le bénéficiaire, les interprètes, la tranche horaire et les compétences professionnelles ne peuvent pas être null.");
        }

        if(professionalSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] Les listes d'interprètes et de compétences professionnelles ne peuvent pas être vides.");
        }

        this.description = description;
        this.numAppointment = numAppointment;
        this.beneficiary = beneficiary;
        this.status = "en attente";
        this.appointmentLocals = appointmentLocals;
        this.interpreters = interpreters;
        this.timeSlot = timeSlot;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.professionalSkillsNeeded = professionalSkillsNeeded;

        if(establishment != null) {
            this.establishment = establishment;
        }
    }

    /**
     * Initialize an Appointment with beneficiary, appointmentLocals, interpreters, specialists, academicSkillsNeeded, professionalSkillsNeeded,
     * timeSlot and sets the status to 'en attente' by default
     * @param beneficiary The Beneficiary concerned
     * @param appointmentLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpreters that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param professionalSkillsNeeded List of professional skills needed
     * @param timeSlot For every repetitive and non-repetitive Appointment
     * @param establishment The establishment, can be null
     * @throws IllegalArgumentException If beneficiary, professionalSkillsNeeded, interpreters or timeSlot is null
     *                                  If professionalSkillsNeeded is empty
     */
    public Appointment(String description, Beneficiary beneficiary, List<String> appointmentLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> professionalSkillsNeeded
            ,TimeSlot timeSlot, Establishment establishment) {
        if(beneficiary == null || interpreters == null || professionalSkillsNeeded == null || timeSlot == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le bénéficiaire, les interprètes, la tranche horaire et les compétences professionnelles ne peuvent pas être null.");
        }

        if(professionalSkillsNeeded.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] Les listes d'interprètes et de compétences professionnelles ne peuvent pas être vides.");
        }

        this.description = description;
        this.beneficiary = beneficiary;
        this.status = "en attente";
        this.appointmentLocals = appointmentLocals;
        this.interpreters = interpreters;
        this.timeSlot = timeSlot;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.professionalSkillsNeeded = professionalSkillsNeeded;

        if(establishment != null) {
            this.establishment = establishment;
        }
    }
    /**
     * Initialize an Appointment with the minimum fields needed to display
     * @param numAppointment the id of the Appointment
     * @param status the status of the Appointment
     * @param listAcademicSkillRequire the list of academic skills needed (can be null, treated as empty)
     * @param listProfessionalSkillRequire the list of professional skills needed
     * @param timeSlot the timeSlot of the Appointment
     * @throws IllegalArgumentException if timeSlot or listProfessionalSkillRequire is null
     *                                  if listProfessionalSkillRequire is empty
     */
    public Appointment(int numAppointment, String status, List<AcademicSkill> listAcademicSkillRequire, List<ProfessionalSkill> listProfessionalSkillRequire, TimeSlot timeSlot) {
        if(timeSlot == null || listProfessionalSkillRequire == null) {
            throw new IllegalArgumentException("[POJOAppointment] La tranche horaire ou la liste des competences profesionnelles ne peut pas etre null.");
        }

        if(listProfessionalSkillRequire.isEmpty()) {
            throw new IllegalArgumentException("[POJOAppointment] La liste des competences profesionnelles ne peut pas etre vide.");
        }

        this.numAppointment = numAppointment;
        this.status = status;
        this.timeSlot = timeSlot;
        if(listAcademicSkillRequire != null) {
            this.academicSkillsNeeded = listAcademicSkillRequire;
        } else {
            this.academicSkillsNeeded = new ArrayList<AcademicSkill>();
        }

        this.professionalSkillsNeeded = listProfessionalSkillRequire;
        this.description = "";
        this.appointmentLocals = new ArrayList<String>();
        this.interpreters = new ArrayList<Interpreter>();
    }
    /**
     * @return the id of the Appointment
     */
    public int getNumAppointment() {
        return numAppointment;
    }

    /**
     * @return the description of the Appointment
     */
    public String getDescription() {
        return description;
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
     * @return the list of interpreters concerned by the Appointment
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
     * @return the list of professional skills needed for the Appointment
     */
    public List<ProfessionalSkill> getProfessionalSkillsNeeded() {
        return professionalSkillsNeeded;
    }

    /**
     * @return the establishment of the Appointment
     */
    public Establishment getEstablishment() {
        return establishment;
    }

    /**
     * @param id the id
     */
    public void setNumAppointment(int id) {
        if(id < 0) {
            throw new IllegalArgumentException("[POJOAppointment] Le id ne peut pas être négatif.");
        }
        this.numAppointment = id;
    }

    /**
     * @param description the description of the appointment
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @param professionalSkillsNeeded the list of professional skills needed
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
     * @throws BadStatusException if status is different from 'accepte', 'refuse','annule' or 'en attente'
     *                            if status is the same as the already set status
     *                            if the current status is not equals to 'en attente'
     */
    public void setStatus(String status) throws BadStatusException {
        if(status == null) {
            throw new IllegalArgumentException("[POJOAppointment] Le statut ne peut pas être null.");
        }

        if(this.status.equals(status)) {
            throw new BadStatusException("[POJOAppointment] Le status status est déja "+this.status);
        }

        if(!(status.equals("accepte") || status.equals("refuse") || status.equals("en attente") || status.equals("annule"))) {
            throw new BadStatusException("[POJOAppointment] "+status+ " n'est pas un status valide.");
        }

        if(!this.status.equals("en attente")) {
            throw new BadStatusException("[POJOAppointment] Le status ne peut plus être modifié.");
        }

        this.status = status;
    }

    /**
     * @param establishment the establishment to set
     * @throws IllegalArgumentException if establishment is null
     */
    public void setEstablishment(Establishment establishment) {
        if(establishment == null) {
            throw new IllegalArgumentException("[POJOAppointment] L'établissement ne peut pas être null.");
        }

        this.establishment = establishment;
    }

    /**
     * @return a String containing the appointment ID, the status, the beneficiary, the interpreters,
     *         the locals, the academic skills needed, the business skills needed and
     *         the time slot
     */
    public String toString() {

        StringBuilder stringBuild = new StringBuilder();
        stringBuild.append("Rendez-vous\n");
        stringBuild.append("Description : ").append(this.description).append("\n");
        stringBuild.append("Id : ").append(this.numAppointment).append("\n");
        stringBuild.append("Statut : ").append(this.status).append("\n");

        stringBuild.append("Bénéficiaire : ");
        if(this.beneficiary == null) {
            stringBuild.append("Aucun bénéficiaire attribué\n");
        } else {
                stringBuild.append(this.beneficiary).append("\n");
        }

        stringBuild.append("Créneau : ");
        if(this.timeSlot == null) {
            stringBuild.append("Aucun créneau attribué\n");
        } else {
            stringBuild.append(this.timeSlot).append("\n");
        }

        stringBuild.append("Établissement : \n");
        if(this.establishment == null) {
            stringBuild.append("Aucun establishment attribué\n");
        } else {
            stringBuild.append(this.establishment).append("\n");
        }

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