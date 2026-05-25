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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;

// TODO: Delete default values from RequestParam once true params can be passed

@Controller
@RequestMapping("/interprete")
public class InterpreterController {
    /**
     * Retrieves the connected interpreter from the session.
     * Returns null if no user is connected or if the connected user is not a interpreter.
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
     * Searches for all Appointments and Absences belonging to the interpreter as a parameter over a period defined by start and end.
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @return Redirect to the "interprete/planning" page
     */
    @GetMapping("/planning")
    public String planning(@RequestParam(defaultValue = "2026-05-15") String start,
                           @RequestParam(defaultValue = "2026-05-22") String end, HttpSession session, Model model) {
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }


        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        interpreter.setAppointmentsList(planningService.getListAppointmentWithDateAndInterpreter(interpreter,dateStart,dateEnd));
        interpreter.setAbsences(planningService.getListAbsenceWithDateAndInterpreter(interpreter,dateStart,dateEnd));

        model.addAttribute("activeTab", "planning");

        model.addAttribute("DTOAbsence", new DTOAbsence());
        return "interprete/planning";
    }

    /**
     * Searches for all Appointments belonging to the beneficiary as a parameter over a period defined by start and end.
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @param beneficiary The beneficiary linked to the appointment on the list
     * @param request the request that triggered this function call
     * @return Redirect to the "interprete/planning/beneficiaires" page
     */
    @GetMapping("/planning/beneficiaires")
    public String planningBeneficiaires(@RequestParam(defaultValue = "2026-05-15") String start,
                                        @RequestParam(defaultValue = "2026-05-22") String end,
                                        @ModelAttribute("BeneficiarySelected") Beneficiary beneficiary,
                                        @ModelAttribute("InterpreterConnected") Interpreter interpreter,
                                        HttpServletRequest request, Model model) {
        if(interpreter == null)
            return "redirect:/login";

        String dateStart = start.substring(0, 10);
        String dateEnd = end.substring(0, 10);

        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentsToBeneficiaryAndDate(
                beneficiary.getNumBeneficiary(), dateStart, dateEnd);

        HttpSession session = request.getSession();
        session.setAttribute("appointmentList", appointmentList);
        model.addAttribute("activeTab", "planning");

        AppointmentFormService appointmentFormService = new AppointmentFormService();
        List<DTOBeneficiaryFormAppointment> hisBeneficiaries = appointmentFormService.findHisBeneficiaries(interpreter.getNumInterpreter());
        model.addAttribute("beneficiariesList", hisBeneficiaries);

        List<DTOEstablishmentFormAppointment> establishments = appointmentFormService.findAllEstablishments();
        model.addAttribute("establishmentList", establishments);

        List<AcademicSkill> academicSkills = appointmentFormService.findAllAcademicSkills();
        model.addAttribute("academicSkillList", academicSkills);

        List<ProfessionalSkill> professionalSkills = appointmentFormService.findAllProfessionalSkills();
        model.addAttribute("professionalSkillList", professionalSkills);

        return "interprete/planning-beneficiaires";
    }

    /**
     * Add the appointment created by the interpreter to the DB
     * @param dtoAppointment the appointment to add to the DB
     * @param interpreter the user connected
     * @return a redirect to the planning page after the creation
     */
    @PostMapping("/planning/beneficiaires")
    public String addAppointment(@ModelAttribute("newAppointment") DTOAppointmentForm dtoAppointment,
                                 @ModelAttribute("InterpreterConnected") Interpreter interpreter,
                                 RedirectAttributes redirectAttributes){
        if(interpreter == null)
            return "redirect:/login";

        try {
            AppointmentFormService appointmentFormService = new AppointmentFormService();
            if(appointmentFormService.createAppointment(dtoAppointment))
                redirectAttributes.addFlashAttribute("successMessage", "Rendez-vous créé avec succès");
            else
                redirectAttributes.addFlashAttribute("errorMessage", "La création du rendez-vous a échoué");
        }
        catch(BadStatusException | SQLException | IllegalArgumentException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue : " + e.getMessage());
        }

        return "redirect:/interprete/planning/beneficiaires";
    }

    @GetMapping("/profil")
    public String profil(Model model) {
        return "interprete/profil";
    }

    /**
     * Displays the list of punctual absences for the connected interpreter within a specific date range
     * The method extracts the date from the start and end parameters and retrieves
     * matching absences from the database
     * @param start The start date
     * @param end The end date
     * @param model the UI model to hold the list of absences and the active tab status
     * @return The view name "interprete/indisponibilites", or a redirect to login if session is invalid
     */
    @GetMapping("/indisponibilites")
    public String indisponibilites(@RequestParam(defaultValue = "2026-05-15") String start,
                                   @RequestParam(defaultValue = "2026-05-15") String end,
                                   HttpServletRequest request,
                                   Model model) {
        HttpSession session = request.getSession();
        Interpreter interpreter = getInterpreterFromSession(session);
        if (interpreter == null) {
            return "redirect:/login";
        }

        try {
            AbsenceService absenceService = new AbsenceService();
            String startDate = start.substring(0, 10);
            String endDate = end.substring(0, 10);

            List<Absence> punctualAbsencesList = absenceService.getPunctualAbsencesInterpreter(interpreter, startDate, endDate);
            model.addAttribute("punctualAbsencesList", punctualAbsencesList);
        } catch (BadStatusException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("activeTab", "indisponibilites");
        return "interprete/indisponibilites";
    }


    /**
     * Function called when the form is filled.
     * Also redirect to the indsponibilites page.
     * It creates an Absence in the Database.
     * @param dtoAbsence the dto to convert into a pojo
     * @param model
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
        return "interprete/indisponibilites";
    }

    /**
     * Deletes a specific absence record based on its unique ID
     * @param id the unique identifier of the absence to be deleted
     * @param interpreter The currently logged-in interpreter
     * @return A redirect to the absences list view after deletion
     */
    @PostMapping("/indisponibilites/delete")
    public String deleteAbsence(@RequestParam int id,
                                @ModelAttribute("InterpreterConnected") Interpreter interpreter) {

        if(interpreter == null) {
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
     * @param interpreter the currently logged-in interpreter
     * @return A redirect to the absences list view after the update is processed
     */
    @PostMapping("/indisponibilites/update")
    public String updateAbsence(@ModelAttribute Absence updatedAbsence,
                                @ModelAttribute("InterpreterConnected") Interpreter interpreter) {

        if(interpreter == null) {
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


}