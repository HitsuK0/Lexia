package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.DTO.DTOEstablishment;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.Services.EstablishementService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/coordinatrice")
public class CoordinatorController {

    // Temporaire
    @GetMapping("/accueil")
    public String accueil(Model model) {
        model.addAttribute("userName", "Dubois Louis");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/accueil";
    }

    // Temporaire
    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "interprete/profil";
    }

    // Temporaire
    @GetMapping("/plannings")
    public String plannings(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/plannings";
    }

    // Temporaire
    @GetMapping("/validations")
    public String validations(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/validations";
    }

    /**
     * This function load the page "etablissement".
     * It also add all the data needed for the display (all the establishment registered in DB)
     * @param model is param used by Spring to add all the data in the page.
     * @return the page displayed for the users.
     */
    @GetMapping("/etablissements")
    public String etablissements(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        EstablishementService establishmentService = new EstablishementService();
        List<DTOEstablishment> listEstablishment = establishmentService.getEtablissements();
        model.addAttribute("listEstablishment", listEstablishment);
        model.addAttribute("DTOEstablishmentAdd", new DTOEstablishment());
        model.addAttribute("DTOEstablishmentEdit", new DTOEstablishment());
        model.addAttribute("DTOReferrerAdd", new DTOReferrer());
        return "coordinatrice/etablissements";
    }

    /**
     * This function create an establishment in DB using the data put in the form.
     * @param dtoEstablishment is the DTOEstablishment the user is trying to add.
     * @param model is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/createEstablishment")
    public String addEstablishment(@ModelAttribute("DTOEstablishmentAdd") DTOEstablishment dtoEstablishment,
                                     Model model){
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.createEstablishment(dtoEstablishment);
        }
        catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }

    /**
     * This function add a Referrer in the database
     * using the data the user put in the form.
     * @param dtoReferrer is the DTOReferrer to add in the database
     * @param model is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/createReferrer")
    public String addReferrer(@ModelAttribute("DTOReferrer") DTOReferrer dtoReferrer,
                                     Model model){
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.createReferrer(dtoReferrer);
        }
        catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }

    /**
     * This functions update the Establishment with the
     * Establishment the user put in the form
     * @param dtoEstablishment is the DTOEstablishment to update.
     * @param model is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("etablissements/updateEstablishment")
    public String updateEstablishment(@ModelAttribute("DTOEstablishment") DTOEstablishment dtoEstablishment,
                              Model model){
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.updateEstablishment(dtoEstablishment);
        }
        catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }


    // Temporaire
    @GetMapping("/utilisateurs")
    public String utilisateurs(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
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

    // Temporaire
    @GetMapping("/horaire/base")
    public String horaireBase(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/horaire-base";
    }

    // Temporaire
    @GetMapping("/gestion/competences")
    public String gestionCompetences(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/gestion-competences";
    }
}