package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAppointment;
import be.hers.info.ProjetIntegree.DTO.DTOAppointmentRequest;
import be.hers.info.ProjetIntegree.POJO.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    /**
     * Creates a new appointment request for the given Beneficiary
     * The appointment is created with status "en attente" and is linked to a
     * TimeSlotPunctual built from the form (full day or specific hour range).
     * Skills are passed with id only, DAOAppointment handles their insertion
     * into the junction tables, same thing for the Establishment.
     * @param dtoAppointmentRequest the DTOAppointmentRequest used by spring
     * @param beneficiary the beneficiary who submitted the request
     * @throws SQLException if a database access error occurs during insertion
     * @throws BadStatusException if a status is rejected by the Appointment POJO
     */
    public void createAppointmentRequest(DTOAppointmentRequest dtoAppointmentRequest, Beneficiary beneficiary) throws SQLException, BadStatusException {
        Appointment appointment = new Appointment();
        appointment.setStatus("en attente");
        appointment.setBeneficiary(beneficiary);
        appointment.setDescription(dtoAppointmentRequest.getDescription());
        appointment.setAppointmentLocals(dtoAppointmentRequest.getLocals());

        Establishment establishment = new Establishment();
        establishment.setNumEstablishment(dtoAppointmentRequest.getNumEstablishment());
        appointment.setEstablishment(establishment);

        TimeSlotPunctual timeSlotPunctual = new TimeSlotPunctual();
        timeSlotPunctual.setStartDate(dtoAppointmentRequest.getDateStart());
        timeSlotPunctual.setEndDate(dtoAppointmentRequest.getDateEnd() != null ? dtoAppointmentRequest.getDateEnd() : null);

        LocalTime durationTime = null;
        if(dtoAppointmentRequest.isFullDay()) {
            timeSlotPunctual.setStartTime(LocalTime.MIDNIGHT);
            durationTime = LocalTime.MIDNIGHT;
        } else {
            timeSlotPunctual.setStartTime(dtoAppointmentRequest.getStartHour());
            Duration duration = Duration.between(dtoAppointmentRequest.getStartHour(), dtoAppointmentRequest.getEndHour());
            durationTime = LocalTime.MIDNIGHT.plus(duration);
        }
        timeSlotPunctual.setDuration(durationTime);

        appointment.setTimeSlot(timeSlotPunctual);

        List<AcademicSkill> academicSkillsForAppointment = new ArrayList<AcademicSkill>();
        for(Integer numAcad : dtoAppointmentRequest.getNumAcademicSkill()) {
            AcademicSkill skill = new AcademicSkill();
            skill.setNumAcademicSkill(numAcad);
            academicSkillsForAppointment.add(skill);
        }
        appointment.setAcademicSkillsNeeded(academicSkillsForAppointment);

        List<ProfessionalSkill> professionalSkillsForAppointment = new ArrayList<ProfessionalSkill>();
        for(Integer numPro : dtoAppointmentRequest.getNumProfessionalSkill()) {
            ProfessionalSkill skill = new ProfessionalSkill();
            skill.setNumProfessionalSkill(numPro);
            professionalSkillsForAppointment.add(skill);
        }
        appointment.setProfessionalSkillsNeeded(professionalSkillsForAppointment);

        DAOAppointment daoAppointment = new DAOAppointment();
        daoAppointment.create(appointment);
    }
}
