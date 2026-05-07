package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.Services.PlanningService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDate;

@Controller
@RequestMapping("/interprete")
public class InterpreterController {

    /**
     * Recherche tous les Appointments et Absences appartenant à l'interprete en paramètre sur une période définie par start et end.
     * @param start
     * @param end
     * @param interpreter
     * @param request
     * @return Redirect to the "interprete/planning" page
     */
    @GetMapping("/planning/events")
    public String planning(@RequestParam String start,
                           @RequestParam String end, @SessionAttribute("InterpreterConnected") Interpreter interpreter, HttpServletRequest request) {
        String dateStart = start.substring(0,10);
        String dateEnd = end.substring(0,10);
        PlanningService planningService = new PlanningService();
        HttpSession session = request.getSession();
        interpreter.setAppointmentsList(planningService.getListAppointmentWithDateAndInterpreter(interpreter,dateStart,dateEnd));
        interpreter.setAbsences(planningService.getListAbsenceWithDateAndInterpreter(interpreter,dateStart,dateEnd));
        session.setAttribute("InterpreterConnected",interpreter );

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
}