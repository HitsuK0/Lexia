package be.hers.info.ProjetIntegree.Controller;

/**
 * @authors Willinger Chloé, Leroy Rodriguez Aïnhoa, Vanderheyden Quentin, Vatafu Jean, Rosman Loïs
 * @reviewer Halet Louis
 */

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.DTO.*;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.*;

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
@RequestMapping("/coordinatrice")
public class CoordinatorController {

    /**
     * Retrieves the connected coordinator from the session.
     * Returns null if no user is connected or if the connected user is not a Coordinator.
     * @param session the current HTTP session
     * @return the connected Coordinator, or null if not found
     */
    private Coordinator getCoordinatorFromSession(HttpSession session) {
        if (session == null) return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Coordinator) {
            return (Coordinator) user;
        }
        return null;
    }

    /** Controller for the pages named "Mon profil"
     * Displays the profile page for the connected Coordinator.
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return the view "/interprete/profil", or a redirect to "/login"
     */
    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        InterpreterProfileService profileService = new InterpreterProfileService();
        DTOInterpreterProfile profileDTO = profileService.buildProfileDTO(coordinator);

        model.addAttribute("profileDTO", profileDTO);
        model.addAttribute("passwordDTO", new DTOPasswordChange());
        model.addAttribute("activeTab", "profil");

        return "interprete/profil";
    }

    /** Controller for the page "Mon profil" of the connected coordinator.
     * Handles the submission of the profile edit form.
     * Saves the modified personal data.
     * The login and password are NOT modified here.
     * @param profileDTO the profile form data submitted by the user
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            profileService.saveProfile(coordinator, profileDTO);
            session.setAttribute("currentUser", coordinator);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/profil";
    }

    /** Controller for the page "Mon profil" of the connected coordinator.
     * Handles the submission of the password change modal.
     * @param passwordDTO the password change form data submitted by the user
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil" after the operation,
     *         with "?passwordError=true" appended if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean success = profileService.changePassword(coordinator, passwordDTO);
            if (!success) {
                return "redirect:/interprete/profil?passwordError=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/profil";
    }

    /** Controller for the page "Mon profil" of the connected coordinator.
     * Adds a professional skill to the connected coordinator.
     * Checks first if the coordinator already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * @param profileDTO the profile form data submitted by the user
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=metiers" after adding,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addProfessionalSkill")
    public String addProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                       HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();

            boolean alreadyOwned = coordinator.getProfessionalSkillsList() != null &&
                    coordinator.getProfessionalSkillsList().stream()
                            .anyMatch(s -> s.getNumProfessionalSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addProfessionalSkill(coordinator.getNumInterpreter(), numSkill);
                if (res) {
                    DAOProfessionalSkill dao = new DAOProfessionalSkill();
                    ProfessionalSkill p = dao.find(numSkill);
                    if (p != null) {
                        if (coordinator.getProfessionalSkillsList() == null) {
                            coordinator.setProfessionalSkillsList(new ArrayList<>());
                        }
                        coordinator.getProfessionalSkillsList().add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", coordinator);

        return "redirect:/interprete/profil?section=metiers";
    }

    /** Controller for the page "Mon profil" of the connected coordinator, section Professional Skill.
     * Deletes a professional skill from the connected coordinator.
     * Removes the skill from the coordinator's session list by matching its ID directly.
     * @param profileDTO the profile form data submitted by the user.
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=metiers" after deleting,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteProfessionalSkill")
    public String deleteProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                          HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();
            boolean res = profileService.deleteProfessionalSkill(coordinator.getNumInterpreter(), numSkill);
            if (res && coordinator.getProfessionalSkillsList() != null) {
                coordinator.getProfessionalSkillsList()
                        .removeIf(s -> s.getNumProfessionalSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", coordinator);
        return "redirect:/interprete/profil?section=metiers";
    }

    /**
     * Controller for the page "Mon profil" of the connected coordinator, section Academic Skill.
     * Adds an academic skill to the connected coordinator.
     * Checks first if the coordinator already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * @param profileDTO the profile form data submitted by the user.
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=academics" after adding,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addAcademicSkill")
    public String addAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                   HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();

            boolean alreadyOwned = coordinator.getAcademicSkillsList() != null &&
                    coordinator.getAcademicSkillsList().stream()
                            .anyMatch(s -> s.getNumAcademicSkill() == numSkill);

            if (!alreadyOwned) {
               boolean res = profileService.addAcademicSkill(coordinator.getNumInterpreter(), numSkill);
               if (res) {
                    DAOAcademicSkill dao = new DAOAcademicSkill();
                    AcademicSkill a = dao.find(numSkill);
                    if (a != null) {
                        if (coordinator.getAcademicSkillsList() == null) {
                            coordinator.setAcademicSkillsList(new ArrayList<>());
                        }
                        coordinator.getAcademicSkillsList().add(a);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", coordinator);
        return "redirect:/interprete/profil?section=academics";
    }
    /**
     * Controller for the page "Mon profil" of the connected coordinator, section Academic Skill.
     * Deletes an academic skill from the connected coordinator.
     * @param profileDTO the profile form data submitted by the user.
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=academics" after deleting,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteAcademicSkill")
    public String deleteAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                      HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();
            boolean res = profileService.deleteAcademicSkill(coordinator.getNumInterpreter(), numSkill);
            if (res && coordinator.getAcademicSkillsList() != null) {
                coordinator.getAcademicSkillsList()
                        .removeIf(s -> s.getNumAcademicSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", coordinator);
        return "redirect:/interprete/profil?section=academics";
    }



     //Temporaire
    @GetMapping("/validations")
    public String validations(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/validations";
    }


    @GetMapping("/planning-gestion")
    public String planning(HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", coordinator.getFirstName() + " " + coordinator.getLastName());
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("breadcrumb", "Planning");
        model.addAttribute("isAdmin", coordinator.isAdmin());
        model.addAttribute("DTOAbsence", new DTOAbsence());

        return "coordinatrice/planning-gestion";
    }

    @GetMapping(value = "/planning-gestion/events", produces="application/json")
    @ResponseBody
    public List<Map<String,Object>> getEventsPlaningCoordinator(@RequestParam String start,
                                                                @RequestParam String end, HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return Collections.emptyList();
        }

        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentWithDateAndInterpreter(coordinator.getNumInterpreter(),dateStart,dateEnd);
        List<Absence> absenceList = planningService.getListAbsenceWithDateAndInterpreter(coordinator.getNumInterpreter(),dateStart,dateEnd);

        coordinator.setAppointmentsList(appointmentList);
        coordinator.setAbsences(absenceList);
        LocalDate ldStart = LocalDate.parse(dateStart);
        LocalDate ldEnd = LocalDate.parse(dateEnd);

        List<Map<String,Object>> events = new ArrayList<>();
        List<LocalDate> listDateBetweenStartEnd = ldStart.datesUntil(ldEnd.plusDays(1))
                .toList();
        for( Appointment a : appointmentList){
            events.add(buildEventAppointment(a,listDateBetweenStartEnd));
        }
        for(Absence a : absenceList){
            events.add(buildEventAbsence(a,listDateBetweenStartEnd));
        }
        return events;
    }
    public Map<String, Object> buildEventAppointment(Appointment a,List<LocalDate> listDateBetweenStartEnd){
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
        return event;
    }
    public Map<String, Object> buildEventAbsence(Absence a,List<LocalDate> listDateBetweenStartEnd){
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> extendedProps = new HashMap<>();
        event.put("title","Indisponibilité");

        if(a.getTimeSlot() instanceof TimeSlotPunctual){
            TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
            LocalDateTime ldt = LocalDateTime.of(tsp.getStartDate(), tsp.getStartTime());
            event.put("start",ldt);

            if (!tsp.getStartDate().equals(tsp.getEndDate())) {
                LocalDateTime ldtEnd = LocalDateTime.of(tsp.getEndDate(), tsp.getStartTime())
                        .plusSeconds(tsp.getDuration().toSecondOfDay());
                event.put("end", ldtEnd);
            } else {
                event.put("end",ldt.plusSeconds(tsp.getDuration().toSecondOfDay()));
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
        }

        boolean isFullDay = false;
        if (a.getTimeSlot() instanceof TimeSlotPunctual) {
            TimeSlotPunctual tsp = (TimeSlotPunctual) a.getTimeSlot();
            isFullDay = tsp.getStartTime() != null
                    && tsp.getStartTime().equals(java.time.LocalTime.MIDNIGHT)
                    && tsp.getDuration() != null
                    && tsp.getDuration().getHour() == 23;
        }
        event.put("color","#f0ad4e");
        extendedProps.put("type","absence");
        extendedProps.put("reason", a.getReason());
        extendedProps.put("fullDay", isFullDay);
        event.put("extendedProps",extendedProps);
        return event;
    }
    @GetMapping("/planning-gestion/interpreter")
    public String planningInterpreter(HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", coordinator.getFirstName() + " " + coordinator.getLastName());
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("breadcrumb", "Planning");
        model.addAttribute("isAdmin", coordinator.isAdmin());
        HoraireBaseService service = new HoraireBaseService();
        List<Interpreter> interpreterList = new ArrayList<>();
        try{
            interpreterList = service.findAllInterpreters();
            interpreterList.removeIf(i -> i.getNumInterpreter() == coordinator.getNumInterpreter());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.addAttribute("listInterpreter", interpreterList);
        model.addAttribute("DTOAbsence", new DTOAbsence());

        return "coordinatrice/planning-gestion/interpreter";
    }

    @GetMapping(value = "/planning-gestion/interpreter/events", produces="application/json")
    @ResponseBody
    public List<Map<String,Object>> getEventsPlaningInterpreter(@RequestParam String start,
                                                                @RequestParam String end, @RequestParam("num") int num, HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return Collections.emptyList();
        }
        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentWithDateAndInterpreter(num,dateStart,dateEnd);
        List<Absence> absenceList = planningService.getListAbsenceWithDateAndInterpreter(num,dateStart,dateEnd);

        coordinator.setAppointmentsList(appointmentList);
        coordinator.setAbsences(absenceList);
        LocalDate ldStart = LocalDate.parse(dateStart);
        LocalDate ldEnd = LocalDate.parse(dateEnd);

        List<Map<String,Object>> events = new ArrayList<>();
        List<LocalDate> listDateBetweenStartEnd = ldStart.datesUntil(ldEnd.plusDays(1))
                .toList();

        for( Appointment a : appointmentList){
            events.add(buildEventAppointment(a,listDateBetweenStartEnd));
        }
        for(Absence a : absenceList){
            events.add(buildEventAbsence(a,listDateBetweenStartEnd));
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
    @GetMapping("/planning-gestion/beneficiaires")
    public String planningBeneficiaires(HttpSession session, Model model) {

        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }
        HoraireBaseService service = new HoraireBaseService();
        List<Beneficiary> beneficiaryList = new ArrayList<>();
        try{
            beneficiaryList = service.findAllBeneficiaries();
        }catch(SQLException e){
            e.printStackTrace();
        }

        session.setAttribute("beneficiaryList", beneficiaryList);
        model.addAttribute("activeTab", "planning");

        return "coordinatrice/planning-gestion/beneficiaires";
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
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
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
            events.add(buildEventAppointment(a,listDateBetweenStartEnd));
        }

        return events;
    }

    /**
     * This function load the page "gestion".
     * It adds all the data needed for the page to display (Skills, Referents and Establishments).
     * If no user of Coordinator object was found in the session or if the Coordinator in the session
     * is not an admin, it redirects the user to the '/login' page
     *
     * @param model used by Spring to add all the data in the page
     * @param session the current HTTP session
     * @return the page displayed for the Coordinator admin user
     */
    @GetMapping("/gestion")
    public String gestion(Model model, HttpSession session) {

        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }
        String userName = coordinator.getLastName().toUpperCase() + " " + coordinator.getFirstName();

        model.addAttribute("userName", userName);
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", coordinator.isAdmin());
        try {
            model.addAttribute("referentList", new ReferrerService().getAllReferrer());
            model.addAttribute("etablissementList", new EstablishementService().getAllFullEstablishments());
            model.addAttribute("professionalSkillList", new SkillService().getAllProfessionalSkills());
            model.addAttribute("academicSkillList", new SkillService().getAllAcademicSkills());
        } catch(SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("DTOReferrer", new DTOReferrer());
        model.addAttribute("DTOEstablishment", new DTOEstablishment());

        return "coordinatrice/gestion";
    }

    /** Creates a new Referrer in the database using the data submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param dtoReferrer the DTOReferrer containing the data of the Referrer to create
     * @param session the current HTTP session
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/addReferrer")
    public String attributeReferrer(
                            @ModelAttribute("DTOReferrer") DTOReferrer dtoReferrer, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new ReferrerService().createReferrer(dtoReferrer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/coordinatrice/gestion?tab=referents";
    }

    /** Updates an existing Referrer in the database with the data submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param dtoReferrer the DTOReferrer containing the updated data of the Referrer
     * @param session the current HTTP session
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/updateReferrer")
    public String referrerUpdate(DTOReferrer dtoReferrer, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new ReferrerService().updateReferrer(dtoReferrer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/gestion?tab=referents";
    }

    /** Deletes a Referrer from the database using the id contained in the DTOReferrer submitted
     * from the form. If no user of Coordinator type is found in the session or if the Coordinator
     * is not an admin, the user is redirected to the '/login' page
     *
     * @param dtoReferrer the DTOReferrer containing the id of the Referrer to delete
     * @param session the current HTTP session
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/deleteReferrer")
    public String referrerDelete(DTOReferrer dtoReferrer, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new ReferrerService().deleteReferrer(dtoReferrer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/gestion?tab=referents";
    }

    /**
     * Creates a new AcademicSkill in the database with the designation submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param session the current HTTP session
     * @param designation the designation of the AcademicSkill to create
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/addAcademicSkill")
    public String academicSkillAdd(HttpSession session, @RequestParam("designation") String designation) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new SkillService().addAcademicSkill(designation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/gestion?tab=competences";
    }

    /**
     * Creates a new ProfessionalSkill in the database with the designation submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param session the current HTTP session
     * @param designation the designation of the ProfessionalSkill to create
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/addProfessionalSkill")
    public String professionalSkillAdd(HttpSession session, @RequestParam("designation") String designation) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new SkillService().addProfessionalSkill(designation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/gestion?tab=competences";
    }
     /**
     * This function create an establishment in DB using the data put in the form.
     *
     * @param dtoEstablishment is the DTOEstablishment the user is trying to add.
     * @param model            is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/createEstablishment")
    public String addEstablishment(@ModelAttribute("DTOEstablishment") DTOEstablishment dtoEstablishment,
                                   Model model) {

        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.createEstablishment(dtoEstablishment);
        } catch (SQLException e) {
             //renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/gestion";
    }

    /**
     * This functions update the Establishment with the
     * Establishment the user put in the form
     *
     * @param dtoEstablishment is the DTOEstablishment to update.
     * @param model            is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("etablissements/updateEstablishment")
    public String updateEstablishment(@ModelAttribute("DTOEstablishment") DTOEstablishment dtoEstablishment,
                                      Model model) {
       EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.updateEstablishment(dtoEstablishment);
        } catch (SQLException e) {
             //renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/gestion";
    }

    /**
     * Retrieve all users along with the number of users, the number of interpreters,
     * the number of resas and the number of beneficiaries
     * @param model is param used by Spring to add all the data in the page.
     * @param session the current HTTP session
     * @return a redirection to the "coordinatrice/utilisateurs" page if the user is a coordinator connected.
     * Otherwise, return a redirection to the "/login" page
     */
    @GetMapping("/utilisateurs")
    public String utilisateurs(Model model, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null)
            return "redirect:/login";

        try{
            CoordinatorUsersService coordinatorUsersService = new CoordinatorUsersService();
            List<DTOUser> beneficiaries = coordinatorUsersService.findAllBeneficiary();
            model.addAttribute("beneficiaries", beneficiaries);

            List<DTOUser> interpreters = coordinatorUsersService.findAllInterpreters();
            model.addAttribute("interpreters", interpreters);

            List<DTOUser> resas = coordinatorUsersService.findAllResas();
            model.addAttribute("resas", resas);

            List<DTOUser> coordinators = coordinatorUsersService.findAllCoordinators();
            model.addAttribute("coordinators", coordinators);

            int numberBeneficiaries = coordinatorUsersService.countBeneficiaries();
            model.addAttribute("numberBeneficiaries", numberBeneficiaries);

            int numberInterpreters = coordinatorUsersService.countInterpreters();
            model.addAttribute("numberInterpreters", numberInterpreters);

            int numberResas = coordinatorUsersService.countResas();
            model.addAttribute("numberResas", numberResas);

            int numberUsers = numberBeneficiaries + numberInterpreters;
            model.addAttribute("numberUsers", numberUsers);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return "coordinatrice/utilisateurs";
    }

     //Temporaire
    @GetMapping("/utilisateurs/{id}")
    public String utilisateurDetail(@PathVariable String id, Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", true);

        switch (id) {
            case "1" -> model.addAttribute("userRole", "RESA");
            case "2" -> model.addAttribute("userRole", "INTERPRETER");
            case "3" -> model.addAttribute("userRole", "BENEFICIARY");
            case "4" -> model.addAttribute("userRole", "COORDINATOR");
            default -> model.addAttribute("userRole", "INTERPRETER");
        }

        return "coordinatrice/utilisateur-detail";
    }

    /**
     * Create a new coordinator, interpreter or beneficiary in the database using the datas submitted from the form
     * @param session the current HTTP session
     * @return a redirection to the "coordinatrice/utilisateurs" page if the user is a coordinator connected.
     *         Otherwise, return a redirection to the "/login" page
     */
    @PostMapping("utilisateurs/addUser")
    public String addUser(HttpSession session, @ModelAttribute DTOUserAdd dtoAddUser,
                          @RequestParam String role, Model model){
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null)
            return "redirect:/login";

        CoordinatorUsersService coordinatorUsersService = new CoordinatorUsersService();
        try{
            String result = "";
            switch (role) {
                case "1" -> result = coordinatorUsersService.addResaCoordinator(dtoAddUser, false);
                case "2" -> result = coordinatorUsersService.addInterpreter(dtoAddUser);
                case "3" -> result = coordinatorUsersService.addBeneficiary(dtoAddUser);
                case "4" -> result = coordinatorUsersService.addResaCoordinator(dtoAddUser, true);
            }

            model.addAttribute("message", result);

            return "redirect:/coordinatrice/utilisateurs";
        }
        catch(SQLException e) {
            e.printStackTrace();
            return "redirect:/coordinatrice/utilisateurs";
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            return "redirect:/coordinatrice/utilisateurs";
        }
    }

    /**
     * If the coordinator exists, the user will be redirected to the home page.
     * Otherwise, if it is null, the user will be redirected to the login page.
     *  @param session session the current HTTP session
     *  @return The HTML path to the home page if a coordinator is logged in, else redirects to /login.
     */
    @GetMapping("/accueil")
    public String accueil(HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null) {
            return "redirect:/login";
        }
        return "coordinatrice/accueil";
    }



}