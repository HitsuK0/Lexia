package be.hers.info.ProjetIntegree.POJO;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private int appointmentID;
    private String status;
    private List<String> appointementLocals;
    private Beneficiary beneficiary;
    private List<Interpreter> interpreters;
    private TimeSlotPunctual timeSlotPunctual;
    private TimeSlotBase timeSlotBase;
    private List<AcademicSkill> academicSkillsNeeded;
    private List<ProfessionalSkill> professionalSkillsNeeded;

    /**
     * Initialize an Appointment with beneficiary, appointementLocals, interpreters, specialists, academicSkillsNeeded, professionalSkillsNeeded
,
     * timeSlotPunctual, timeSlotBase and sets the status to 'On hold' by default
     * @param appointmentID The id of the Appointment
     * @param beneficiary The Beneficiary concerned
     * @param appointementLocals List of local(s) where the Appointment will take place, can be null
     * @param interpreters List of Interpretes that will participate
     * @param academicSkillsNeeded List of academic skills needed, can be null
     * @param professionalSkillsNeeded
 List of business skills needed
     * @param timeSlotPunctual For every non-repetitive Appointment, can be null
     * @param timeSlotBase For every repetitive Appointment, can be null
     *                     Note: an Appointment is either a timeSlotPunctual or a timeSlotBase, but not both at the same time
     * @throws NullPointerException if beneficiary, appointementLocals, professionalSkillsNeeded
, interpreters is null
     * @throws IllegalArgumentException If interpreters or professionalSkillsNeeded
 is empty
     *                                  If timeSlotPunctual and timeSlotBase are not null
     *                                  If appointmentID is negative
     */
    public Appointment(int appointmentID, Beneficiary beneficiary, List<String> appointementLocals, List<Interpreter> interpreters, List<AcademicSkill> academicSkillsNeeded, List<ProfessionalSkill> professionalSkillsNeeded
,
                       TimeSlotPunctual timeSlotPunctual, TimeSlotBase timeSlotBase) {
        if(beneficiary == null || appointementLocals == null || interpreters == null || professionalSkillsNeeded
 == null || (timeSlotPunctual == null && timeSlotBase == null)) {
            throw new NullPointerException();
        }

        if(timeSlotPunctual != null && timeSlotBase != null) {
            throw new IllegalArgumentException();
        }

        if(appointmentID < 0) {
            throw new IllegalArgumentException();
        }

        if(interpreters.size() == 0 || professionalSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointmentID = appointmentID;
        this.beneficiary = beneficiary;
        this.status = "en attente";
        this.appointementLocals = appointementLocals;
        this.interpreters = interpreters;
        this.timeSlotPunctual = timeSlotPunctual;
        this.timeSlotBase = timeSlotBase;
        this.academicSkillsNeeded = academicSkillsNeeded;
        this.professionalSkillsNeeded
 = professionalSkillsNeeded
;
    }

    /**
     * Initialize an Appointment with no elements
     */
    public Appointment() {
        status = "en attente";
        appointementLocals = new ArrayList<String>();
        interpreters = null;
        timeSlotPunctual = null;
        timeSlotBase = null;
        academicSkillsNeeded = new ArrayList<AcademicSkill>();
        professionalSkillsNeeded
 = new ArrayList<ProfessionalSkill>();
    }

    /**
     * @return the id of the Appointment
     */
    public int getAppointmentID() {

        return appointmentID;
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
    public List<String> getAppointementLocals() {

        return appointementLocals;
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
     * @return the time slot punctual, only for non-repetitive Appointments
     * @throws NullPointerException if timeSlotPunctual is null
     */
    public TimeSlotPunctual getTimeSlotPunctual() {
        if(timeSlotPunctual == null) {
            throw new NullPointerException();
        }

        return timeSlotPunctual;
    }

    /**
     * @return the time slot base, only for repetitive Appointments
     * @throws NullPointerException if timeSlotBase is null
     */
    public TimeSlotBase getTimeSlotBase() {
        if(timeSlotBase == null ){
            throw new NullPointerException();
        }

        return timeSlotBase;
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
    public List<ProfessionalSkill> getProfessionalSkillsNeeded
() {

        return professionalSkillsNeeded
;
    }

    /**
     * @param id the id
     * @throws IllegalArgumentException if the id is negative
     */
    public void setAppointmentID(int id) {
        if(id < 0) {
            throw new IllegalArgumentException();
        }
        this.appointmentID = id;
    }

    /**
     * @param appointementLocals list of locals
     * @throws IllegalArgumentException if appointementLocals is empty
     * @throws NullPointerException if appointementLocals is null
     */
    public void setAppointementLocals(List<String> appointementLocals) {
        if(appointementLocals == null) {
            throw new NullPointerException();
        }

        if(appointementLocals.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.appointementLocals = appointementLocals;
    }

    /**
     * @param beneficiary the beneficiary concerned
     * @throws NullPointerException if beneficiary is null
     */
    public void setBeneficiary(Beneficiary beneficiary) {
        if(beneficiary == null) {
            throw new NullPointerException();
        }

        this.beneficiary = beneficiary;
    }

    /**
     * @param interpreters the list of interpreters
     * @throws NullPointerException if interpreters is null
     * @throws IllegalArgumentException interpreters is empty
     */
    public void setInterpreters(List<Interpreter> interpreters) {
        if(interpreters == null) {
            throw new NullPointerException();
        }

        if(interpreters.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.interpreters = interpreters;
    }

    /**
     * @param timeSlotPunctual the time slot punctual
     * @throws NullPointerException if timeSlotPunctual is null
     */
    public void setTimeSlotPunctual(TimeSlotPunctual timeSlotPunctual) {
        if(timeSlotPunctual == null) {
            throw new NullPointerException();
        }

        this.timeSlotPunctual = timeSlotPunctual;
    }

    /**
     * @param timeSlotBase the time slot base
     * @throws NullPointerException if timeSlotBase is null
     */
    public void setTimeSlotBase(TimeSlotBase timeSlotBase) {
        if(timeSlotBase == null) {
            throw new NullPointerException();
        }

        this.timeSlotBase = timeSlotBase;
    }

    /**
     * @param academicSkillsNeeded the list of academic skills needed
     * @throws NullPointerException if academicSkillsNeeded is null
     * @throws IllegalArgumentException if academicSkillsNeeded is empty
     */
    public void setAcademicSkillsNeeded(List<AcademicSkill> academicSkillsNeeded) {
        if(academicSkillsNeeded == null) {
            throw new NullPointerException();
        }

        if(academicSkillsNeeded.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.academicSkillsNeeded = academicSkillsNeeded;
    }

    /**
     * @param professionalSkillsNeeded
 the list of business skills needed
     * @throws NullPointerException if professionalSkillsNeeded
 is null
     * @throws IllegalArgumentException if professionalSkillsNeeded
 is empty
     */
    public void setProfessionalSkillsNeeded
(List<ProfessionalSkill> professionalSkillsNeeded
) {
        if(professionalSkillsNeeded
 == null) {
            throw new NullPointerException();
        }

        if(professionalSkillsNeeded
.size() == 0) {
            throw new IllegalArgumentException();
        }

        this.professionalSkillsNeeded
 = professionalSkillsNeeded
;
    }

    /**
     * @param status the status
     * @throws NullPointerException if status is null
     * @throws BadStatusException if status is different from 'accepted' or 'refused'
     *                            if status is the same as the already set status
     *                            if the current status is not on hold
     */
    public void setStatus(String status) throws BadStatusException {
        if(status == null) {
            throw new NullPointerException();
        }

        if(!(status.equals("accepte") || status.equals("refuse")) || this.status.equals(status)) {
            throw new BadStatusException();
        }

        if(!this.status.equals("en attente")) {
            throw new BadStatusException();
        }

        this.status = status;
    }

    /**
     * @return a String containing the appointment ID, the status, the beneficiary, the interpreters,
     *         the locals, the academic skills needed, the business skills needed,
     *         the time slot punctual and the time slot base
     */
    public String toString() {

        String strLocals = "Local :\n";
        if(this.appointementLocals == null || this.appointementLocals.size() == 0) {
            strLocals += "Aucun local attribué\n";
        } else {
            for (int i = 0; i < this.appointementLocals.size(); i++) {
                strLocals += this.appointementLocals.get(i) + "\n";
            }
        }
        strLocals += "\n";

        String strInterpreters = "Interprète(s) :\n";
        if(this.interpreters == null || this.interpreters.size() == 0) {
            strInterpreters += "Aucun interprète attribué\n";
        } else {
            for (int i = 0; i < this.interpreters.size(); i++) {
                strInterpreters += "- " + this.interpreters.get(i) + "\n";
            }
        }
        strInterpreters += "\n";

        String strAcademicSkills = "Compétence(s) académique(s) :\n";
        if(this.academicSkillsNeeded == null || this.academicSkillsNeeded.size() == 0) {
            strAcademicSkills += "Aucune compétence académique requise.\n";
        } else {
            for (int i = 0; i < this.academicSkillsNeeded.size(); i++) {
                strAcademicSkills += "- " + this.academicSkillsNeeded.get(i) + "\n";
            }
        }
        strAcademicSkills += "\n";

        String strProfessionalSkill = "Compétence(s) professionnelle(s) :\n";
        for (int i = 0; i < this.professionalSkillsNeeded.size(); i++) {
            strProfessionalSkill += this.professionalSkillsNeeded.get(i).toString() + "\n";
        }
        strProfessionalSkill += "\n";

        String strTimeSlot = "Tranche horaire :\n";
        if(this.timeSlotBase != null) {
            strTimeSlot += "Créneau répétitif : " + this.timeSlotBase.toString() + "\n";
        } else {
            strTimeSlot += "Créneau ponctuel : " + this.timeSlotPunctual.toString() + "\n";
        }
        strTimeSlot += "\n";

        return "Rendez-vous\n" +
                "Id : " + this.appointmentID + "\n" +
                "Statut : " + this.status + "\n" +
                "Bénéficiaire : " + this.beneficiary.toString() + "\n" +
                strTimeSlot + strLocals + strInterpreters + strAcademicSkills + strProfessionalSkill;
    }
}