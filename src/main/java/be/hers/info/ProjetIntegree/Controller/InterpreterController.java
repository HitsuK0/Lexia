package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.POJO.Absence;
import be.hers.info.ProjetIntegree.Service.ServiceAbsence;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/interprete")
public class InterpreterController {

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        model.addAttribute("userName", "NOM Prenom");
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