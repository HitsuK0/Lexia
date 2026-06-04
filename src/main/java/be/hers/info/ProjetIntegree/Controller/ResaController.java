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
        return "resa/accueil";
    }

    // Temporaire
    @GetMapping("/profil")
    public String profil(Model model) {
        return "interprete/profil";
    }

    // Temporaire — réutilise le même template que la coordinatrice
    @GetMapping("/gestion")
    public String etablissements(Model model) {
        return "coordinatrice/gestion";
    }

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        return "resa/planning";
    }
}