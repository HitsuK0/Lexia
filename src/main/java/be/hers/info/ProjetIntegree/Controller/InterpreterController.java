package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.AbsenceService;
import be.hers.info.ProjetIntegree.Services.PlanningService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.util.List;

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
    @GetMapping("/planning/events")
    public String planning(@RequestParam String start,
                           @RequestParam String end, @ModelAttribute("InterpreterConnected") Interpreter interpreter, Model model) {

        if(interpreter == null) {
            return "redirect:/login";
        }

        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        interpreter.setAppointmentsList(planningService.getListAppointmentWithDateAndInterpreter(interpreter,dateStart,dateEnd));
        interpreter.setAbsences(planningService.getListAbsenceWithDateAndInterpreter(interpreter,dateStart,dateEnd));

        // Ici j'ai retirer la session, puisque je travaille direcement avec l'instence interpreter, je ne dois plus m'occuper de la session

        model.addAttribute("activeTab", "planning");

        return "interprete/planning";
    }

    /**
     * Searches for all Appointments belonging to the beneficiary as a parameter over a period defined by start and end.
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @param beneficiary The beneficiary linked to the appointment on the list
     * @param request the request that triggered this function call
     * @return Redirect to the "interprete/planning-beneficiaires" page
     */
    @GetMapping("/planning/beneficiaires")
    public String planningBeneficiaires(@RequestParam String start, @RequestParam String end,
                                        @ModelAttribute("BeneficiaryConnected") Beneficiary beneficiary,
                                        HttpServletRequest request, Model model) {

        if(beneficiary == null) {
            return "redirect:/login";
        }

        String dateStart = start.substring(0, 10);
        String dateEnd = end.substring(0, 10);

        PlanningService planningService = new PlanningService();
        List<Appointment> appointmentList = planningService.getListAppointmentsToBeneficiaryAndDate(
                beneficiary.getNumBeneficiary(), dateStart, dateEnd);

        HttpSession session = request.getSession();
        session.setAttribute("appointmentList", appointmentList);
        // Ici c'est pas vraiment pareil que l'autre, je travail pas vraiment avec l'objet Beneficiary dans la session (je pense)

        model.addAttribute("activeTab", "planning");

        return "interprete/planning/beneficiaires";
    }

    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "interprete/profil";
    }

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
            String startDate = null;
            String endDate = null;

            startDate = start.substring(0, 10);
            endDate = end.substring(0, 10);

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

    // Petit bouton poubelle
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

    // petit bouton pour modifier
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