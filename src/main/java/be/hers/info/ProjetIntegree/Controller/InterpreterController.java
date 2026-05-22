package be.hers.info.ProjetIntegree.Controller;
/**
 * @authors Halet Louis, Wellinger Chloe, Vatafu Jean, Rosman Loïs, Vanderheyden Quentin
 * @reviewer Nicolas Jean-François
 */

import be.hers.info.ProjetIntegree.DTO.*;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.AbsenceService;
import be.hers.info.ProjetIntegree.Services.AppointmentFormService;
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
     * Searches for all Appointments and Absences belonging to the interpreter as a parameter over a period defined by start and end.
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @param interpreter The interpreter linked to the appointment on the list
     * @return Redirect to the "interprete/planning" page
     */
    @GetMapping("/planning")
    public String planning(@RequestParam(defaultValue = "2026-05-15") String start,
                           @RequestParam(defaultValue = "2026-05-22") String end, @ModelAttribute("InterpreterConnected") Interpreter interpreter, Model model) {

        if(interpreter == null) {
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
     * @param interpreter The currently logged-in interpreter (from ModelAttribute, reference)
     * @param model the UI model to hold the list of absences and the active tab status
     * @return The view name "interprete/indisponibilites", or a redirect to login if session is invalid
     */
    @GetMapping("/indisponibilites")
    public String indisponibilites(@RequestParam String start,
                                   @RequestParam String end,
                                   @ModelAttribute("InterpreterConnected") Interpreter interpreter,
                                   Model model) {

        if(interpreter == null) {
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
        Interpreter interpreter = (Interpreter) session.getAttribute("InterpreterConnected");
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
}