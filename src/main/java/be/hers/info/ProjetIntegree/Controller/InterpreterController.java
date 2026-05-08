package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.Service.ServiceAbsence;
import be.hers.info.ProjetIntegree.Services.PlanningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/interprete")
public class InterpreterController {


    /**
     * Searches for all Appointments and Absences belonging to the interpreter as a parameter over a period defined by start and end.
     * @param start the date retrieved via the URL
     * @param end the date retrieved via the URL
     * @param interpreter The interpreter linked to the appointment on the list
     * @param request the request that triggered this function call
     * @return Redirect to the "interprete/planning" page
     */
    @GetMapping("/planning/events")
    public String planning(@RequestParam String start,
                           @RequestParam String end, @SessionAttribute("InterpreterConnected") Interpreter interpreter, HttpServletRequest request, Model model) {
        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        HttpSession session = request.getSession();
        interpreter.setAppointmentsList(planningService.getListAppointmentWithDateAndInterpreter(interpreter,dateStart,dateEnd));
        interpreter.setAbsences(planningService.getListAbsenceWithDateAndInterpreter(interpreter,dateStart,dateEnd));
        session.setAttribute("InterpreterConnected",interpreter );
        model.addAttribute("isAdmin",null);

        return "interprete/planning";
    }

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("DTOAbsence", new DTOAbsence());
        return "interprete/planning";
    }

    // Temporaire
    @GetMapping("/planning/beneficiaires")
    public String planningBeneficiaires(Model model) {
        return "interprete/planning-beneficiaires";
    }

    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "interprete/profil";
    }

    @GetMapping("/indisponibilites")
    public String indisponibilites(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "interprete/indisponibilites";
    }

    /**
     * Function called when the form is filled.
     * Also redirect to the indsponibilites page.
     * @param dtoAbsence
     * @param model
     * @return
     */
    @PostMapping("/indisponibilites")
    public String createIndisponibilite(@ModelAttribute("DTOAbsence") DTOAbsence dtoAbsence, Model model) {
        ServiceAbsence serviceAbsence = new ServiceAbsence();
        Absence absence = new Absence();
        serviceAbsence.createAbsence(dtoAbsence, absence);
        return "interprete/indisponibilites";
    }
}