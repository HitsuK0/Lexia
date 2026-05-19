package be.hers.info.ProjetIntegree.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DTOAppointmentRequest {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalTime startHour;
    private LocalTime endHour;
    private String description;
    private List<String> locals = new ArrayList<String>();
    private int numEstablishment;
    private boolean fullDay;
    private List<Integer> numAcademicSkills;
    private List<Integer> numProfessionalSkills;

    /**
     * Creates an empty DTOAppointmentRequest
     */
    public DTOAppointmentRequest(){}

    /** @return the start date of the appointment request */
    public LocalDate getDateStart() {
        return dateStart;
    }

    /** @return the end date of the appointment request */
    public LocalDate getDateEnd() {
        return dateEnd;
    }

    /** @return the start hour of the appointment request */
    public LocalTime getStartHour() {
        return startHour;
    }

    /** @return the end hour of the appointment request */
    public LocalTime getEndHour() {
        return endHour;
    }

    /** @return true if the appointment request is a full day request, false otherwise */
    public boolean isFullDay() {
        return fullDay;
    }

    /** @return the locals of the appointment request */
    public List<String> getLocals() {
        return locals;
    }

    /** @return the description of the appointment request */
    public String getDescription() {
        return description;
    }

    /** @return the id of the establishment of the appointment request */
    public int getNumEstablishment() {
        return numEstablishment;
    }

    /** @return the list of academic skills id's of the appointment request */
    public List<Integer> getNumAcademicSkill() {
        return numAcademicSkills;
    }

    /** @return the list of professional skills id's of the appointment request */
    public List<Integer> getNumProfessionalSkill() {
        return numProfessionalSkills;
    }

    /**
     * Sets the end hour of the request
     * @param endHour the hours when Appointment ends
     */
    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    /**
     * Sets the start date of the request
     * @param dateStart the date when Appointment starts
     */
    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * Sets the end date of the request
     * @param dateEnd the date when Appointment ends
     */
    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    /**
     * Sets the start hour of the request
     * @param startHour the hour when Appointment starts
     */
    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    /**
     * Sets the full day request state
     * @param fullDay the full day request state
     */
    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    /**
     * Sets the id's of the academic skills needed for this Appointment request
     * @param numAcademicSkills the list of id's referring to Academic Skills
     */
    public void setNumAcademicSkill(List<Integer> numAcademicSkills) {
        this.numAcademicSkills = numAcademicSkills;
    }

    /**
     * Sets the id's of the professional skills needed for this Appointment request
     * @param numProfessionalSkills the list of id's referring to Professional Skills
     */
    public void setNumProfessionalSkill(List<Integer> numProfessionalSkills) {
        this.numProfessionalSkills = numProfessionalSkills;
    }

    /**
     * Sets the id's of the establishment for this Appointment request
     * @param numEstablishment the id of the establishment
     */
    public void setNumEstablishment(int numEstablishment) {
        this.numEstablishment = numEstablishment;
    }

    /**
     * Sets the description for this Appointment request
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the locals for this Appointment request
     * @param locals the locals
     */
    public void setLocals(List<String> locals) {
        this.locals = locals;
    }
}
