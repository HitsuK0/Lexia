package be.hers.info.ProjetIntegree.Controller;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */

import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.AppointmentFormService;
import be.hers.info.ProjetIntegree.Services.HoraireBaseService;
import be.hers.info.ProjetIntegree.Services.SkillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coordinatrice/horaire-base")
public class HoraireBaseController {

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
     * Computes the LocalDate of the current week's day matching the given dayNumber.
     * dayNumber follows ISO convention: 1 = Monday, 7 = Sunday.
     *
     * @param dayNumber the day number (1=Monday ... 7=Sunday)
     * @return the LocalDate of that day in the current week
     */
    private LocalDate getDateForDayNumber(int dayNumber) {
        LocalDate today = LocalDate.now();
        int todayDayNumber = today.getDayOfWeek().getValue();
        return today.plusDays(dayNumber - todayDayNumber);
    }

    /**
     * Builds a FullCalendar event map from an Appointment whose TimeSlot is a TimeSlotBase.
     * Places the event on the correct day of the current week using the dayNumber.
     * Includes extendedProps for display: type, beneficiary, establishment, locals,
     * description, academicSkills, professionalSkills and the TimeSlotBase id.
     *
     * @param a the Appointment to convert
     * @return a Map representing the FullCalendar event, or null if the TimeSlot is not a TimeSlotBase
     */
    private Map<String, Object> buildAppointmentEvent(Appointment a) {
        if (!(a.getTimeSlot() instanceof TimeSlotBase))
            return null;
        TimeSlotBase tsb = (TimeSlotBase) a.getTimeSlot();

        LocalDate day = getDateForDayNumber(tsb.getDayNumber());
        LocalDateTime start = LocalDateTime.of(day, tsb.getStartTime());
        LocalDateTime end = start.plusSeconds(tsb.getDuration().toSecondOfDay());

        String acadSkills = a.getAcademicSkillsNeeded() != null
                ? a.getAcademicSkillsNeeded().stream().map(AcademicSkill::getDesignation).collect(Collectors.joining(", ")) : "";
        String profSkills = a.getProfessionalSkillsNeeded() != null
                ? a.getProfessionalSkillsNeeded().stream().map(ProfessionalSkill::getDesignation).collect(Collectors.joining(", ")) : "";

        Map<String, Object> event = new HashMap<>();
        Map<String, Object> extendedProps = new HashMap<>();

        event.put("title", acadSkills.isEmpty() ? profSkills : acadSkills);
        event.put("start", start);
        event.put("end", end);
        event.put("color", "#b39ddb");

        extendedProps.put("type", "appointment");
        extendedProps.put("numTimeSlot", tsb.getNumTimeSlot());
        extendedProps.put("dayNumber", tsb.getDayNumber());
        extendedProps.put("startTime", tsb.getStartTime().toString());
        extendedProps.put("endTime", end.toLocalTime().toString());
        extendedProps.put("isAbsence", false);
        extendedProps.put("academicSkills", acadSkills);
        extendedProps.put("professionalSkills", profSkills);
        extendedProps.put("beneficiary", a.getBeneficiary() != null ? a.getBeneficiary().getLastName().toUpperCase() + " " + a.getBeneficiary().getFirstName() : "");
        extendedProps.put("establishment", a.getEstablishment() != null ? a.getEstablishment().getNameBuilding() : "");
        extendedProps.put("locals", a.getAppointmentLocals());
        extendedProps.put("description", a.getDescription());
        event.put("extendedProps", extendedProps);
        return event;
    }

    /**
     * Builds a FullCalendar event map for a recurring absence (Absence with TimeSlotBase).
     * Places the event as an all-day block on the corresponding day of the current week.
     *
     * @param a the Absence to convert (must have a TimeSlotBase)
     * @return a Map representing the FullCalendar event, or null if the TimeSlot is not a TimeSlotBase
     */
    private Map<String, Object> buildAbsenceEvent(Absence a) {
        if (!(a.getTimeSlot() instanceof TimeSlotBase))
            return null;
        TimeSlotBase tsb = (TimeSlotBase) a.getTimeSlot();

        LocalDate day = getDateForDayNumber(tsb.getDayNumber());
        LocalDateTime start = LocalDateTime.of(day, tsb.getStartTime());
        LocalDateTime end = start.plusSeconds(tsb.getDuration().toSecondOfDay());

        Map<String, Object> event = new HashMap<>();
        Map<String, Object> extendedProps = new HashMap<>();

        event.put("title", "Indisponibilité");
        event.put("start", start);
        event.put("end", end);
        event.put("color", "#f0ad4e");

        extendedProps.put("type", "absence");
        extendedProps.put("numTimeSlot", tsb.getNumTimeSlot());
        extendedProps.put("dayNumber", tsb.getDayNumber());
        extendedProps.put("startTime", tsb.getStartTime().toString());
        extendedProps.put("endTime", end.toLocalTime().toString());
        extendedProps.put("isAbsence", true);
        event.put("extendedProps", extendedProps);
        return event;
    }

