package be.hers.info.ProjetIntegree.Services;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */

import be.hers.info.ProjetIntegree.DAO.*;
import be.hers.info.ProjetIntegree.POJO.*;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class HoraireBaseService {

    /**
     * Retrieves all Appointments with a TimeSlotBase linked to the given interpreter.
     * Uses a wide date range to ensure all base appointments are returned regardless of date.
     *
     * @param interpreter the interpreter whose base appointments are requested
     * @return a list of Appointments with a TimeSlotBase, or an empty list if none found
     * @throws SQLException if a database error occurs
     */
    public List<Appointment> getBaseAppointmentsForInterpreter(Interpreter interpreter) throws SQLException {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> all = daoAppointment.findAllAppointmentToInterpreterAndDate(interpreter, "2000-01-01", "2099-12-31");
        return all.stream()
                .filter(a -> a.getTimeSlot() instanceof TimeSlotBase)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all recurring Absences (with a TimeSlotBase) linked to the given interpreter.
     *
     * @param interpreter the interpreter whose base absences are requested
     * @return a list of Absences with a TimeSlotBase, or an empty list if none found
     * @throws SQLException if a database error occurs
     * @throws BadStatusException if an absence status is invalid
     */
    public List<Absence> getBaseAbsencesForInterpreter(Interpreter interpreter) throws SQLException, BadStatusException {
        DAOAbsence daoAbsence = new DAOAbsence();
        return daoAbsence.findBaseAbsencesInterpreter(interpreter);
    }

    /**
     * Retrieves all Appointments with a TimeSlotBase linked to the given beneficiary.
     * Uses a wide date range to ensure all base appointments are returned regardless of date.
     *
     * @param numBeneficiary the id of the beneficiary
     * @return a list of Appointments with a TimeSlotBase, or an empty list if none found
     * @throws SQLException if a database error occurs
     */
    public List<Appointment> getBaseAppointmentsForBeneficiary(int numBeneficiary) throws SQLException {
        DAOAppointment daoAppointment = new DAOAppointment();
        List<Appointment> all = daoAppointment.findAllAppointmentToBeneficiaryAndDate(numBeneficiary, "2000-01-01", "2099-12-31");
        return all.stream()
                .filter(a -> a.getTimeSlot() instanceof TimeSlotBase)
                .collect(Collectors.toList());
    }

    /**
     * Returns the sorted list of interpreters suggested for a beneficiary's add-slot modal.
     * Order:
     * - Priority 0: the beneficiary's referent interpreter is always first, with "(Référent)" appended to the label.
     * - Priority 3: interpreters whose address locality matches any of the beneficiary's communication languages come next.
     * - The rest are sorted alphabetically by last name.
     * Note: since Interpreter has no explicit language field, the address locality is used as a best-effort proxy.
     *
     * @param beneficiary   the beneficiary for whom the interpreter list is built
     * @param allInterpreters the full list of interpreters to sort
     * @return a list of maps, each containing "numInterpreter" and "label"
     */
    public List<Map<String, Object>> getSuggestedInterpreters(Beneficiary beneficiary, List<Interpreter> allInterpreters) {
        int referentId = beneficiary.getInterpreter() != null ? beneficiary.getInterpreter().getNumInterpreter() : -1;

        List<String> benefLangs = beneficiary.getCommunicationLanguage() != null ? beneficiary.getCommunicationLanguage().stream()
                .map(String::toLowerCase).collect(Collectors.toList())
                : Collections.emptyList();

        List<Interpreter> sorted = new ArrayList<>(allInterpreters);
        sorted.sort((a, b) -> {
            // Priority 0 — referent always first
            if (a.getNumInterpreter() == referentId) return -1;
            if (b.getNumInterpreter() == referentId) return 1;

            // Priority 3 — language match via address locality
            boolean aMatches = false;
            boolean bMatches = false;
            if (!benefLangs.isEmpty()) {
                if (a.getAddress() != null && a.getAddress().getLocality() != null) {
                    aMatches = benefLangs.stream()
                            .anyMatch(lang -> a.getAddress().getLocality().toLowerCase().contains(lang));
                }
                if (b.getAddress() != null && b.getAddress().getLocality() != null) {
                    bMatches = benefLangs.stream()
                            .anyMatch(lang -> b.getAddress().getLocality().toLowerCase().contains(lang));
                }
            }
            if (aMatches && !bMatches)
                return -1;
            if (!aMatches && bMatches)
                return 1;

            return a.getLastName().compareToIgnoreCase(b.getLastName());
        });

        List<Map<String, Object>> result = new ArrayList<>();
        for (Interpreter interp : sorted) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("numInterpreter", interp.getNumInterpreter());
            String label = interp.getLastName().toUpperCase() + " " + interp.getFirstName();
            if (interp.getNumInterpreter() == referentId) {
                label += " (Référent)";
            }
            entry.put("label", label);
            result.add(entry);
        }
        return result;
    }

    /**
     * Creates a recurring absence (TimeSlotBase) for the given interpreter.
     * First inserts the TimeSlotBase, then creates the Absence linked to it.
     * The absence status is set to "accepte" since it is created by the coordinator.
     *
     * @param numInterpreter the id of the interpreter
     * @param dayNumber the day of the week (1=Monday ... 7=Sunday)
     * @param startTime the start time
     * @param endTime the end time (used to compute duration)
     * @return true if the absence was successfully created, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createBaseAbsence(int numInterpreter, int dayNumber, LocalTime startTime, LocalTime endTime) throws SQLException {
        LocalTime duration = LocalTime.ofSecondOfDay(endTime.toSecondOfDay() - startTime.toSecondOfDay());
        TimeSlotBase tsb = new TimeSlotBase(startTime, duration, dayNumber);

        DAOTimeSlotBase daoTSB = new DAOTimeSlotBase();
        if (!daoTSB.create(tsb))
            return false;

        Absence absence = new Absence();
        absence.setTimeSlot(tsb);
        try {
            absence.setStatus("accepte");
        } catch (BadStatusException e) {
            e.printStackTrace();
            return false;
        }
        absence.setPrivateReason(false);

        DAOAbsence daoAbsence = new DAOAbsence();
        return daoAbsence.create(absence, numInterpreter);
    }

    /**
     * Creates a base appointment slot for an interpreter.
     * First inserts the TimeSlotBase, then creates the Appointment linked to the interpreter, beneficiary and establishment.
     * The appointment status is set to "accepte" since it is created by the coordinator.
     *
     * @param numInterpreter the id of the interpreter to assign
     * @param numBeneficiary the id of the beneficiary
     * @param numEstablishment the id of the establishment
     * @param dayNumber the day of the week (1=Monday ... 7=Sunday)
     * @param startTime the start time
     * @param endTime the end time (used to compute duration)
     * @param local the local (room), may be null
     * @param description the description, may be null
     * @return true if the appointment was successfully created, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createBaseAppointmentForInterpreter(int numInterpreter, int numBeneficiary, int numEstablishment, int dayNumber, LocalTime startTime, LocalTime endTime,
                                                       String local, String description) throws SQLException {
        LocalTime duration = LocalTime.ofSecondOfDay(endTime.toSecondOfDay() - startTime.toSecondOfDay());
        TimeSlotBase tsb = new TimeSlotBase(startTime, duration, dayNumber);

        DAOTimeSlotBase daoTSB = new DAOTimeSlotBase();
        if (!daoTSB.create(tsb))
            return false;

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        DAOInterpreter daoInterpreter = new DAOInterpreter();

        Beneficiary beneficiary = daoBeneficiary.find(numBeneficiary);
        Establishment establishment = daoEstablishment.find(numEstablishment);
        Interpreter interpreter = daoInterpreter.find(numInterpreter);

        if (beneficiary == null || establishment == null || interpreter == null)
            return false;

        List<String> locals = local != null && !local.isBlank() ? Arrays.asList(local.split(",")) : null;

        Appointment appointment = new Appointment();
        appointment.setDescription(description);
        try {
            appointment.setStatus("accepte");
        } catch (BadStatusException e) {
            e.printStackTrace();
            return false;
        }
        appointment.setAppointmentLocals(locals);
        appointment.setBeneficiary(beneficiary);
        appointment.setEstablishment(establishment);
        appointment.setTimeSlot(tsb);
        appointment.setInterpreters(List.of(interpreter));
        appointment.setAcademicSkillsNeeded(Collections.emptyList());
        appointment.setProfessionalSkillsNeeded(Collections.emptyList());

        DAOAppointment daoAppointment = new DAOAppointment();
        return daoAppointment.create(appointment);
    }

    /**
     * Creates a base appointment slot for a beneficiary.
     * First inserts the TimeSlotBase, then creates the Appointment linked to the beneficiary,
     * interpreter, establishment, academic skill and professional skill.
     * The appointment status is set to "accepte" since it is created by the coordinator.
     *
     * @param numBeneficiary the id of the beneficiary
     * @param numInterpreter the id of the interpreter to assign
     * @param numEstablishment the id of the establishment
     * @param numAcademicSkill the id of the required academic skill
     * @param numProfessionalSkill the id of the required professional skill
     * @param dayNumber the day of the week (1=Monday ... 7=Sunday)
     * @param startTime the start time
     * @param endTime the end time (used to compute duration)
     * @param local the local (room)
     * @param description the description, may be null
     * @return true if the appointment was successfully created, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createBaseAppointmentForBeneficiary(int numBeneficiary, int numInterpreter, int numEstablishment, int numAcademicSkill, int numProfessionalSkill,
                                                       int dayNumber, LocalTime startTime, LocalTime endTime, String local, String description) throws SQLException {
        LocalTime duration = LocalTime.ofSecondOfDay(endTime.toSecondOfDay() - startTime.toSecondOfDay());
        TimeSlotBase tsb = new TimeSlotBase(startTime, duration, dayNumber);

        DAOTimeSlotBase daoTSB = new DAOTimeSlotBase();
        if (!daoTSB.create(tsb))
            return false;

        DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
        DAOEstablishment daoEstablishment = new DAOEstablishment();
        DAOInterpreter daoInterpreter = new DAOInterpreter();
        DAOAcademicSkill daoAcademicSkill = new DAOAcademicSkill();
        DAOProfessionalSkill daoProfessionalSkill = new DAOProfessionalSkill();

        Beneficiary beneficiary = daoBeneficiary.find(numBeneficiary);
        Establishment establishment = daoEstablishment.find(numEstablishment);
        Interpreter interpreter = daoInterpreter.find(numInterpreter);
        AcademicSkill academicSkill = daoAcademicSkill.find(numAcademicSkill);
        ProfessionalSkill professionalSkill = daoProfessionalSkill.find(numProfessionalSkill);

        if (beneficiary == null || establishment == null || interpreter == null || academicSkill == null || professionalSkill == null)
            return false;

        List<String> locals = local != null && !local.isBlank() ? Arrays.asList(local.split(",")) : null;

        Appointment appointment = new Appointment();
        appointment.setDescription(description);
        try {
            appointment.setStatus("accepte");
        } catch (BadStatusException e) {
            e.printStackTrace();
            return false;
        }
        appointment.setAppointmentLocals(locals);
        appointment.setBeneficiary(beneficiary);
        appointment.setEstablishment(establishment);
        appointment.setTimeSlot(tsb);
        appointment.setInterpreters(List.of(interpreter));
        appointment.setAcademicSkillsNeeded(List.of(academicSkill));
        appointment.setProfessionalSkillsNeeded(List.of(professionalSkill));

        DAOAppointment daoAppointment = new DAOAppointment();
        return daoAppointment.create(appointment);
    }

    /**
     * Updates an existing TimeSlotBase with a new day, start time and end time.
     * The duration is recomputed from endTime - startTime.
     *
     * @param numTimeSlot the id of the TimeSlotBase to update
     * @param dayNumber the new day number (1=Monday ... 7=Sunday)
     * @param startTime the new start time
     * @param endTime the new end time
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateSlot(int numTimeSlot, int dayNumber, LocalTime startTime, LocalTime endTime) throws SQLException {
        DAOTimeSlotBase daoTSB = new DAOTimeSlotBase();
        TimeSlotBase tsb = daoTSB.find(numTimeSlot);
        if (tsb == null)
            return false;

        LocalTime duration = LocalTime.ofSecondOfDay(endTime.toSecondOfDay() - startTime.toSecondOfDay());
        tsb.setDayNumber(dayNumber);
        tsb.setStartTime(startTime);
        tsb.setDuration(duration);

        return daoTSB.update(tsb);
    }

    /**
     * Deletes a TimeSlotBase by its id.
     * The DB trigger trg_delete_timeslot_base automatically cascades the deletion to linked Absence and Appointment rows.
     *
     * @param numTimeSlot the id of the TimeSlotBase to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteSlot(int numTimeSlot) throws SQLException {
        DAOTimeSlotBase daoTSB = new DAOTimeSlotBase();
        TimeSlotBase tsb = daoTSB.find(numTimeSlot);
        if (tsb == null)
            return false;
        return daoTSB.delete(tsb);
    }
}