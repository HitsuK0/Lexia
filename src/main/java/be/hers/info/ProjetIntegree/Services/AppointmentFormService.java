package be.hers.info.ProjetIntegree.Services;

/**
 * @authors Rosman Loïs
 * @reviewer Nicolas Jean-François
 */

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.DTO.DTOAppointmentForm;
import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryFormAppointment;
import be.hers.info.ProjetIntegree.DTO.DTOEstablishmentFormAppointment;
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
     * @throws BadStatusException If an bad status error occurs with this method.
     * @throws SQLException If an SQL error occurs with this method.
     * @throws IllegalArgumentException If an element contained in an appointment isn't found
     */
    public boolean createAppointment(DTOAppointmentForm appointmentDTO) throws BadStatusException, SQLException {
        boolean estCree = false;
        Appointment newAppointment = new Appointment();

        newAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        if(beneficiary == null)
            throw new IllegalArgumentException("Bénéficiaire introuvable");
        newAppointment.setBeneficiary(beneficiary);

        TimeSlotPunctual newTimeSlotPunctual = new TimeSlotPunctual(
                appointmentDTO.getStartTime(),
                appointmentDTO.getEndTime(),
                appointmentDTO.getStartDate(),
                appointmentDTO.getEndDate()
        );

        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        daoTimeSlotPunctual.create(newTimeSlotPunctual);
        newAppointment.setTimeSlot(newTimeSlotPunctual);

        DAOEstablishment daoEstablishment = new DAOEstablishment();
        Establishment establishment = daoEstablishment.find(appointmentDTO.getNumEstablishment());
        if(establishment == null)
            throw new IllegalArgumentException("Etablissement introuvable");
        newAppointment.setEstablishment(establishment);

        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        List<AcademicSkill> listAcademicSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumAcademicSkillsNeeded()){
            AcademicSkill academicSkillFound = daoAcademicSkill.find(num);
            if(academicSkillFound == null)
                throw new IllegalArgumentException("Compétence académique introuvable");
            listAcademicSkills.add(academicSkillFound);
        }
        newAppointment.setAcademicSkillsNeeded(listAcademicSkills);

        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        List<ProfessionalSkill> listProfessionalSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumProfessionalSkillsNeeded()){
            ProfessionalSkill professionalSkillFound = daoProfessionalSkill.find(num);
            if(professionalSkillFound == null)
                throw new IllegalArgumentException("Compétence professionnelle introuvable");
            listProfessionalSkills.add(professionalSkillFound);
        }
        newAppointment.setProfessionalSkillsNeeded(listProfessionalSkills);

        DAOAppointment daoAppointment = new DAOAppointment();
        if(daoAppointment.create(newAppointment))
            estCree = true;

        return estCree;
    }

    /**
     * Search for all beneficiaries whose the referent is the interpreter whose the id is indicated
     * @param numInterpreter the interpreter's id which is the referent
     * @return a list containing at least one beneficiary if the DB containing at least one
     *         an empty list if no beneficiary found
     *         null if SQLException is thrown
     * @throws SQLException If an SQL error occurs with this method.
     */
    public List<DTOBeneficiaryFormAppointment> findHisBeneficiaries(int numInterpreter){
        try{
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            return daoBeneficiary.findWithInterpreter(numInterpreter);
        }
        catch(SQLException sqle){
            return null;
        }
    }

    /**
     * Search for all the establishments in the DB and stores them in
     * a list containing DTOEstablishmentFormAppointment objects
     * @return a list containing all the establishments
     *         an empty list if no establishment found
     *         null if an SQLException is thrown
     */
    public List<DTOEstablishmentFormAppointment> findAllEstablishments(){
        try{
            DAOEstablishment daoEstablishment = new DAOEstablishment();
            return daoEstablishment.findAllDTOFormAppointment();
        }
        catch(SQLException sqle){
            return null;
        }
    }

    /**
     * Search all academic skills in the DB
     * @return a list containing all academic skills in the DB
     *         an empty list if no academic skill found
     *         null if an SQLException is thrown
     */
    public List<AcademicSkill> findAllAcademicSkills(){
        try{
            DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
            return daoAcademicSkill.findAll();
        }
        catch(SQLException sqle){
            return null;
        }
    }

    /**
     * Search all professional skills in the DB
     * @return a list containing all professional skills in the DB
     *         an empty list if no professional skill found
     *         null if an SQLException is thrown
     */
    public List<ProfessionalSkill> findAllProfessionalSkills(){
        try{
            DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
            return daoProfessionalSkill.findAll();
        }
        catch(SQLException sqle){
            return null;
        }
    }

    public boolean updateAppointment(DTOAppointmentForm appointmentDTO, int numAppointment) throws SQLException {
        boolean isUpdated = false;
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setNumAppointment(numAppointment);

        updatedAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        if(beneficiary == null)
            throw new IllegalArgumentException("Bénéficiaire introuvable");
        updatedAppointment.setBeneficiary(beneficiary);

        TimeSlotPunctual newTimeSlotPunctual = new TimeSlotPunctual(
                appointmentDTO.getStartTime(),
                appointmentDTO.getEndTime(),
                appointmentDTO.getStartDate(),
                appointmentDTO.getEndDate()
        );

        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        daoTimeSlotPunctual.create(newTimeSlotPunctual);
        updatedAppointment.setTimeSlot(newTimeSlotPunctual);

        DAOEstablishment daoEstablishment = new DAOEstablishment();
        Establishment establishment = daoEstablishment.find(appointmentDTO.getNumEstablishment());
        if(establishment == null)
            throw new IllegalArgumentException("Etablissement introuvable");
        updatedAppointment.setEstablishment(establishment);

        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        List<AcademicSkill> listAcademicSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumAcademicSkillsNeeded()){
            AcademicSkill academicSkillFound = daoAcademicSkill.find(num);
            if(academicSkillFound == null)
                throw new IllegalArgumentException("Compétence académique introuvable");
            listAcademicSkills.add(academicSkillFound);
        }
        updatedAppointment.setAcademicSkillsNeeded(listAcademicSkills);

        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        List<ProfessionalSkill> listProfessionalSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumProfessionalSkillsNeeded()){
            ProfessionalSkill professionalSkillFound = daoProfessionalSkill.find(num);
            if(professionalSkillFound == null)
                throw new IllegalArgumentException("Compétence professionnelle introuvable");
            listProfessionalSkills.add(professionalSkillFound);
        }
        updatedAppointment.setProfessionalSkillsNeeded(listProfessionalSkills);

        DAOAppointment daoAppointment = new DAOAppointment();
        if(daoAppointment.update(updatedAppointment))
            isUpdated = true;

        return isUpdated;
    }
}

