package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.DTO.DTOAppointment;
import be.hers.info.ProjetIntegree.POJO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service link to the form used to ask an appointment
 */
public class AppointmentFormService {
    /**
     * It creates an appointment in the database using the data in the absenceDTO given in param.
     * @param appointmentDTO the appointmentDTO used to retrieve the data in the form.
     * @return a success message if the appointment is added to the DB. Otherwise, return an error message
     */
    public String createAppointment(DTOAppointment appointmentDTO) throws BadStatusException, SQLException {
        String messageReussite = "";
        Appointment newAppointment = new Appointment();

        newAppointment.setStatus(appointmentDTO.getStatus());
        newAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        newAppointment.setBeneficiary(beneficiary);

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        List<Interpreter> listInterpreters = new ArrayList<>();
        for(int num : appointmentDTO.getNumInterpreters())
            listInterpreters.add(daoInterpreter.find(num));
        newAppointment.setInterpreters(listInterpreters);

        TimeSlotPunctual newTimeSlotPunctual = new TimeSlotPunctual(
                appointmentDTO.getStartTime(),
                appointmentDTO.getEndTime(),
                appointmentDTO.getStartDate(),
                appointmentDTO.getEndDate()
        );

        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        TimeSlotPunctual tempTimeSlot = daoTimeSlotPunctual.findSameTimeSlot(newTimeSlotPunctual);
        if(tempTimeSlot == null)
            daoTimeSlotPunctual.create(newTimeSlotPunctual);
        else
            newTimeSlotPunctual = tempTimeSlot;
        newAppointment.setTimeSlot(newTimeSlotPunctual);

        DAOEstablishment daoEstablishment = new DAOEstablishment();
        Establishment establishment = daoEstablishment.find(appointmentDTO.getNumEstablishment());
        newAppointment.setEstablishment(establishment);

        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        List<AcademicSkill> listAcademicSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumAcademicSkillsNeeded())
            listAcademicSkills.add(daoAcademicSkill.find(num));
        newAppointment.setAcademicSkillsNeeded(listAcademicSkills);

        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        List<ProfessionalSkill> listProfessionalSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumProfessionalSkillsNeeded())
            listProfessionalSkills.add(daoProfessionalSkill.find(num));
        newAppointment.setProfessionalSkillsNeeded(listProfessionalSkills);

        DAOAppointment daoAppointment = new DAOAppointment();
        if(daoAppointment.create(newAppointment))
            messageReussite = "a réussi";
        else
            messageReussite = "a échoué";

        return messageReussite;
    }
}
