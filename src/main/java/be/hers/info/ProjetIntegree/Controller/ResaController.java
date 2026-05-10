package be.hers.info.ProjetIntegree.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/resa")
public class ResaController {

    // Temporaire
    @GetMapping("/accueil")
    public String accueil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", false);
        return "resa/accueil";
    }

    // Temporaire
    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", false);
        return "interprete/profil";
    }

    // Temporaire — réutilise le même template que la coordinatrice
    @GetMapping("/etablissements")
    public String etablissements(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", false);
        return "coordinatrice/etablissements";
    }

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", false);
        return "resa/planning";
    }
}