package be.hers.info.ProjetIntegree.Services;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.POJO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ValidationService {

    /**
     * Finds an Appointment by its numAppointment.
     *
     * @param numAppointment the id of the appointment to find
     * @return the Appointment if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Appointment findAppointmentById(int numAppointment) throws SQLException {
        return new DAOAppointment().find(numAppointment);
    }

    /**
     * Finds an Absence by its numAbsence.
     *
     * @param numAbsence the id of the absence to find
     * @return the Absence if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Absence findAbsenceById(int numAbsence) throws SQLException {
        return new DAOAbsence().find(numAbsence);
    }

    /**
     * Returns all pending appointment requests (status = "en attente"), across all beneficiaries.
     * Used to populate the Bénéficiaires tab on the validations page.
     *
     * @return a list of Appointments with status "en attente", or an empty list if none found
     * @throws SQLException if a database error occurs
     */
    public List<Appointment> findAllPendingAppointments() throws SQLException {
        DAOAppointment daoAppointment = new DAOAppointment();
        return daoAppointment.findAllRequestsByOptionalStatus("en attente");
    }

    /**
     * Returns all pending absence requests (status = "en attente"), across all interpreters.
     * Used to populate the Interprètes tab on the validations page.
     *
     * @return a list of Absences with status "en attente", or an empty list if none found
     * @throws SQLException       if a database error occurs
     * @throws BadStatusException if an absence status in the database is invalid
     */
    public List<Absence> findAllPendingAbsences() throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        return daoAbsence.findAllPendingAbsences();
    }

    /**
     * Returns the list of interpreters available for the given appointment's time slot.
     * An interpreter is considered available if they have no conflicting absence or appointment
     * on the same date and overlapping time range.
     * The referent of the beneficiary is placed first in the list if available.
     *
     * @param appointment the appointment whose time slot is used as reference
     * @return a list of available Interpreters, or an empty list if none are available
     * @throws SQLException if a database error occurs
     */
    public List<Interpreter> findAvailableInterpretersForAppointment(Appointment appointment) throws SQLException, BadStatusException {
        if (!(appointment.getTimeSlot() instanceof TimeSlotPunctual))
            return new ArrayList<>();
        TimeSlotPunctual tsp = (TimeSlotPunctual) appointment.getTimeSlot();

        String date = tsp.getStartDate().toString();
        String dateEnd = tsp.getEndDate().toString();

        int dayOfWeek = tsp.getStartDate().getDayOfWeek().getValue();

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        List<Interpreter> allInterpreters = daoInterpreter.findAll();
        List<Interpreter> available = new ArrayList<>();

        for (Interpreter interpreter : allInterpreters) {
            List<Appointment> conflicts = new DAOAppointment().findAllAppointmentToInterpreterAndDate(interpreter, date, dateEnd);
            List<Absence> absenceConflicts = new DAOAbsence().findPunctualAbsencesInterpreter(interpreter);
            List<Absence> baseAbsenceConflicts = new DAOAbsence().findBaseAbsencesInterpreter(interpreter);

            boolean hasConflict = conflicts.stream().anyMatch(a -> {
                if (!(a.getTimeSlot() instanceof TimeSlotPunctual))
                    return false;
                TimeSlotPunctual other = (TimeSlotPunctual) a.getTimeSlot();
                return !a.getStatus().equals("refuse") && overlaps(tsp, other);
            });

            boolean hasBaseConflict = conflicts.stream().anyMatch(a -> {
                if (!(a.getTimeSlot() instanceof TimeSlotBase))
                    return false;
                TimeSlotBase other = (TimeSlotBase) a.getTimeSlot();
                return other.getDayNumber() == dayOfWeek && overlapsBase(tsp, other);
            });

            boolean hasAbsenceConflict = absenceConflicts.stream().anyMatch(ab -> {
                if (!(ab.getTimeSlot() instanceof TimeSlotPunctual))
                    return false;
                TimeSlotPunctual other = (TimeSlotPunctual) ab.getTimeSlot();
                return !ab.getStatus().equals("refuse") && overlaps(tsp, other);
            });

            boolean hasBaseAbsenceConflict = baseAbsenceConflicts.stream().anyMatch(ab -> {
                if (!(ab.getTimeSlot() instanceof TimeSlotBase))
                    return false;
                TimeSlotBase other = (TimeSlotBase) ab.getTimeSlot();
                return other.getDayNumber() == dayOfWeek && overlapsBase(tsp, other);
            });

            if (!hasConflict && !hasBaseConflict && !hasAbsenceConflict && !hasBaseAbsenceConflict) {
                available.add(interpreter);
            }
        }

        if (appointment.getBeneficiary() != null && appointment.getBeneficiary().getInterpreter() != null) {
            int referentId = appointment.getBeneficiary().getInterpreter().getNumInterpreter();
            available.sort((a, b) -> {
                if (a.getNumInterpreter() == referentId) return -1;
                if (b.getNumInterpreter() == referentId) return 1;
                return a.getLastName().compareToIgnoreCase(b.getLastName());
            });
        }

        return available;
    }

    /**
     * Checks whether two TimeSlotPunctual instances overlap in time.
     * Two slots overlap if one starts before the other ends.
     *
     * @param a the first time slot
     * @param b the second time slot
     * @return true if the two slots overlap, false otherwise
     */
    private boolean overlaps(TimeSlotPunctual a, TimeSlotPunctual b) {
        if (!a.getStartDate().equals(b.getStartDate()))
            return false;
        long aStart = a.getStartTime().toSecondOfDay();
        long aEnd = aStart + a.getDuration().toSecondOfDay();
        long bStart = b.getStartTime().toSecondOfDay();
        long bEnd = bStart + b.getDuration().toSecondOfDay();
        return aStart < bEnd && bStart < aEnd;
    }

    /**
     * Checks whether a TimeSlotPunctual overlaps with a TimeSlotBase on the same day.
     * Uses seconds of day for comparison.
     *
     * @param punctual the punctual time slot (the requested appointment)
     * @param base     the recurring base time slot
     * @return true if the two slots overlap in time, false otherwise
     */
    private boolean overlapsBase(TimeSlotPunctual punctual, TimeSlotBase base) {
        long pStart = punctual.getStartTime().toSecondOfDay();
        long pEnd = pStart + punctual.getDuration().toSecondOfDay();
        long bStart = base.getStartTime().toSecondOfDay();
        long bEnd = bStart + base.getDuration().toSecondOfDay();
        return pStart < bEnd && bStart < pEnd;
    }

    /**
     * Accepts an appointment request by assigning the selected interpreter and changing the status to "accepte".
     * The appointment must currently have status "en attente".
     *
     * @param numAppointment the id of the appointment to accept
     * @param numInterpreter the id of the interpreter to assign
     * @return true if the appointment was successfully updated, false otherwise
     * @throws SQLException       if a database error occurs
     * @throws BadStatusException if the status transition is invalid
     */
    public boolean acceptAppointment(int numAppointment, int numInterpreter) throws SQLException, BadStatusException {
        DAOAppointment daoAppointment = new DAOAppointment();
        Appointment appointment = daoAppointment.find(numAppointment);
        if (appointment == null || !appointment.getStatus().equals("en attente"))
            return false;

        DAOInterpreter daoInterpreter = new DAOInterpreter();
        Interpreter interpreter = daoInterpreter.find(numInterpreter);
        if (interpreter == null)
            return false;

        List<Interpreter> interpreters = appointment.getInterpreters() != null
                ? new ArrayList<>(appointment.getInterpreters())
                : new ArrayList<>();
        interpreters.add(interpreter);
        appointment.setInterpreters(interpreters);
        appointment.setStatus("accepte");

        return daoAppointment.update(appointment);
    }

    /**
     * Refuses an appointment request by changing its status to "refuse".
     * The appointment must currently have status "en attente".
     *
     * @param numAppointment the id of the appointment to refuse
     * @return true if the appointment was successfully updated, false otherwise
     * @throws SQLException       if a database error occurs
     * @throws BadStatusException if the status transition is invalid
     */
    public boolean refuseAppointment(int numAppointment) throws SQLException, BadStatusException {
        DAOAppointment daoAppointment = new DAOAppointment();
        Appointment appointment = daoAppointment.find(numAppointment);
        if (appointment == null || !appointment.getStatus().equals("en attente"))
            return false;

        appointment.setStatus("refuse");
        return daoAppointment.update(appointment);
    }

    /**
     * Accepts an absence request by changing its status to "accepte".
     * The absence must currently have status "en attente".
     *
     * @param numAbsence the id of the absence to accept
     * @return true if the absence was successfully updated, false otherwise
     * @throws SQLException       if a database error occurs
     * @throws BadStatusException if the status transition is invalid
     */
    public boolean acceptAbsence(int numAbsence) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        Absence absence = daoAbsence.find(numAbsence);
        if (absence == null || !absence.getStatus().equals("en attente"))
            return false;

        absence.setStatus("accepte");
        return daoAbsence.update(absence);
    }

    /**
     * Refuses an absence request by changing its status to "refuse".
     * The absence must currently have status "en attente".
     *
     * @param numAbsence the id of the absence to refuse
     * @return true if the absence was successfully updated, false otherwise
     * @throws SQLException       if a database error occurs
     * @throws BadStatusException if the status transition is invalid
     */
    public boolean refuseAbsence(int numAbsence) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        Absence absence = daoAbsence.find(numAbsence);
        if (absence == null || !absence.getStatus().equals("en attente"))
            return false;

        absence.setStatus("refuse");
        return daoAbsence.update(absence);
    }
}