    /**
     * Displays the Horaire de Base page for the coordinator.
     * Loads the list of all interpreters and beneficiaries for the dropdowns.
     * Also loads all available establishments, academic skills and professional skills for the add/edit modals.
     * Redirects to login if no coordinator is found in session.
     *
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return the view "coordinatrice/horaire-base", or a redirect to "/login"
     */
    @GetMapping
    public String horaireBase(HttpSession session, Model model) {
        if (getCoordinatorFromSession(session) == null)
            return "redirect:/login";

        try {
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            SkillService skillService = new SkillService();
            AppointmentFormService appointmentFormService = new AppointmentFormService();

            model.addAttribute("interpreterList", daoInterpreter.findAll());
            model.addAttribute("beneficiaryList", daoBeneficiary.findAll());
            model.addAttribute("allAcademicSkills", skillService.getAllAcademicSkills());
            model.addAttribute("allProfessionalSkills", skillService.getAllProfessionalSkills());
            model.addAttribute("establishmentList", appointmentFormService.findAllEstablishments());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "coordinatrice/horaire-base";
    }

    /**
     * Returns the list of TimeSlotBase events for a given interpreter in JSON format.
     * Includes both base appointments and recurring absences.
     * Events are placed on the current week's days using the TimeSlotBase dayNumber.
     * Redirects to an empty list if no coordinator is found in session.
     *
     * @param id the numInterpreter of the interpreter whose schedule is requested
     * @param session the current HTTP session
     * @return a list of FullCalendar event maps, or an empty list if the session is invalid
     */
    @GetMapping(value = "/interprete/{id}/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getEventsInterpreter(@PathVariable int id, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return Collections.emptyList();

        List<Map<String, Object>> events = new ArrayList<>();
        HoraireBaseService service = new HoraireBaseService();

        try {
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            Interpreter interpreter = daoInterpreter.find(id);
            if (interpreter == null)
                return Collections.emptyList();

            // Base appointments
            for (Appointment a : service.getBaseAppointmentsForInterpreter(interpreter)) {
                Map<String, Object> event = buildAppointmentEvent(a);
                if (event != null)
                    events.add(event);
            }

            // Recurring absences
            for (Absence a : service.getBaseAbsencesForInterpreter(interpreter)) {
                Map<String, Object> event = buildAbsenceEvent(a);
                if (event != null)
                    events.add(event);
            }

        } catch (SQLException | BadStatusException e) {
            e.printStackTrace();
        }

        return events;
    }

    /**
     * Returns the list of TimeSlotBase events for a given beneficiary in JSON format.
     * Events are placed on the current week's days using the TimeSlotBase dayNumber.
     * Redirects to an empty list if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the beneficiary whose schedule is requested
     * @param session the current HTTP session
     * @return a list of FullCalendar event maps, or an empty list if the session is invalid
     */
    @GetMapping(value = "/beneficiaire/{id}/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getEventsBeneficiary(@PathVariable int id, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return Collections.emptyList();

        List<Map<String, Object>> events = new ArrayList<>();
        HoraireBaseService service = new HoraireBaseService();

        try {
            for (Appointment a : service.getBaseAppointmentsForBeneficiary(id)) {
                Map<String, Object> event = buildAppointmentEvent(a);
                if (event != null)
                    events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    /**
     * Returns the sorted list of interpreters suggested for a beneficiary's add-slot modal.
     * The referent is always first with "(Référent)" in the label.
     * Then interpreters whose address locality matches the beneficiary's communication languages.
     * Then the rest alphabetically.
     * Redirects to an empty list if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the beneficiary
     * @param session the current HTTP session
     * @return a list of maps with "numInterpreter" and "label", or an empty list
     */
    @GetMapping(value = "/beneficiaire/{id}/interpreters", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getSuggestedInterpreters(@PathVariable int id, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return Collections.emptyList();

        try {
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            DAOInterpreter daoInterpreter = new DAOInterpreter();

            Beneficiary beneficiary = daoBeneficiary.find(id);
            if (beneficiary == null)
                return Collections.emptyList();

            List<Interpreter> allInterpreters = daoInterpreter.findAll();

            HoraireBaseService service = new HoraireBaseService();
            return service.getSuggestedInterpreters(beneficiary, allInterpreters);

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Creates a new base schedule slot for an interpreter.
     * If isAbsence is true, creates a recurring Absence with a TimeSlotBase.
     * If isAbsence is false, creates an Appointment with a TimeSlotBase.
     * All CRUD operations are delegated to HoraireBaseService.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numInterpreter of the interpreter
     * @param dayNumber the day of the week (1=Monday ... 7=Sunday)
     * @param startTime the start time as "HH:mm"
     * @param endTime the end time as "HH:mm"
     * @param isAbsence true if this is a recurring unavailability, false for an appointment
     * @param numBeneficiary the numBeneficiary (ignored if isAbsence is true)
     * @param numEstablishment the numEstablishment (ignored if isAbsence is true)
     * @param local the local (ignored if isAbsence is true)
     * @param description the description (optional, ignored if isAbsence is true)
     * @param session the current HTTP session
     * @return "ok" on success, "error" on failure, or "unauthorized" if session is invalid
     */
    @PostMapping(value = "/interprete/{id}", produces = "application/json")
    @ResponseBody
    public String createInterpreterSlot( @PathVariable int id, @RequestParam int dayNumber, @RequestParam String startTime, @RequestParam String endTime,
            @RequestParam(defaultValue = "false") boolean isAbsence, @RequestParam(required = false) Integer numBeneficiary, @RequestParam(required = false) Integer numEstablishment,
            @RequestParam(required = false) String local, @RequestParam(required = false) String description, HttpSession session) {

        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            HoraireBaseService service = new HoraireBaseService();

            if (isAbsence) {
                return service.createBaseAbsence(id, dayNumber, start, end) ? "ok" : "error";
            } else {
                if (numBeneficiary == null || numEstablishment == null)
                    return "error";
                return service.createBaseAppointmentForInterpreter( id, numBeneficiary, numEstablishment,
                        dayNumber, start, end, local, description) ? "ok" : "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Creates a new base schedule slot for a beneficiary.
     * Creates an Appointment with a TimeSlotBase linked to the beneficiary, interpreter, establishment, academic skill and professional skill provided.
     * All CRUD operations are delegated to HoraireBaseService.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the beneficiary
     * @param dayNumber the day of the week (1=Monday ... 7=Sunday)
     * @param startTime the start time as "HH:mm"
     * @param endTime the end time as "HH:mm"
     * @param numInterpreter the numInterpreter to assign
     * @param numEstablishment the numEstablishment
     * @param numAcademicSkill the numAcademicSkill required
     * @param numProfessionalSkill the numProfessionalSkill required
     * @param local the local (room)
     * @param description the description (optional)
     * @param session the current HTTP session
     * @return "ok" on success, "error" on failure, or "unauthorized" if session is invalid
     */
    @PostMapping(value = "/beneficiaire/{id}", produces = "application/json")
    @ResponseBody
    public String createBeneficiarySlot( @PathVariable int id, @RequestParam int dayNumber, @RequestParam String startTime,
            @RequestParam String endTime, @RequestParam int numInterpreter, @RequestParam int numEstablishment, @RequestParam int numAcademicSkill,
            @RequestParam int numProfessionalSkill, @RequestParam String local, @RequestParam(required = false) String description, HttpSession session) {

        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            HoraireBaseService service = new HoraireBaseService();

            return service.createBaseAppointmentForBeneficiary(id, numInterpreter, numEstablishment,
                    numAcademicSkill, numProfessionalSkill, dayNumber, start, end, local, description) ? "ok" : "error";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Updates an existing TimeSlotBase (day, start time, end time).
     * The duration is recomputed as endTime - startTime.
     * The linked Appointment or Absence is not modified.
     * All CRUD operations are delegated to HoraireBaseService.
     * Redirects to login if no coordinator is found in session.
     *
     * @param numTimeSlot the numTimeSlot of the TimeSlotBase to update
     * @param dayNumber the new day number (1=Monday ... 7=Sunday)
     * @param startTime the new start time as "HH:mm"
     * @param endTime the new end time as "HH:mm"
     * @param session the current HTTP session
     * @return "ok" on success, "error" on failure, or "unauthorized" if session is invalid
     */
    @PostMapping(value = "/update", produces = "application/json")
    @ResponseBody
    public String updateSlot( @RequestParam int numTimeSlot, @RequestParam int dayNumber, @RequestParam String startTime,
            @RequestParam String endTime, HttpSession session) {

        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            HoraireBaseService service = new HoraireBaseService();
            return service.updateSlot(numTimeSlot, dayNumber, LocalTime.parse(startTime), LocalTime.parse(endTime)) ? "ok" : "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Deletes a TimeSlotBase by its numTimeSlot.
     * The DB trigger trg_delete_timeslot_base automatically cascades the deletion to linked Absence and Appointment rows.
     * All CRUD operations are delegated to HoraireBaseService.
     * Redirects to login if no coordinator is found in session.
     *
     * @param numTimeSlot the numTimeSlot of the TimeSlotBase to delete
     * @param session the current HTTP session
     * @return "ok" on success, "error" on failure, or "unauthorized" if session is invalid
     */
    @PostMapping(value = "/delete", produces = "application/json")
    @ResponseBody
    public String deleteSlot( @RequestParam int numTimeSlot, HttpSession session) {

        if (getCoordinatorFromSession(session) == null)
            return "unauthorized";

        try {
            HoraireBaseService service = new HoraireBaseService();
            return service.deleteSlot(numTimeSlot) ? "ok" : "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}