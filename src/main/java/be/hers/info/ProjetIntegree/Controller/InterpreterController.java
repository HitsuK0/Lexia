package be.hers.info.ProjetIntegree.Controller;
/**
 * @authors Halet Louis, Wellinger Chloe, Vatafu Jean, Rosman Loïs, Vanderheyden Quentin
 * @reviewer Nicolas Jean-François
 */

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.DTO.*;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.AbsenceService;
import be.hers.info.ProjetIntegree.Services.AppointmentFormService;
import be.hers.info.ProjetIntegree.Services.InterpreterProfileService;
import be.hers.info.ProjetIntegree.Services.PlanningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/interprete")
public class InterpreterController {

    /**
     * Retrieves the connected interpreter from the session.
     * Returns null if no user is connected or if the connected user is not an interpreter.
     * This helper avoids relying on @ModelAttribute which may inject an empty POJO
     * instead of null when no session attribute exists.
     *
     * @param session the current HTTP session
     * @return the connected interpreter, or null if not found
     */
    private Interpreter getInterpreterFromSession(HttpSession session) {
        if (session == null) return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Interpreter) {
            return (Interpreter) user;
        }
        return null;
    }

    /**
     * Redirect to the "interprete/planning" page
     * Redirects to login if no beneficiary is found in session.
     *
     * @return Redirect to the "interprete/planning" page
     */
    @GetMapping("/planning")
    public String planning(HttpSession session, Model model) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        model.addAttribute("activeTab", "planning");

        model.addAttribute("DTOAbsence", new DTOAbsence());
        return "interprete/planning";
    }

    /**
     * Searches for all Absences and Appointments within the Start and End time range.
     * Format the information found in a list on the Map for FullCalendar
     * Redirects to login if no Interpreter is found in session.
     *
     * @param start   the start date of the schedule
     * @param end     the end date of the schedule
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return a formatted map list for FullCalendar
     */
    @GetMapping(value = "/planning/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getEventsPlaningInterpreter(@RequestParam String start,
                                                                 @RequestParam String end, HttpSession session, Model model) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return Collections.emptyList();
        }

        String dateStart = start.substring(0, 10);
        String dateEnd = end.substring(0, 10);
        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentWithDateAndInterpreter(interpreter, dateStart, dateEnd);
        List<Absence> absenceList = planningService.getListAbsenceWithDateAndInterpreter(interpreter, dateStart, dateEnd);

        interpreter.setAppointmentsList(appointmentList);
        interpreter.setAbsences(absenceList);
        LocalDate ldStart = LocalDate.parse(dateStart);
        LocalDate ldEnd = LocalDate.parse(dateEnd);

        List<Map<String, Object>> events = new ArrayList<>();
        List<LocalDate> listDateBetweenStartEnd = ldStart.datesUntil(ldEnd.plusDays(1))
                .toList();
        for (Appointment a : appointmentList) {
            Map<String, Object> event = new HashMap<>();
            Map<String, Object> extendedProps = new HashMap<>();

            String skills = a.getAcademicSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));
            event.put("title", skills);

            if (a.getTimeSlot() instanceof TimeSlotPunctual) {
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                LocalDateTime ldt = LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                event.put("start", ldt);
                event.put("end", ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));

                switch (a.getStatus()) {
                    case "en attente":
                        event.put("color", "#f0ad4e");
                        break;
                    case "accepte":
                        event.put("color", "#81c784");
                        break;
                    case "refuse":
                        event.put("color", "#f28b82");
                        break;
                    case "annule":
                        event.put("color", "#f28b82");
                        break;
                }
            } else {
                TimeSlotBase tsp = (TimeSlotBase) a.getTimeSlot();
                int i = tsp.getDayNumber();
                LocalDate ld = null;
                for (LocalDate l : listDateBetweenStartEnd) {
                    if (l.getDayOfWeek().getValue() == i) {
                        ld = l;
                        break;
                    }
                }
                LocalDateTime ldt = LocalDateTime.of(ld, tsp.getStartTime());
                event.put("start", ldt);
                event.put("end", ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
                event.put("color", "#b39ddb");
            }

            String professionalSkills = a.getProfessionalSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));

            extendedProps.put("type", "appointment");
            extendedProps.put("status", a.getStatus());
            extendedProps.put("professionalSkills", professionalSkills);
            extendedProps.put("beneficiary", a.getBeneficiary().getLastName().substring(0, 1) + ". " + a.getBeneficiary().getFirstName());
            extendedProps.put("locals", a.getAppointmentLocals());
            extendedProps.put("establishment", a.getEstablishment().getNameBuilding());
            extendedProps.put("description", a.getDescription());
            event.put("extendedProps", extendedProps);
            events.add(event);

        }
        for (Absence a : absenceList) {
            Map<String, Object> event = new HashMap<>();
            Map<String, Object> extendedProps = new HashMap<>();
            event.put("title", "Indisponibilité");

            if (a.getTimeSlot() instanceof TimeSlotPunctual) {
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                LocalDateTime ldt = LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                event.put("start", ldt);

                if (!tsp.getStartDate().equals(tsp.getEndDate())) {
                    LocalDateTime ldtEnd = LocalDateTime.of(tsp.getEndDate(), tsp.getStartTime())
                            .plusSeconds(tsp.getDuration().toSecondOfDay());
                    event.put("end", ldtEnd);
                } else {
                    event.put("end", ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
                }
            } else {
                TimeSlotBase tsp = (TimeSlotBase) a.getTimeSlot();
                int i = tsp.getDayNumber();
                LocalDate ld = null;
                for (LocalDate l : listDateBetweenStartEnd) {
                    if (l.getDayOfWeek().getValue() == i) {
                        ld = l;
                        break;
                    }
                }
                if (ld == null) continue;
                LocalDateTime ldt = LocalDateTime.of(ld, tsp.getStartTime());
                event.put("start", ldt);
                event.put("end", ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
            }

            boolean isFullDay = false;
            if (a.getTimeSlot() instanceof TimeSlotPunctual) {
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                isFullDay = tsp.getStartTime() != null
                        && tsp.getStartTime().equals(java.time.LocalTime.MIDNIGHT)
                        && tsp.getDuration() != null
                        && tsp.getDuration().getHour() == 23;
            }
            event.put("color", "#f0ad4e");
            extendedProps.put("type", "absence");
            extendedProps.put("reason", a.getReason());
            extendedProps.put("fullDay", isFullDay);
            event.put("extendedProps", extendedProps);
            events.add(event);
        }
        return events;
    }

    /**
     * Create a list of beneficiaries linked to the interpreter
     * Redirects to login if no beneficiary is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return Redirect to the "interprete/planning/beneficiaires" page
     */
    @GetMapping("/planning/beneficiaires")
    public String planningBeneficiaires(HttpSession session, Model model) {

        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        AppointmentFormService serviceAppointment = new AppointmentFormService();
        PlanningService planningService = new PlanningService();
        List<Beneficiary> beneficiaryList = planningService.getListBeneficiaryRefererInterpreter(interpreter.getNumInterpreter());
        session.setAttribute("beneficiaryList", beneficiaryList);
        model.addAttribute("activeTab", "planning");
        model.addAttribute("establishmentList", serviceAppointment.findAllEstablishments());
        model.addAttribute("academicSkillList", serviceAppointment.findAllAcademicSkills());
        model.addAttribute("professionalSkillList", serviceAppointment.findAllProfessionalSkills());

        return "interprete/planning-beneficiaires";
    }

    /**
     * Search all Appointments within the Start and End time range linked to the beneficiary number passed in the URL.
     * Format the information found in a list on the Map for FullCalendar
     * Redirects to login if no Interpreter is found in session.
     *
     * @param start   the start date of the schedule
     * @param end     the end date of the schedule
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return a formatted map list for FullCalendar
     */
    @GetMapping(value = "/planning/beneficiaires/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getEventsPlaningBeneficiary(@RequestParam String start,
                                                                 @RequestParam String end, @RequestParam("num") int num, HttpSession session, Model model) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return Collections.emptyList();
        }

        String dateStart = start.substring(0, 10);
        String dateEnd = end.substring(0, 10);

        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentsToBeneficiaryAndDate(num, dateStart, dateEnd);

        LocalDate ldStart = LocalDate.parse(dateStart);
        LocalDate ldEnd = LocalDate.parse(dateEnd);

        List<Map<String, Object>> events = new ArrayList<>();
        List<LocalDate> listDateBetweenStartEnd = ldStart.datesUntil(ldEnd.plusDays(1))
                .toList();
        for (Appointment a : appointmentList) {
            Map<String, Object> event = new HashMap<>();
            Map<String, Object> extendedProps = new HashMap<>();


            String skills = a.getAcademicSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));
            event.put("title", skills);

            if (a.getTimeSlot() instanceof TimeSlotPunctual) {
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                LocalDateTime ldt = LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                event.put("start", ldt);
                event.put("end", ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));

                switch (a.getStatus()) {
                    case "en attente":
                        event.put("color", "#f0ad4e");
                        break;
                    case "accepte":
                        event.put("color", "#81c784");
                        break;
                    case "refuse":
                        event.put("color", "#f28b82");
                        break;
                    case "annule":
                        event.put("color", "#f28b82");
                        break;
                }
            } else {
                TimeSlotBase tsp = (TimeSlotBase) a.getTimeSlot();
                int i = tsp.getDayNumber();
                LocalDate ld = null;
                for (LocalDate l : listDateBetweenStartEnd) {
                    if (l.getDayOfWeek().getValue() == i) {
                        ld = l;
                        break;
                    }
                }
                if (ld == null) continue;
                LocalDateTime ldt = LocalDateTime.of(ld, tsp.getStartTime());
                event.put("start", ldt);
                event.put("end", ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
                event.put("color", "#b39ddb");
            }

            String professionalSkills = a.getProfessionalSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));

            extendedProps.put("type", "appointment");
            extendedProps.put("status", a.getStatus());
            extendedProps.put("professionalSkills", professionalSkills);
            extendedProps.put("beneficiary", a.getBeneficiary().getLastName().substring(0, 1) + ". " + a.getBeneficiary().getFirstName());
            extendedProps.put("locals", a.getAppointmentLocals());
            extendedProps.put("establishment", a.getEstablishment().getNameBuilding());
            extendedProps.put("description", a.getDescription());
            event.put("extendedProps", extendedProps);
            events.add(event);

        }
        return events;
    }

    /**
     * Displays the list of punctual absences for the connected interpreter within a specific date range
     * The method extracts the date from the start and end parameters and retrieves
     * matching absences from the database
     *
     * @param model the UI model to hold the list of absences and the active tab status
     * @return The view name "interprete/indisponibilites", or a redirect to login if session is invalid
     */
    @GetMapping("/indisponibilites")
    public String indisponibilites(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            AbsenceService absenceService = new AbsenceService();

            List<Absence> punctualAbsencesList = absenceService.getPunctualAbsencesInterpreter(interpreter);
            model.addAttribute("punctualAbsencesList", punctualAbsencesList);
        } catch (BadStatusException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("activeTab", "indisponibilites");
        model.addAttribute("DTOAbsence", new DTOAbsence());
        model.addAttribute("DTOAbsenceEdit", new DTOAbsence());
        return "interprete/indisponibilites";
    }


    /**
     * Function called when the form is filled.
     * Also redirect to the indsponibilites page.
     * It create an Absence in the Database.
     *
     * @param dtoAbsence the dto to convert into a pojo
     * @param model      the UI model to hold the list of absences and the active tab status
     * @param request    the current HTTP request used to access the session
     * @return redirect the curent page.
     */
    @PostMapping("/indisponibilites")
    public String createIndisponibilite(@ModelAttribute("DTOAbsence") DTOAbsence dtoAbsence, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String pageReferer = request.getHeader("Referer");
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        if (dtoAbsence.getStartDate() != null && dtoAbsence.getEndDate() != null
                && (dtoAbsence.isFullDay() || (dtoAbsence.getStartTime() != null && dtoAbsence.getEndTime() != null))) {
            AbsenceService absenceService = new AbsenceService();
            try {
                absenceService.createAbsence(dtoAbsence, interpreter.getNumInterpreter(), "en attente");
            } catch (SQLException sql) {
                // afficher la page d'erreur
            } catch (BadStatusException bse) {
                // afficher la page d'erreur
            }
        }

        return "redirect:" + pageReferer;
    }

    /**
     * Deletes a specific absence record based on its unique ID
     *
     * @param id      the unique identifier of the absence to be deleted
     * @param model   the UI model to hold the list of absences and the active tab status
     * @param request the current HTTP request used to access the session
     * @return A redirect to the absences list view after deletion
     */
    @PostMapping("/indisponibilites/delete")
    public String deleteAbsence(@RequestParam int id,
                                Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        try {
            AbsenceService absenceService = new AbsenceService();
            absenceService.deleteAbsence(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/indisponibilites";
    }

    /**
     * Updates an existing absence from the edit modal form.
     * Receives the absence id as a request parameter and the new values as a DTOAbsence.
     * Delegates the update logic to AbsenceService.updateAbsenceFromDTO().
     * Only absences with status "en attente" can be modified (enforced by the HTML).
     * Redirects to login if no interpreter is found in session.
     *
     * @param numAbsence the id of the absence to update
     * @param dtoAbsence the DTO containing the new values submitted from the edit modal
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/interprete/indisponibilites" after the update,
     * or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/indisponibilites/update")
    public String updateAbsence(@RequestParam int numAbsence, @ModelAttribute("DTOAbsenceEdit") DTOAbsence dtoAbsence, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null)
            return "redirect:/login";

        try {
            AbsenceService absenceService = new AbsenceService();
            absenceService.updateAbsenceFromDTO(numAbsence, dtoAbsence);
        } catch (SQLException | BadStatusException e) {
            e.printStackTrace();
        }
        return "redirect:/interprete/indisponibilites";
    }

    /**
     * Controller for the pages named "Mon profil"
     * Displays the profile page for the connected interpreter.
     * Reads the interpreter directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Builds a {@link DTOInterpreterProfile} from the connected interpreter and adds it to the model so the Thymeleaf form can bind its fields.
     * Also adds an empty {@link DTOPasswordChange} for the password change modal.
     * Redirects to login if no interpreter is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "interprete/profil", or a redirect to "/login"
     */
    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        InterpreterProfileService profileService = new InterpreterProfileService();

        DTOInterpreterProfile profileDTO = profileService.buildProfileDTO(interpreter);

        model.addAttribute("profileDTO", profileDTO);
        model.addAttribute("passwordDTO", new DTOPasswordChange());
        model.addAttribute("activeTab", "profil");

        return "interprete/profil";
    }

    /**
     * Controller for the pages named "Mon profil"
     * Handles the submission of the profile edit form.
     * Saves the modified personal data (lastName, firstName, phoneNumber, emailAddress, address, weeklyWorkHours) of the connected interpreter.
     * The login and password are NOT modified here.
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/interpreter/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            profileService.saveProfile(interpreter, profileDTO);
            session.setAttribute("currentUser", interpreter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/profil";
    }

    /**
     * Controller for the pages named "Mon profil"
     * Handles the submission of the password change modal.
     * Verifies that newPassword and confirmPassword match, then updates the password in the database.
     * The DB trigger will hash the new password automatically on UPDATE.
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     * Redirects back to the profile page with an error parameter if the passwords do not match.
     *
     * @param passwordDTO the password change form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/interprete/profil" after the operation,
     * with "?passwordError=true" appended if passwords do not match,
     * or a redirect to "/login" if the session is invalid
     */

    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean success = profileService.changePassword(interpreter, passwordDTO);
            if (!success) {
                return "redirect:/interprete/profil?passwordError=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/profil";
    }

    /**
     * Controller for the pages named "Mon profil" part Professional Skill.
     * Adds a professional skill to the connected interpreter.
     * Checks first if the interpreter already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * Loads the skill from the database after insertion to update the interpreter's session list.
     * The profileDTO reconstructed by Spring via @ModelAttribute does not carry its skill lists
     * — only numProfessionalSkillSelected is used from it.
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numProfessionalSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/interprete/profil" after adding,
     * or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addProfessionalSkill")
    public String addProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();

            boolean alreadyOwned = interpreter.getProfessionalSkillsList() != null &&
                    interpreter.getProfessionalSkillsList().stream()
                            .anyMatch(s -> s.getNumProfessionalSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addProfessionalSkill(interpreter.getNumInterpreter(), numSkill);
                if (res) {
                    DAOProfessionalSkill dao = new DAOProfessionalSkill();
                    ProfessionalSkill p = dao.find(numSkill);
                    if (p != null) {
                        if (interpreter.getProfessionalSkillsList() == null) {
                            interpreter.setProfessionalSkillsList(new ArrayList<>());
                        }
                        interpreter.getProfessionalSkillsList().add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", interpreter);
        return "redirect:/interprete/profil?section=metiers";
    }

    /**
     * Controller for the pages named "Mon profil" part Professional Skill.
     * Deletes a professional skill from the connected interpreter.
     * Removes the skill from the interpreter's session list by matching its ID directly,
     * since the profileDTO reconstructed by Spring via @ModelAttribute does not carry its
     * skill lists — only numProfessionalSkillSelected is used from it.
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numProfessionalSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/interprete/profil" after deleting,
     * or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteProfessionalSkill")
    public String deleteProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                          HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();
            boolean res = profileService.deleteProfessionalSkill(interpreter.getNumInterpreter(), numSkill);
            if (res && interpreter.getProfessionalSkillsList() != null) {
                interpreter.getProfessionalSkillsList()
                        .removeIf(s -> s.getNumProfessionalSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", interpreter);
        return "redirect:/interprete/profil?section=metiers";
    }

    /**
     * Controller for the pages named "Mon profil" part Academic Skill.
     * Adds an academic skill to the connected interpreter.
     * Checks first if the interpreter already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * Loads the skill from the database after insertion to update the interpreter's session list.
     * The profileDTO reconstructed by Spring via @ModelAttribute does not carry its skill lists
     * — only numAcademicSkillSelected is used from it.
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numAcademicSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/interprete/profil" after adding,
     * or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addAcademicSkill")
    public String addAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();

            boolean alreadyOwned = interpreter.getAcademicSkillsList() != null &&
                    interpreter.getAcademicSkillsList().stream()
                            .anyMatch(s -> s.getNumAcademicSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addAcademicSkill(interpreter.getNumInterpreter(), numSkill);
                if (res) {
                    DAOAcademicSkill dao = new DAOAcademicSkill();
                    AcademicSkill a = dao.find(numSkill);
                    if (a != null) {
                        if (interpreter.getAcademicSkillsList() == null) {
                            interpreter.setAcademicSkillsList(new ArrayList<>());
                        }
                        interpreter.getAcademicSkillsList().add(a);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", interpreter);
        return "redirect:/interprete/profil?section=academics";
    }

    /**
     * Controller for the pages named "Mon profil" part Academic Skill.
     * Deletes an academic skill from the connected interpreter.
     * Removes the skill from the interpreter's session list by matching its ID directly,
     * since the profileDTO reconstructed by Spring via @ModelAttribute does not carry its
     * skill lists — only numAcademicSkillSelected is used from it.
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numAcademicSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/interprete/profil" after deleting,
     * or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteAcademicSkill")
    public String deleteAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                      HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();
            boolean res = profileService.deleteAcademicSkill(interpreter.getNumInterpreter(), numSkill);
            if (res && interpreter.getAcademicSkillsList() != null) {
                interpreter.getAcademicSkillsList()
                        .removeIf(s -> s.getNumAcademicSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", interpreter);
        return "redirect:/interprete/profil?section=academics";
    }

    /**
     * Returns the list of all establishments in JSON format.
     * Used by the RDV modal in the beneficiary planning page to populate the establishment select.
     * Redirects to an empty list if no interpreter is found in session.
     *
     * @param session the current HTTP session
     * @return a list of DTOEstablishmentFormAppointment, or an empty list if the session is invalid
     */
    @GetMapping(value = "/planning/beneficiaires/etablissements", produces = "application/json")
    @ResponseBody
    public List<DTOEstablishmentFormAppointment> getEstablishments(HttpSession session) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null)
            return Collections.emptyList();

        AppointmentFormService service = new AppointmentFormService();
        List<DTOEstablishmentFormAppointment> list = service.findAllEstablishments();
        return list != null ? list : Collections.emptyList();
    }

    /**
     * Creates a new appointment from the RDV modal in the beneficiary planning page.
     * Receives the appointment data as a JSON body sent by the JS fetch call.
     * Returns "ok" if the appointment was successfully created, "error" otherwise.
     *
     * @param dtoAppointment the appointment data sent as JSON from the frontend
     * @param session        the current HTTP session
     * @return "ok" on success, "error" on failure
     */
    @PostMapping(value = "/planning/beneficiaires/rdv", consumes = "application/json")
    @ResponseBody
    public String createRDV(@RequestBody DTOAppointmentForm dtoAppointment, HttpSession session) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            AppointmentFormService service = new AppointmentFormService();
            boolean success = service.createAppointment(dtoAppointment);
            return success ? "ok" : "error";
        } catch (BadStatusException | SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return "error";
        }
    }
}