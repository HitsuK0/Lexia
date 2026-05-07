package be.hers.info.ProjetIntegree.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/coordinatrice")
public class CoordinatorController {

    // Temporaire
    @GetMapping("/accueil")
    public String accueil(Model model) {
        model.addAttribute("userName", "Dubois Louis");
        return "coordinatrice/accueil";
    }

    // Temporaire
    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "coordinatrice/profil";
    }

    // Temporaire
    @GetMapping("/plannings")
    public String plannings(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "coordinatrice/plannings";
    }

    // Temporaire
    @GetMapping("/validations")
    public String validations(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "coordinatrice/validations";
    }

    // Temporaire
    @GetMapping("/etablissements")
    public String etablissements(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "coordinatrice/etablissements";
    }

    // Temporaire
    @GetMapping("/utilisateurs")
    public String utilisateurs(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "coordinatrice/utilisateurs";
    }
}