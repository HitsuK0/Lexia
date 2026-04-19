package be.hers.info.ProjetIntegree.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/interprete")
public class InterpreteController {

    @GetMapping("/planning")
    public String planning(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "interprete/planning";
    }
}