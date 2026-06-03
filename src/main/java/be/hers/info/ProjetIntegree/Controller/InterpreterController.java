package be.hers.info.ProjetIntegree.Controller;
/**
 * @authors Halet Louis, Wellinger Chloe, Vatafu Jean, Rosman Loïs, Vanderheyden Quentin
 * @reviewer Nicolas Jean-François
 */

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
import java.time.Duration;
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
     * @param start the start date of the schedule
     * @param end the end date of the schedule
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return a formatted map list for FullCalendar
     */
    @GetMapping(value = "/planning/events", produces="application/json")
    @ResponseBody
    public List<Map<String,Object>> getEventsPlaningInterpreter(@RequestParam String start,
                                                                @RequestParam String end, HttpSession session, Model model) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return Collections.emptyList();
        }

        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentWithDateAndInterpreter(interpreter,dateStart,dateEnd);
        List<Absence> absenceList = planningService.getListAbsenceWithDateAndInterpreter(interpreter,dateStart,dateEnd);

        interpreter.setAppointmentsList(appointmentList);
        interpreter.setAbsences(absenceList);
        LocalDate ldStart = LocalDate.parse(dateStart);
        LocalDate ldEnd = LocalDate.parse(dateEnd);

        List<Map<String,Object>> events = new ArrayList<>();
        List<LocalDate> listDateBetweenStartEnd = ldStart.datesUntil(ldEnd.plusDays(1))
                .toList();
        for( Appointment a : appointmentList){
            Map<String, Object> event = new HashMap<>();
            Map<String, Object> extendedProps = new HashMap<>();

            String skills = a.getAcademicSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));
            event.put("title", skills);

            if(a.getTimeSlot() instanceof TimeSlotPunctual){
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                LocalDateTime ldt =  LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                event.put("start",ldt);
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));

                switch (a.getStatus()){
                    case "en attente":
                        event.put("color","#f0ad4e");
                        break;
                    case "accepte":
                        event.put("color","#81c784");
                        break;
                    case "refuse":
                        event.put("color","#f28b82");
                        break;
                }
            }else{
                TimeSlotBase tsp = (TimeSlotBase) a.getTimeSlot();
                int i = tsp.getDayNumber();
                LocalDate ld = null;
                for(LocalDate l : listDateBetweenStartEnd){
                    if(l.getDayOfWeek().getValue() == i){
                        ld = l;
                        break;
                    }
                }
                LocalDateTime ldt = LocalDateTime.of(ld, tsp.getStartTime());
                event.put("start",ldt);
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
                event.put("color","#b39ddb");
            }

            String professionalSkills = a.getProfessionalSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));

            extendedProps.put("type","appointment");
            extendedProps.put("status",a.getStatus());
            extendedProps.put("professionalSkills", professionalSkills);
            extendedProps.put("beneficiary", a.getBeneficiary().getLastName().substring(0,1) + ". " + a.getBeneficiary().getFirstName());
            extendedProps.put("locals", a.getAppointmentLocals());
            extendedProps.put("establishment",a.getEstablishment().getNameBuilding());
            extendedProps.put("description", a.getDescription());
            event.put("extendedProps",extendedProps);
            events.add(event);

        }
        for(Absence a : absenceList){
            Map<String, Object> event = new HashMap<>();
            Map<String, Object> extendedProps = new HashMap<>();
            event.put("title","Indisponibilité");
            if(a.getTimeSlot() instanceof TimeSlotPunctual){
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                LocalDateTime ldt =  LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                event.put("start",ldt);
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));

            }else{
                TimeSlotBase tsp = (TimeSlotBase) a.getTimeSlot();
                int i = tsp.getDayNumber();
                LocalDate ld = null;
                for(LocalDate l : listDateBetweenStartEnd){
                    if(l.getDayOfWeek().getValue() == i){
                        ld = l;
                        break;
                    }
                }
                LocalDateTime ldt = LocalDateTime.of(ld, tsp.getStartTime());
                event.put("start",ldt);
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
            }
            event.put("color","#f0ad4e");
            extendedProps.put("type","absence");
            extendedProps.put("reason", a.getReason());
            event.put("extendedProps",extendedProps);
            events.add(event);
        }
        return events;
    }

    /**
     * Create a list of beneficiaries linked to the interpreter
     * Redirects to login if no beneficiary is found in session.
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
        PlanningService planningService = new PlanningService();
        List<Beneficiary> beneficiaryList = planningService.getListBeneficiaryRefererInterpreter(interpreter.getNumInterpreter());
        session.setAttribute("beneficiaryList", beneficiaryList);
        model.addAttribute("activeTab", "planning");

        return "interprete/planning-beneficiaires";
    }

    /**
     * Search all Appointments within the Start and End time range linked to the beneficiary number passed in the URL.
     * Format the information found in a list on the Map for FullCalendar
     * Redirects to login if no Interpreter is found in session.
     * @param start the start date of the schedule
     * @param end the end date of the schedule
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return a formatted map list for FullCalendar
     */
    @GetMapping(value = "/planning/beneficiaires/events", produces="application/json")
    @ResponseBody
    public List<Map<String,Object>> getEventsPlaningBeneficiary(@RequestParam String start,
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

        List<Map<String,Object>> events = new ArrayList<>();
        List<LocalDate> listDateBetweenStartEnd = ldStart.datesUntil(ldEnd.plusDays(1))
                .toList();
        for( Appointment a : appointmentList){
            Map<String, Object> event = new HashMap<>();
            Map<String, Object> extendedProps = new HashMap<>();


            String skills = a.getAcademicSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));
            event.put("title", skills);

            if(a.getTimeSlot() instanceof TimeSlotPunctual){
                TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
                LocalDateTime ldt =  LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
                event.put("start",ldt);
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));

                switch (a.getStatus()){
                    case "en attente":
                        event.put("color","#f0ad4e");
                        break;
                    case "accepte":
                        event.put("color","#81c784");
                        break;
                    case "refuse":
                        event.put("color","#f28b82");
                        break;
                }
            }else{
                TimeSlotBase tsp = (TimeSlotBase) a.getTimeSlot();
                int i = tsp.getDayNumber();
                LocalDate ld = null;
                for(LocalDate l : listDateBetweenStartEnd){
                    if(l.getDayOfWeek().getValue() == i){
                        ld = l;
                        break;
                    }
                }
                LocalDateTime ldt = LocalDateTime.of(ld, tsp.getStartTime());
                event.put("start",ldt);
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
                event.put("color","#b39ddb");
            }

            String professionalSkills = a.getProfessionalSkillsNeeded().stream()
                    .map(s -> s.getDesignation())
                    .collect(Collectors.joining(", "));

            extendedProps.put("type","appointment");
            extendedProps.put("status",a.getStatus());
            extendedProps.put("professionalSkills", professionalSkills);
            extendedProps.put("beneficiary", a.getBeneficiary().getLastName().substring(0,1) + ". " + a.getBeneficiary().getFirstName());
            extendedProps.put("locals", a.getAppointmentLocals());
            extendedProps.put("establishment",a.getEstablishment().getNameBuilding());
            extendedProps.put("description", a.getDescription());
            event.put("extendedProps",extendedProps);
            events.add(event);

        }
        return events;
    }

    /**
     * Displays the list of punctual absences for the connected interpreter within a specific date range
     * The method extracts the date from the start and end parameters and retrieves
     * matching absences from the database
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
        return "interprete/indisponibilites";
    }


    /**
     * Function called when the form is filled.
     * Also redirect to the indsponibilites page.
     * It create an Absence in the Database.
     * @param dtoAbsence the dto to convert into a pojo
     * @param model the UI model to hold the list of absences and the active tab status
     * @param request    the current HTTP request used to access the session
     * @return the page to redirect to.
     */
    @PostMapping("/indisponibilites")
    public String createIndisponibilite(@ModelAttribute("DTOAbsence") DTOAbsence dtoAbsence, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        AbsenceService absenceService = new  AbsenceService();
        try{
            absenceService.createAbsence(dtoAbsence, interpreter.getNumInterpreter());
        }
        catch(SQLException sql){
            // afficher la page d'erreur
        }
        catch(BadStatusException bse){
            // afficher la page d'erreur
        }
        return "redirect:/interprete/indisponibilites";
    }

    /**
     * Deletes a specific absence record based on its unique ID
     * @param id the unique identifier of the absence to be deleted
     * @param model the UI model to hold the list of absences and the active tab status
     * @param request    the current HTTP request used to access the session
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
     * Updates the details of an existing absence
     * The updated information is received as a model attribute and passed to the service
     * @param updatedAbsence The absence object containing the modified data
     * @param model the UI model to hold the list of absences and the active tab status
     * @param request    the current HTTP request used to access the session
     * @return A redirect to the absences list view after the update is processed
     */
    @PostMapping("/indisponibilites/update")
    public String updateAbsence(@ModelAttribute Absence updatedAbsence,
                                Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            AbsenceService absenceService = new AbsenceService();
            absenceService.updateAbsence(updatedAbsence);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/indisponibilites";
    }

    /** Controller for the pages named "Mon profil"
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
    public String profil(HttpSession session, Model model){
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

    /** Controller for the pages named "Mon profil"
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

    /** Controller for the pages named "Mon profil"
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
     *         with "?passwordError=true" appended if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
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
     * Controller for the pages named "Mon profil" part Professional Skill
     * Before adding the object, search for the Professional Skill in the list of available Professional Skills using the id (NumProfessionalSkillSelected)     * Ajoute dans les listes de professionalSkill de l'interprete en session et dans le profileDTO
     * Also features the action in DB
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/interpreter/profil" after adding, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addProfessionalSkill")
    public String addProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        ProfessionalSkill p = null;
        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean res = profileService.addProfessionalSkill(interpreter.getNumInterpreter(), profileDTO.getNumProfessionalSkillSelected());
            if(res){
                p = profileDTO.findProfessionalSkillById(profileDTO.getNumProfessionalSkillSelected());
                if(p != null){
                    if(!interpreter.getProfessionalSkillsList().contains(p)){
                        interpreter.getProfessionalSkillsList().add(p);
                    }
                    if(!profileDTO.getProfessionalSkillListInterpreter().contains((p))){
                        profileDTO.getProfessionalSkillListInterpreter().add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", interpreter);

        return "redirect:/interprete/profil";
    }

    /**
     * Controller for the pages named "Mon profil" part Professional Skill
     * Before deleting the object, search for the Professional Skill in the list of available Professional Skills using the ID (NumProfessionalSkillSelected)
     * Deletes the Professional Skill from the interpreter's session list and from the profileDTO
     * Also performs the action in the database
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/interpreter/profil" after deleting, or a redirect to "/login" if the session is invalid
     */

    @PostMapping("/profil/deleteProfessionalSkill")
    public String deleteProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        ProfessionalSkill p = null;
        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean res = profileService.deleteProfessionalSkill(interpreter.getNumInterpreter(), profileDTO.getNumProfessionalSkillSelected());
            if(res){
                p = profileDTO.findProfessionalSkillById(profileDTO.getNumProfessionalSkillSelected());
                if(p != null){
                    interpreter.getProfessionalSkillsList().remove(p);
                    profileDTO.getProfessionalSkillListInterpreter().remove(p);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", interpreter);

        return "redirect:/interprete/profil";
    }

    /**
     * Controller for the pages named "Mon profil" part Academic Skill
     * Before adding the object, search for the Academic Skill in the list of available Academic Skills using the ID (NumAcademicSkillSelected)
     * Adds it to the interpreter's Academic Skill lists in the session and to the profileDTO
     * Also performs the action in the database
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/interpreter/profil" after adding, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addAcademicSkill")
    public String addAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        AcademicSkill a = null;
        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean res = profileService.addAcademicSkill(interpreter.getNumInterpreter(), profileDTO.getNumAcademicSkillSelected());
            if(res){
                a = profileDTO.findAcademicSkillById(profileDTO.getNumAcademicSkillSelected());
                if(a != null){
                    if(!interpreter.getAcademicSkillsList().contains(a)){
                        interpreter.getAcademicSkillsList().add(a);
                    }
                    if(!profileDTO.getAcademicSkillListInterpreter().contains((a))){
                        profileDTO.getAcademicSkillListInterpreter().add(a);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", interpreter);

        return "redirect:/interprete/profil";
    }

    /**
     * Controller for the pages named "Mon profil" part Academic Skill
     * Before deleting the object, search for the Academic Skill in the list of available Academic Skills using the ID (NumAcademicSkillSelected)
     * Deletes it from the interpreter's Academic Skill lists in the session and from the profileDTO
     * Also performs the action in the database
     * Reads the interpreter directly from the session.
     * Redirects to login if no interpreter is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/interpreter/profil" after deleting, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteAcademicSkill")
    public String deleteAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }
        AcademicSkill a = null;
        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean res = profileService.deleteAcademicSkill(interpreter.getNumInterpreter(), profileDTO.getNumAcademicSkillSelected());
            if(res){
                a = profileDTO.findAcademicSkillById(profileDTO.getNumAcademicSkillSelected());
                if(a != null){
                    interpreter.getAcademicSkillsList().remove(a);
                    profileDTO.getAcademicSkillListInterpreter().remove(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", interpreter);

        return "redirect:/interprete/profil";
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