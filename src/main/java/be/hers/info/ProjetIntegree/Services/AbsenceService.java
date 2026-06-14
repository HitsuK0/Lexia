package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAbsence;
import be.hers.info.ProjetIntegree.DAO.DAOTimeSlotPunctual;
import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service link to the Absence.
 */
public class AbsenceService {

    /**
     * It create an Absence in the database using the data in the absenceDTO given in param.
     *
     * @param absenceDTO     is the DTOAbsence used by spring to copy the data in the form.
     * @param numInterpreter the numero of th interpreter
     * @param status         the status of the absence
     * @throws BadStatusException if the status of the absence isn't correct
     * @throws SQLException       if a database access error occurs
     */
    public void createAbsence(DTOAbsence absenceDTO, int numInterpreter,String status) throws BadStatusException, SQLException {
        if(absenceDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[AbsenceService] La date de l'absence ne peut pas etre dans le passe.");
        }

        Absence absence = new Absence();
        absence.setReason(absenceDTO.getReason());
        absence.setStatus(status);
        absence.setPrivateReason(absenceDTO.isPrivateReason());
        TimeSlotPunctual timeSlotPunctual = new TimeSlotPunctual();
        timeSlotPunctual.setStartDate(absenceDTO.getStartDate());
        timeSlotPunctual.setEndDate(absenceDTO.getEndDate());
        LocalTime duration;
        if (absenceDTO.isFullDay()) {
            timeSlotPunctual.setStartTime(LocalTime.MIDNIGHT);
            duration = LocalTime.of(23, 59, 59);
        } else {
            timeSlotPunctual.setStartTime(absenceDTO.getStartTime());
            Duration d = Duration.between(absenceDTO.getStartTime(), absenceDTO.getEndTime());
            duration = LocalTime.MIDNIGHT.plus(d);
        }
        timeSlotPunctual.setDuration(duration);
        DAOTimeSlotPunctual daoTimeSlotPunctual = new DAOTimeSlotPunctual();
        daoTimeSlotPunctual.create(timeSlotPunctual);
        absence.setTimeSlot(timeSlotPunctual);
        DAOAbsence daoAbsence = new DAOAbsence();
        daoAbsence.create(absence, numInterpreter);
    }

    /**
     * Deletes the absence identified by the given number
     *
     * @param numAbsence the id of the absence to delete
     * @throws SQLException if a database access error occurs
     */
    public void deleteAbsence(int numAbsence) throws SQLException {
        DAOAbsence daoAbsence = new DAOAbsence();
        Absence absenceToDelete = new Absence();

        absenceToDelete.setNumAbsence(numAbsence);

        daoAbsence.delete(absenceToDelete);
    }

    /**
     * Updates an existing absence
     *
     * @param absenceToUpdate the absence carrying the new values
     * @throws SQLException if a database access error occurs
     */
    public void updateAbsence(Absence absenceToUpdate) throws SQLException {
        DAOAbsence daoAbsence = new DAOAbsence();

        if (absenceToUpdate.getNumAbsence() == -1) {
            return;
        }

        daoAbsence.update(absenceToUpdate);
    }

    /**
     * Searches for all non-repetitive Absences belonging to the interpreter
     *
     * @param interpreter The interpreter linked to the Absence
     * @return The Absence list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getPunctualAbsencesInterpreter(Interpreter interpreter) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();

        if (interpreter.getNumInterpreter() == -1) {
            return new ArrayList<>();
        }

        return daoAbsence.findPunctualAbsencesInterpreter(interpreter);
    }

    /**
     * Searches for all repetitive Absences belonging to the interpreter as a parameter
     *
     * @param interpreter The interpreter linked to the Absence
     * @return The Absence list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getBaseAbsencesInterpreter(Interpreter interpreter) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        if (interpreter.getNumInterpreter() == -1) {
            return new ArrayList<>();
        }

        return daoAbsence.findBaseAbsencesInterpreter(interpreter);
    }

    /**
     * Updates an existing absence from a DTOAbsence.
     * Creates a new TimeSlotPunctual from the DTO data, updates the absence to point to it,
     * then deletes the old TimeSlotPunctual.
     * This order ensures the FK constraint is never violated.
     * If startTime is null (disabled selects when fullDay is checked), the absence is treated
     * as a full-day absence regardless of the fullDay flag.
     *
     * @param numAbsence the id of the absence to update
     * @param dto        the DTO containing the new values
     * @throws SQLException       if a database access error occurs
     * @throws BadStatusException if the absence status is invalid
     */
    public void updateAbsenceFromDTO(int numAbsence, DTOAbsence dto) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        Absence absence = daoAbsence.find(numAbsence);
        if (absence == null) return;

        TimeSlotPunctual oldTsp = (TimeSlotPunctual) absence.getTimeSlot();
        DAOTimeSlotPunctual daoTSP = new DAOTimeSlotPunctual();

        TimeSlotPunctual newTsp = new TimeSlotPunctual();
        newTsp.setStartDate(dto.getStartDate());
        newTsp.setEndDate(dto.getEndDate());

        if (dto.isFullDay() || dto.getStartTime() == null) {
            newTsp.setStartTime(LocalTime.MIDNIGHT);
            newTsp.setDuration(LocalTime.of(23, 59, 59));
        } else {
            newTsp.setStartTime(dto.getStartTime());
            Duration d = Duration.between(dto.getStartTime(), dto.getEndTime());
            newTsp.setDuration(LocalTime.MIDNIGHT.plus(d));
        }

        daoTSP.create(newTsp);

        absence.setReason(dto.getReason());
        absence.setPrivateReason(dto.isPrivateReason());
        absence.setTimeSlot(newTsp);
        daoAbsence.update(absence);

        daoTSP.delete(oldTsp);
    }
}
