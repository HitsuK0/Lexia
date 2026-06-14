package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.ValidationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
@Controller
@RequestMapping("/coordinatrice/validations")
public class ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(ValidationController.class);

    /**
     * Retrieves the connected coordinator from the session.
     * Returns null if no user is connected or if the connected user is not a Coordinator.
     *
     * @param session the current HTTP session
     * @return the connected Coordinator, or null if not found
     */
    private Coordinator getCoordinatorFromSession(HttpSession session) {
        if (session == null)
            return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Coordinator)
            return (Coordinator) user;
        return null;
    }

    /**
     * Displays the Validations page for the coordinator.
     * Loads all pending appointment requests (onglet Bénéficiaires) and all pending absence requests (onglet Interprètes).
     * Redirects to login if no coordinator is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "coordinatrice/validations", or a redirect to "/login"
     */
    @GetMapping
    public String validations(HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null)
            return "redirect:/login";

        try {
            ValidationService service = new ValidationService();

            model.addAttribute("pendingAppointments", service.findAllPendingAppointments());
            model.addAttribute("pendingAbsences", service.findAllPendingAbsences());

        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors du chargement de la page validations", e);
            model.addAttribute("pendingAppointments", Collections.emptyList());
            model.addAttribute("pendingAbsences", Collections.emptyList());
        }

        return "coordinatrice/validations";
    }

    /**
     * Returns the list of available interpreters for a given appointment, in JSON format.
     * Used by the JS to populate the interpreter selection table when a card is expanded.
     * Each entry contains numInterpreter, lastName, firstName, professionalSkills and academicSkills.
     * Redirects to an empty list if no coordinator is found in session.
     *
     * @param numAppointment the id of the appointment
     * @param session        the current HTTP session
     * @return a list of maps with interpreter data, or an empty list
     */
    @GetMapping(value = "/appointment/{numAppointment}/interpreters", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getAvailableInterpreters(@PathVariable int numAppointment, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return Collections.emptyList();

        try {
            ValidationService service = new ValidationService();

            Appointment appointment = service.findAppointmentById(numAppointment);
            if (appointment == null) return Collections.emptyList();

            List<Interpreter> available = service.findAvailableInterpretersForAppointment(appointment);

            int referentId = appointment.getBeneficiary() != null && appointment.getBeneficiary().getInterpreter() != null
                    ? appointment.getBeneficiary().getInterpreter().getNumInterpreter()
                    : -1;

            return available.stream().map(i -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("numInterpreter", i.getNumInterpreter());
                entry.put("lastName", i.getLastName().toUpperCase());
                entry.put("firstName", i.getFirstName());
                entry.put("isReferent", i.getNumInterpreter() == referentId);
                entry.put("professionalSkills", i.getProfessionalSkillsList() != null
                        ? i.getProfessionalSkillsList().stream().map(ProfessionalSkill::getDesignation).collect(Collectors.joining(", "))
                        : "");
                entry.put("academicSkills", i.getAcademicSkillsList() != null
                        ? i.getAcademicSkillsList().stream().map(AcademicSkill::getDesignation).collect(Collectors.joining(", "))
                        : "");
                return entry;
            }).collect(Collectors.toList());

        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors du chargement des interprètes disponibles pour le RDV {}", numAppointment, e);
            return Collections.emptyList();
        }
    }

    /**
     * Returns the FullCalendar event for a given appointment, in JSON format.
     * The event is placed on the appointment's date and displayed in orange (pending color).
     * Redirects to an empty list if no coordinator is found in session.
     *
     * @param numAppointment the id of the appointment
     * @param session        the current HTTP session
     * @return a list containing one event map, or an empty list
     */
    @GetMapping(value = "/appointment/{numAppointment}/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getAppointmentEvent(@PathVariable int numAppointment, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return Collections.emptyList();

        try {
            ValidationService service = new ValidationService();
            Appointment appointment = service.findAppointmentById(numAppointment);
            if (appointment == null || !(appointment.getTimeSlot() instanceof TimeSlotPunctual))
                return Collections.emptyList();

            TimeSlotPunctual tsp = (TimeSlotPunctual) appointment.getTimeSlot();
            LocalDateTime start = LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
            LocalDateTime end = start.plusSeconds(tsp.getDuration().toSecondOfDay());

            String acadSkills = appointment.getAcademicSkillsNeeded() != null
                    ? appointment.getAcademicSkillsNeeded().stream().map(AcademicSkill::getDesignation).collect(Collectors.joining(", "))
                    : "";

            Map<String, Object> event = new HashMap<>();
            event.put("title", acadSkills);
            event.put("start", start);
            event.put("end", end);
            event.put("color", "#f0ad4e");

            return List.of(event);

        } catch (SQLException e) {
            logger.error("Erreur lors du chargement de l'événement pour le RDV {}", numAppointment, e);
            return Collections.emptyList();
        }
    }

    /**
     * Returns the FullCalendar event for a given absence, in JSON format.
     * Full-day absences are returned as allDay events.
     * The event is displayed in orange (pending color).
     * Redirects to an empty list if no coordinator is found in session.
     *
     * @param numAbsence the id of the absence
     * @param session    the current HTTP session
     * @return a list containing one event map, or an empty list
     */
    @GetMapping(value = "/absence/{numAbsence}/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getAbsenceEvent(@PathVariable int numAbsence, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return Collections.emptyList();

        try {
            ValidationService service = new ValidationService();
            Absence absence = service.findAbsenceById(numAbsence);
            if (absence == null || !(absence.getTimeSlot() instanceof TimeSlotPunctual))
                return Collections.emptyList();

            TimeSlotPunctual tsp = (TimeSlotPunctual) absence.getTimeSlot();
            boolean isFullDay = tsp.getStartTime().equals(LocalTime.MIDNIGHT) && tsp.getDuration().toSecondOfDay() == 0;

            Map<String, Object> event = new HashMap<>();
            event.put("title", "Indisponibilité");
            event.put("color", "#f0ad4e");

            if (isFullDay) {
                event.put("start", tsp.getStartDate().toString());
                event.put("end", tsp.getEndDate().plusDays(1).toString());
                event.put("allDay", true);
            } else {
                LocalDateTime start = LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                LocalDateTime end = start.plusSeconds(tsp.getDuration().toSecondOfDay());
                event.put("start", start);
                event.put("end", end);
            }

            return List.of(event);

        } catch (SQLException e) {
            logger.error("Erreur lors du chargement de l'événement pour l'absence {}", numAbsence, e);
            return Collections.emptyList();
        }
    }

    /**
     * Accepts a pending appointment request.
     * Assigns the selected interpreter and changes the appointment status to "accepte".
     * Returns "ok" on success, "error" on failure, "unauthorized" if session is invalid.
     *
     * @param numAppointment the id of the appointment to accept
     * @param numInterpreter the id of the interpreter to assign
     * @param session        the current HTTP session
     * @return "ok", "error", or "unauthorized"
     */
    @PostMapping(value = "/appointment/{numAppointment}/accept", produces = "application/json")
    @ResponseBody
    public String acceptAppointment(@PathVariable int numAppointment, @RequestParam int numInterpreter, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            ValidationService service = new ValidationService();
            return service.acceptAppointment(numAppointment, numInterpreter) ? "ok" : "error";
        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors de l'acceptation du RDV {}", numAppointment, e);
            return "error";
        }
    }

    /**
     * Refuses a pending appointment request.
     * Changes the appointment status to "refuse".
     * Returns "ok" on success, "error" on failure, "unauthorized" if session is invalid.
     *
     * @param numAppointment the id of the appointment to refuse
     * @param session        the current HTTP session
     * @return "ok", "error", or "unauthorized"
     */
    @PostMapping(value = "/appointment/{numAppointment}/refuse", produces = "application/json")
    @ResponseBody
    public String refuseAppointment(@PathVariable int numAppointment, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            ValidationService service = new ValidationService();
            return service.refuseAppointment(numAppointment) ? "ok" : "error";
        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors du refus du RDV {}", numAppointment, e);
            return "error";
        }
    }

    /**
     * Accepts a pending absence request.
     * Changes the absence status to "accepte".
     * Returns "ok" on success, "error" on failure, "unauthorized" if session is invalid.
     *
     * @param numAbsence the id of the absence to accept
     * @param session    the current HTTP session
     * @return "ok", "error", or "unauthorized"
     */
    @PostMapping(value = "/absence/{numAbsence}/accept", produces = "application/json")
    @ResponseBody
    public String acceptAbsence(@PathVariable int numAbsence, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            ValidationService service = new ValidationService();
            return service.acceptAbsence(numAbsence) ? "ok" : "error";
        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors de l'acceptation de l'absence {}", numAbsence, e);
            return "error";
        }
    }

    /**
     * Refuses a pending absence request.
     * Changes the absence status to "refuse".
     * Returns "ok" on success, "error" on failure, "unauthorized" if session is invalid.
     *
     * @param numAbsence the id of the absence to refuse
     * @param session    the current HTTP session
     * @return "ok", "error", or "unauthorized"
     */
    @PostMapping(value = "/absence/{numAbsence}/refuse", produces = "application/json")
    @ResponseBody
    public String refuseAbsence(@PathVariable int numAbsence, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            ValidationService service = new ValidationService();
            return service.refuseAbsence(numAbsence) ? "ok" : "error";
        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors du refus de l'absence {}", numAbsence, e);
            return "error";
        }
    }
}