package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAbsence;
import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.TimeSlotPunctual;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service link to the Absence.
 */
public class AbsenceService {

    /**
     * It creates an Absence in the database using the data in the absenceDTO given in param.
     * @param absenceDTO is the DTOAbsence used by spring to copy the data in the form.
     */
    public void createAbsence(DTOAbsence absenceDTO, int numInterpreter) throws BadStatusException, SQLException {
        Absence absence = new Absence();
        absence.setReason(absenceDTO.getReason());
        absence.setStatus("en attente");
        absence.setPrivateReason(absenceDTO.isPrivateReason());
        TimeSlotPunctual timeSlotPunctual =  new TimeSlotPunctual();
        timeSlotPunctual.setStartDate(absenceDTO.getStartDate());
        timeSlotPunctual.setEndDate(absenceDTO.getEndDate());
        LocalTime duration;
        if(absenceDTO.isFullDay()){
            timeSlotPunctual.setStartTime(LocalTime.MIDNIGHT);
            duration = LocalTime.MIDNIGHT.plus(Duration.ofHours(24));
        }
        else{
            timeSlotPunctual.setStartTime(absenceDTO.getStartTime());
            Duration d = Duration.between(absenceDTO.getStartTime(), absenceDTO.getEndTime());
            duration = LocalTime.MIDNIGHT.plus(d);
        }
        timeSlotPunctual.setDuration(duration);

        absence.setTimeSlot(timeSlotPunctual);
        DAOAbsence daoAbsence = new DAOAbsence();
        daoAbsence.create(absence, numInterpreter);
    }

    /**
     * Deletes the absence identified by the given number
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
     * @param absenceToUpdate the absence carrying the new values
     * @throws SQLException if a database access error occurs
     */
    public void updateAbsence(Absence absenceToUpdate) throws SQLException {
        DAOAbsence daoAbsence = new DAOAbsence();

        if(absenceToUpdate.getNumAbsence() == -1) {
            return;
        }

        daoAbsence.update(absenceToUpdate);
    }

    /**
     * Searches for all non-repetitive Absences belonging to the interpreter
     * as a parameter over a period defined by start and end
     * @param interpreter The interpreter linked to the Absence
     * @param startDate the date retrieved via the URL
     * @param endDate the date retrieved via the URL
     * @return The Absence list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getPunctualAbsencesInterpreter(Interpreter interpreter, String startDate, String endDate) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();

        if(interpreter.getNumInterpreter() == -1) {
            return new ArrayList<>();
        }

        return daoAbsence.findPunctualAbsencesInterpreter(interpreter, startDate, endDate);
    }

    /**
     * Searches for all repetitive Absences belonging to the interpreter as a parameter
     * @param interpreter The interpreter linked to the Absence
     * @return The Absence list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getBaseAbsencesInterpreter(Interpreter interpreter) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        if(interpreter.getNumInterpreter() == -1) {
            return new ArrayList<>();
        }

        return daoAbsence.findBaseAbsencesInterpreter(interpreter);
    }
}
