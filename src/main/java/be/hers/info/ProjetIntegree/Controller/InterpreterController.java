package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/interprete")
public class InterpreterController {

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "interprete/planning";
    }

    //Horaire :
    // - Chercher tous les RDV des bénéficiaires de l'utilisateur connecté
    @GetMapping("/planning/beneficiaires")
    public String planningBeneficiaires(@RequestParam String start, @RequestParam String end,
                                        @SessionAttribute("BeneficiaryConnected") Beneficiary beneficiary, Model model,
                                        HttpServletRequest request) {
        String dateStart = start.substring(0, 10);
        String dateEnd = end.substring(0, 10);

        List<Appointment> appointmentList = List.of();
        
        
        HttpSession session = request.getSession();
        session.setAttribute("appointmentList", appointmentList);

        return "interprete/planning-beneficiaires";
    }

    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        return "interprete/profil";
    }
}