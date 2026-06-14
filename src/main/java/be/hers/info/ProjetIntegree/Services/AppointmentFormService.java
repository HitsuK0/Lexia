package be.hers.info.ProjetIntegree.Services;

/**
 * @authors Rosman Loïs, Quentin Vanderheyden.
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
        Appointment newAppointment = new Appointment();

        newAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        if(beneficiary == null)
            throw new IllegalArgumentException("Bénéficiaire introuvable");
        newAppointment.setBeneficiary(beneficiary);

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

        TimeSlotPunctual newTimeSlotPunctual = new TimeSlotPunctual(
                appointmentDTO.getStartTime(),
                appointmentDTO.getEndTime(),
                appointmentDTO.getStartDate(),
                appointmentDTO.getEndDate()
        );

        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        daoTimeSlotPunctual.create(newTimeSlotPunctual);
        newAppointment.setTimeSlot(newTimeSlotPunctual);

        DAOAppointment daoAppointment = new DAOAppointment();
        return daoAppointment.create(newAppointment);
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

    /**
     * This function is used to update an Appointment with new data.
     * @param appointmentDTO represent an Appointment
     * @param numAppointment is num of the Appointment, we are trying to update
     * @return true if the update went well else false
     * @throws SQLException if the database encounter an issue.
     */
    public boolean updateAppointment(DTOAppointmentForm appointmentDTO, int numAppointment) throws SQLException, BadStatusException {
        boolean isUpdated = false;
        Appointment updatedAppointment = new Appointment();
        Appointment oldAppointment = new DAOAppointment().find(numAppointment);

        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();

        TimeSlotPunctual newTimeSlotPunctual = daoTimeSlotPunctual.find(oldAppointment.getTimeSlot().getNumTimeSlot());
        newTimeSlotPunctual.setStartTime(appointmentDTO.getStartTime());
        newTimeSlotPunctual.setDuration(appointmentDTO.getEndTime());
        newTimeSlotPunctual.setStartDate(appointmentDTO.getStartDate());
        newTimeSlotPunctual.setEndDate(appointmentDTO.getEndDate());
        daoTimeSlotPunctual.update(newTimeSlotPunctual);

        updatedAppointment.setTimeSlot(newTimeSlotPunctual);

        updatedAppointment.setNumAppointment(numAppointment);

        updatedAppointment.setStatus("en attente");
        updatedAppointment.setBeneficiary(new Beneficiary());

        updatedAppointment.setAppointmentLocals(appointmentDTO.getAppointmentLocals());

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        Beneficiary beneficiary = daoBeneficiary.find(appointmentDTO.getNumBeneficiary());
        if(beneficiary != null)
            updatedAppointment.setBeneficiary(beneficiary);

        DAOEstablishment daoEstablishment = new DAOEstablishment();
        Establishment establishment = daoEstablishment.find(appointmentDTO.getNumEstablishment());
        if(establishment != null)
            updatedAppointment.setEstablishment(establishment);

        DAOAppointment daoAppointment = new DAOAppointment();

        List<AcademicSkill> academicSkillListBD = daoAppointment.findListAcademicSkillRequire(numAppointment);

        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        List<AcademicSkill> listAcademicSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumAcademicSkillsNeeded()){
            AcademicSkill academicSkillFound = daoAcademicSkill.find(num);
            if(academicSkillFound != null) {
                listAcademicSkills.add(academicSkillFound);
                if(!academicSkillListBD.contains(academicSkillFound)){
                    daoAppointment.addAcademicSkillAtAppointment(numAppointment, academicSkillFound.getNumAcademicSkill());
                }
            }
        }
        academicSkillListBD.stream().filter(x -> listAcademicSkills.contains(x) == false).toList();
        for(AcademicSkill academicSkill : academicSkillListBD){
            daoAppointment.deleteAcademicSkillAtAppointment(numAppointment, academicSkill.getNumAcademicSkill());
        }

        updatedAppointment.setAcademicSkillsNeeded(listAcademicSkills);

        List<ProfessionalSkill> professionalSkillListBD = daoAppointment.findListProfessionalSkillRequire(numAppointment);

        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();
        List<ProfessionalSkill> listProfessionalSkills = new ArrayList<>();
        for(int num : appointmentDTO.getNumProfessionalSkillsNeeded()){
            ProfessionalSkill professionalSkillFound = daoProfessionalSkill.find(num);

            if(professionalSkillFound != null) {
                listProfessionalSkills.add(professionalSkillFound);
                if(!professionalSkillListBD.contains(professionalSkillFound)){
                    daoAppointment.addProfessionalSkillAtAppointment(numAppointment, professionalSkillFound.getNumProfessionalSkill());
                }
            }
        }
        professionalSkillListBD.stream().filter(x -> listProfessionalSkills.contains(x) == false).toList();
        for(ProfessionalSkill professionalSkill : professionalSkillListBD){
            daoAppointment.deleteProfessionalSkillAtAppointment(numAppointment, professionalSkill.getNumProfessionalSkill());
        }
        updatedAppointment.setProfessionalSkillsNeeded(listProfessionalSkills);

        daoAppointment.deleteInRDVInterpreter(updatedAppointment);
        if(daoAppointment.update(updatedAppointment))
            isUpdated = true;

        return isUpdated;
    }
}

