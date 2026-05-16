package be.hers.info.ProjetIntegree.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/beneficiaire")
public class BeneficiaryController {

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        return "beneficiaire/planning";
    }

    // Temporaire
    @GetMapping("/demandes")
    public String demandes(Model model) {
        return "beneficiaire/demandes";
    }

    // Temporaire
    @GetMapping("/profil")
    public String profil(Model model) {
        return "beneficiaire/profil";
    }
}
