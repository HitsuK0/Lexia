package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.Interpreter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbsenceService {

    public void deleteAbsence(int numAbsence) throws SQLException {
        DAOAbsence daoAbsence = new DAOAbsence();
        Absence absenceToDelete = new Absence();

        absenceToDelete.setNumAbsence(numAbsence);

        daoAbsence.delete(absenceToDelete);
    }

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
