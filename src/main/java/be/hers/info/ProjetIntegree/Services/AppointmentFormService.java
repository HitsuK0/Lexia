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
     * It creates an appointment in the database using the data in the DTOAppointment given in param.
     * @param appointmentDTO the appointmentDTO used to retrieve the data in the form.
     * @return true if the appointment is created successfully. Otherwise, return false
     */
    public boolean createAppointment(DTOAppointment appointmentDTO) throws BadStatusException, SQLException {
        boolean estCree = false;
        Appointment newAppointment = new Appointment();

        newAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        if(beneficiary == null)
            throw new IllegalArgumentException("Bénéficiaire introuvable");
        newAppointment.setBeneficiary(beneficiary);

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        List<Interpreter> listInterpreters = new ArrayList<>();
        for(int num : appointmentDTO.getNumInterpreters()){
            Interpreter interpreterFound = daoInterpreter.find(num);
            if(interpreterFound == null)
                throw new IllegalArgumentException("Interprète introuvable");
            listInterpreters.add(interpreterFound);
        }
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
        if(establishment == null)
            throw new IllegalArgumentException("Etablissement introuvable");
        newAppointment.setEstablishment(establishment);

        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        List<AcademicSkill> listAcademicSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumAcademicSkillsNeeded()){
            AcademicSkill academicSkillFinded = daoAcademicSkill.find(num);
            if(academicSkillFinded == null)
                throw new IllegalArgumentException("Compétence académique introuvable");
            listAcademicSkills.add(academicSkillFinded);
        }
        newAppointment.setAcademicSkillsNeeded(listAcademicSkills);

        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        List<ProfessionalSkill> listProfessionalSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumProfessionalSkillsNeeded()){
            ProfessionalSkill professionalSkillFinded = daoProfessionalSkill.find(num);
            if(professionalSkillFinded == null)
                throw new IllegalArgumentException("Compétence professionnelle introuvable");
            listProfessionalSkills.add(professionalSkillFinded);
        }
        newAppointment.setProfessionalSkillsNeeded(listProfessionalSkills);

        DAOAppointment daoAppointment = new DAOAppointment();
        if(daoAppointment.create(newAppointment))
            estCree = true;

        return estCree;
    }
}
