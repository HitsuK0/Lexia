package be.hers.info.ProjetIntegree.Services;

import be.hers.info.ProjetIntegree.DAO.DAOAppointment;
import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Halet Louis
 * @reviewer Nicolas Jean-Francois
 */
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    /**
     * Returns all appointment requests submitted by the given Beneficiary, optionally filtered by status.
     *
     * @param beneficiary the Beneficiary whose requests are being retrieved
     * @param status      the status to filter on ("accepte", "refuse", "en attente"), or null/empty for no filter
     * @return the list of appointment requests matching the criteria, empty list if none
     */
    public List<Appointment> findRequestsForBeneficiary(Beneficiary beneficiary, String status) {
        List<Appointment> appointmentList = new ArrayList<>();
        DAOAppointment daoAppointment = new DAOAppointment();
        try {
            appointmentList = daoAppointment.findAllRequestsByBeneficiaryAndOptionalStatus(beneficiary.getNumBeneficiary(), status);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recherche des demandes pour le bénéficiaire {}", beneficiary.getNumBeneficiary(), e);
        }
        return appointmentList;
    }

    /**
     * Deletes the appointment request identified by numAppointment.
     * The request is only deleted if it exists and its status is "en attente".
     *
     * @param numAppointment the id of the request to delete
     * @throws SQLException       if a database access error occurs
     * @throws BadStatusException if a status read from the database is invalid
     */
    public void deleteAppointmentRequest(int numAppointment) throws SQLException, BadStatusException {
        DAOAppointment daoAppointment = new DAOAppointment();
        Appointment appointment = daoAppointment.find(numAppointment);

        if (appointment != null && appointment.getStatus().equals("en attente")) {
            daoAppointment.delete(appointment);
        }
    }
}
