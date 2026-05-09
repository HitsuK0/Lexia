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
            return; // verificvation de merde mais sais pas quoi mettre, un boolean?
        }

        daoAbsence.update(absenceToUpdate);
    }

    public List<Absence> getPunctualAbsencesInterpreter(Interpreter interpreter, String startDate, String endDate) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();

        if(interpreter.getNumInterpreter() == -1) {
            return new ArrayList<>();
        }

        return daoAbsence.findPunctualAbsencesInterpreter(interpreter, startDate, endDate);
    }

    public List<Absence> getBaseAbsencesInterpreter(Interpreter interpreter) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        // Fonction pas encore utilise mais peut-etre le sera une fois la page pour encoder l'horaire de base ?
        if(interpreter.getNumInterpreter() == -1) {
            return new ArrayList<>();
        }

        return daoAbsence.findBaseAbsencesInterpreter(interpreter);
    }
}
