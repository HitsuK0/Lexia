package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Interpreter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;

@Controller
@RequestMapping("/interprete")
public class InterpreteController {

    // Temporaire
    @GetMapping("/planning")
    public String planning(@RequestParam("date") LocalDate date, @SessionAttribute("InterpreterConnected") Interpreter interpreter, Model model) {

        //model.addAttribute("userName", "NOM Prenom");
        return "interprete/planning";
    }

    // Temporaire
    @GetMapping("/planning/beneficiaires")
    public String planningBeneficiaires(Model model) {
        return "interprete/planning-beneficiaires";
    }
}