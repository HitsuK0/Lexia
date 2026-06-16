package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOAppointmentForm;
import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.AppointmentFormService;
import be.hers.info.ProjetIntegree.Services.AppointmentService;
import be.hers.info.ProjetIntegree.Services.BeneficiaryProfileService;
import be.hers.info.ProjetIntegree.Services.PlanningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
@Controller
@RequestMapping("/beneficiaire")
public class BeneficiaryController {

    private static final Logger logger = LoggerFactory.getLogger(BeneficiaryController.class);

    /**
     * Retrieves the connected beneficiary from the session.
     * Returns null if no user is connected or if the connected user is not a Beneficiary.
     * This helper avoids relying on @ModelAttribute which may inject an empty POJO
     * instead of null when no session attribute exists.
     *
     * @param session the current HTTP session
     * @return the connected Beneficiary, or null if not found
     */
    private Beneficiary getBeneficiaryFromSession(HttpSession session) {
        if (session == null) return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Beneficiary) {
            return (Beneficiary) user;
        }
        return null;
    }

    /**
     * Controller for the pages named "Mon planning"
     * Displays the weekly calendar page for the connected beneficiary.
     * Pass on model the establishmentList, the academicSkillList and the professionalSkillList
     * Reads the beneficiary directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "beneficiaire/planning", or a redirect to "/login"
     */
    @GetMapping("/planning")
    public String planning(HttpSession session, Model model) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }
        AppointmentFormService service = new AppointmentFormService();
        model.addAttribute("establishmentList", service.findAllEstablishments());
        model.addAttribute("academicSkillList", service.findAllAcademicSkills());
        model.addAttribute("professionalSkillList", service.findAllProfessionalSkills());
        DTOAppointmentForm dto = new DTOAppointmentForm();
        dto.setNumBeneficiary(beneficiary.getNumBeneficiary());
        model.addAttribute("DTOAppointmentForm", dto);
        model.addAttribute("activeTab", "planning");
        return "beneficiaire/planning";
    }

    /**
     * Search all Appointments within the Start and End time range linked to the connected beneficiary.
     * Format the information found in a list on the Map for FullCalendar
     *
     * @param start   the start date of the schedule
     * @param end     the end date of the schedule
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return a formatted map list for FullCalendar
     */
    @GetMapping(value = "/planning/events", produces = "application/json")
    @ResponseBody
    public List<Map<String, Object>> getEventsPlaningBeneficiary(@RequestParam String start,
                                                                 @RequestParam String end, HttpSession session, Model model) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return Collections.emptyList();
        }
        String dateStart = start.substring(0, 10);
        String dateEnd = end.substring(0, 10);
        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentsToBeneficiaryAndDate(beneficiary.getNumBeneficiary(), dateStart, dateEnd);
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

            String interpreterNames = a.getInterpreters().stream()
                    .map(i -> i.getLastName().substring(0, 1) + ". " + i.getFirstName())
                    .collect(Collectors.joining(", "));

            boolean isBase = !(a.getTimeSlot() instanceof TimeSlotPunctual);

            extendedProps.put("type", "appointment");
            extendedProps.put("status", a.getStatus());
            extendedProps.put("professionalSkills", professionalSkills);
            extendedProps.put("interpreter", interpreterNames);
            extendedProps.put("locals", a.getAppointmentLocals());
            extendedProps.put("establishment", a.getEstablishment().getNameBuilding());
            extendedProps.put("description", a.getDescription());
            extendedProps.put("isBase", isBase);
            event.put("extendedProps", extendedProps);
            events.add(event);

        }
        return events;
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
    @PostMapping(value = "/planning/rdv", consumes = "application/json")
    @ResponseBody
    public String createRDVPlanning(@RequestBody DTOAppointmentForm dtoAppointment, HttpSession session) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            AppointmentFormService service = new AppointmentFormService();
            boolean success = service.createAppointment(dtoAppointment);
            return success ? "ok" : "error";
        } catch (BadStatusException | SQLException | IllegalArgumentException e) {
            logger.error("Erreur lors de la création du RDV planning pour le bénéficiaire {}", beneficiary.getNumBeneficiary(), e);
            return "error";
        }
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
    @PostMapping(value = "/demandes/rdv", consumes = "application/json")
    @ResponseBody
    public String createRDVDemandes(@RequestBody DTOAppointmentForm dtoAppointment, HttpSession session) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            AppointmentFormService service = new AppointmentFormService();
            dtoAppointment.setNumBeneficiary(beneficiary.getNumBeneficiary());
            boolean success = service.createAppointment(dtoAppointment);
            return success ? "ok" : "error";
        } catch (BadStatusException | SQLException | IllegalArgumentException e) {
            logger.error("Erreur lors de la création du RDV demandes pour le bénéficiaire {}", beneficiary.getNumBeneficiary(), e);
            return "error";
        }
    }

    /**
     * Controller for the pages named "Mes demandes"
     * Displays the list of appointment requests for the connected beneficiary.
     * Reads the beneficiary directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "beneficiaire/demandes", or a redirect to "/login"
     */
    @GetMapping("/demandes")
    public String demandes(HttpSession session, Model model, @RequestParam(required = false) String status) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        AppointmentFormService service = new AppointmentFormService();
        model.addAttribute("establishmentList", service.findAllEstablishments());
        model.addAttribute("academicSkillList", service.findAllAcademicSkills());
        model.addAttribute("professionalSkillList", service.findAllProfessionalSkills());
        List<Appointment> appointmentList = new ArrayList<Appointment>();

        AppointmentService appointmentService = new AppointmentService();
        appointmentList = appointmentService.findRequestsForBeneficiary(beneficiary, status);

        model.addAttribute("requests", appointmentList);
        model.addAttribute("status", status);
        model.addAttribute("activeTab", "demandes");
        model.addAttribute("numBeneficiary", beneficiary.getNumBeneficiary());
        return "beneficiaire/demandes";
    }

    /**
     * Controller for the trash icon button on the "Mes demandes" page.
     * Deletes the appointment request identified by numAppointment.
     * Reads the beneficiary directly from the session.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param numAppointment the id of the appointment request to delete
     * @param session        the current HTTP session
     * @return a redirect to "/beneficiaire/demandes" after the deletion attempt,
     * a redirect to "/login" if the session is invalid
     */
    @PostMapping("/demandes/delete")
    public String deleteRDV(@RequestParam int numAppointment, HttpSession session) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            AppointmentService appointmentService = new AppointmentService();
            appointmentService.deleteAppointmentRequest(numAppointment);
        } catch (SQLException | BadStatusException e) {
            logger.error("Erreur lors de la suppression du RDV {}", numAppointment, e);
        }
        return "redirect:/beneficiaire/demandes";
    }

    /**
     * Controller for the pages named "Mon profil"
     * Displays the profile page for the connected beneficiary.
     * Reads the beneficiary directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Builds a {@link DTOBeneficiaryProfile} from the connected beneficiary and adds it to the model so the Thymeleaf form can bind its fields.
     * Also adds an empty {@link DTOPasswordChange} for the password change modal.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "beneficiaire/profil", or a redirect to "/login"
     */
    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        BeneficiaryProfileService profileService = new BeneficiaryProfileService();
        DTOBeneficiaryProfile profileDTO = profileService.buildProfileDTO(beneficiary);

        model.addAttribute("profileDTO", profileDTO);
        model.addAttribute("passwordDTO", new DTOPasswordChange());
        model.addAttribute("activeTab", "profil");

        return "beneficiaire/profil";
    }

    /**
     * Controller for the pages named "Mon profil"
     * Handles the submission of the profile edit form.
     * Saves the modified personal data (lastName, firstName, phoneNumber, emailAddress, address) of the connected beneficiary.
     * The login and password are NOT modified here.
     * Reads the beneficiary directly from the session.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/beneficiaire/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOBeneficiaryProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            BeneficiaryProfileService profileService = new BeneficiaryProfileService();
            profileService.saveProfile(beneficiary, profileDTO);
            session.setAttribute("currentUser", beneficiary);
        } catch (SQLException e) {
            logger.error("Erreur lors de la sauvegarde du profil bénéficiaire {}", beneficiary.getNumBeneficiary(), e);
        }

        return "redirect:/beneficiaire/profil";
    }

    /**
     * Controller for the pages named "Mon profil"
     * Handles the submission of the password change modal.
     * Verifies that newPassword and confirmPassword match, then updates the password in the database.
     * The DB trigger will hash the new password automatically on UPDATE.
     * Reads the beneficiary directly from the session.
     * Redirects to login if no beneficiary is found in session.
     * Redirects back to the profile page with an error parameter if the passwords do not match.
     *
     * @param passwordDTO the password change form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/beneficiaire/profil" after the operation,
     * with "?passwordError=true" appended if passwords do not match,
     * or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            BeneficiaryProfileService profileService = new BeneficiaryProfileService();
            boolean success = profileService.changePassword(beneficiary, passwordDTO);
            if (!success) {
                return "redirect:/beneficiaire/profil?passwordError=true";
            }
        } catch (SQLException e) {
            logger.error("Erreur lors du changement de mot de passe bénéficiaire {}", beneficiary.getNumBeneficiary(), e);
        }

        return "redirect:/beneficiaire/profil";
    }
}