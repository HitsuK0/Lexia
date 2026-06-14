package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.POJO.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-francois
 */
public class PlanningService {

    private static final Logger logger = LoggerFactory.getLogger(PlanningService.class);

    /**
     * Searches for all Appointments belonging to the interpreter as a parameter over a period defined by start and end.
     *
     * @param inter The interpreter linked to the appointment on the list
     * @param start the date retrieved via the URL
     * @param end   the date retrieved via the URL
     * @return The appointment list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Appointment> getListAppointmentWithDateAndInterpreter(Interpreter inter, String start, String end) {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> list = new ArrayList<>();
        try {
            list = daoAppointment.findAllAppointmentToInterpreterAndDate(inter, start, end);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des RDV pour l'interprète {} entre {} et {}", inter.getNumInterpreter(), start, end, e);
        }
        return list;
    }

    /**
     * Searches for all Appointments belonging to the numInterpreter as a parameter over a period defined by start and end.
     *
     * @param numInterpreter The numero of Interpreter linked to the appointment on the list
     * @param start          the date retrieved via the URL
     * @param end            the date retrieved via the URL
     * @return The appointment list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Appointment> getListAppointmentWithDateAndInterpreter(int numInterpreter, String start, String end) {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> list = new ArrayList<>();
        try {
            list = daoAppointment.findAllAppointmentToInterpreterAndDate(numInterpreter, start, end);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des RDV pour l'interprète {} entre {} et {}", numInterpreter, start, end, e);
        }
        return list;
    }

    /**
     * Searches for all Absences belonging to the interpreter as a parameter over a period defined by start and end.
     *
     * @param numInterpreter The Coordinator linked to the appointment on the list
     * @param start          the date retrieved via the URL
     * @param end            the date retrieved via the URL
     * @return The Absences list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getListAbsenceWithDateAndInterpreter(int numInterpreter, String start, String end) {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Absence> list = new ArrayList<>();
        try {
            list = daoAppointment.findAllAbsenceToInterpreterAndDate(numInterpreter, start, end);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des absences pour l'interprète {} entre {} et {}", numInterpreter, start, end, e);
        }
        return list;
    }

    /**
     * Searches for all Absences belonging to the interpreter as a parameter over a period defined by start and end.
     *
     * @param inter The interpreter linked to the appointment on the list
     * @param start the date retrieved via the URL
     * @param end   the date retrieved via the URL
     * @return The Absences list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Absence> getListAbsenceWithDateAndInterpreter(Interpreter inter, String start, String end) {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Absence> list = new ArrayList<>();
        try {
            list = daoAppointment.findAllAbsenceToInterpreterAndDate(inter, start, end);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des absences pour l'interprète {} entre {} et {}", inter.getNumInterpreter(), start, end, e);
        }
        return list;
    }

    /**
     * Searches for all Appointments belonging to the beneficiary as a parameter over a period defined by start and end.
     *
     * @param numBeneficiary The beneficiary linked to the appointment on the list
     * @param start          the date retrieved via the URL
     * @param end            the date retrieved via the URL
     * @return The Absences list meets the constraints; an empty list is returned if no object is found.
     */
    public List<Appointment> getListAppointmentsToBeneficiaryAndDate(int numBeneficiary, String start, String end) {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> list = new ArrayList<>();
        try {
            list = daoAppointment.findAllAppointmentToBeneficiaryAndDate(numBeneficiary, start, end);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des RDV pour le bénéficiaire {} entre {} et {}", numBeneficiary, start, end, e);
        }
        return list;
    }

    /**
     * Searches for all Beneficiary link at Interpreter.
     *
     * @param numInterpreter The numInterpreter
     * @return The list of Beneficiary if found, an empty list otherwise
     */
    public List<Beneficiary> getListBeneficiaryRefererInterpreter(int numInterpreter) {
        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        List<Beneficiary> list = new ArrayList<>();
        try {
            list = daoBeneficiary.findByRefInterpreter(numInterpreter);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des bénéficiaires pour l'interprète référent {}", numInterpreter, e);
        }
        return list;
    }

    /**
     * Update the status of Appointment
     *
     * @param numAppointment The numAppointment
     * @param Status         The status to update
     */
    public void changeStatusAppointment(int numAppointment, String Status) {
        DAOAppointment daoAppointment = new DAOAppointment();
        try {
            Appointment a = daoAppointment.find(numAppointment);
            if (a != null) {
                a.setStatus(Status);
                daoAppointment.update(a);
            }

        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors du changement de statut du RDV {}", numAppointment, e);
        }
    }
}
