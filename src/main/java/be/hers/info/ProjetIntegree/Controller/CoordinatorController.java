package be.hers.info.ProjetIntegree.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Temporaire
    @GetMapping("/utilisateurs/{id}")
    public String utilisateurDetail(@PathVariable String id, Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", true);

        switch (id) {
            case "1" -> model.addAttribute("userRole", "RESA");
            case "2" -> model.addAttribute("userRole", "INTERPRETER");
            case "3" -> model.addAttribute("userRole", "BENEFICIARY");
            case "4" -> model.addAttribute("userRole", "COORDINATOR");
            default  -> model.addAttribute("userRole", "INTERPRETER");
        }

        return "coordinatrice/utilisateur-detail";
    }
}