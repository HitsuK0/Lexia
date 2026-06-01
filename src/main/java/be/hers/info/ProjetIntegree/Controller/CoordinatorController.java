package be.hers.info.ProjetIntegree.Controller;


import be.hers.info.ProjetIntegree.DTO.DTOAbsence;
import be.hers.info.ProjetIntegree.DTO.DTOEstablishment;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.Establishment;
import be.hers.info.ProjetIntegree.Services.EstablishementService;

import be.hers.info.ProjetIntegree.Services.ReferrerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.w3c.dom.html.HTMLDocument;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coordinatrice")
public class CoordinatorController {

    /**
     * Retrieves the connected coordinator from the session.
     * Returns null if no user is connected or if the connected user is not a Coordinator.
     * @param session the current HTTP session
     * @return the connected Coordinator, or null if not found
     */
    private Coordinator getCoordinatorFromSession(HttpSession session) {
        if (session == null) return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Coordinator) {
            return (Coordinator) user;
        }
        return null;
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
     * The page enable to add and modify an Establishment and to attribute a referrer for an Establishment.
     *
     * @param model is param used by Spring to add all the data in the page.
     * @return the page displayed for the users.
     */
    @GetMapping("/etablissements")
    public String etablissements(Model model, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null)
            return "redirect:login";
        String userName = coordinator.getLastName().toUpperCase() + " " + coordinator.getFirstName();
        model.addAttribute("userName", userName);
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        EstablishementService establishmentService = new EstablishementService();
        List<DTOEstablishment> listEstablishment = null;
        try {
            listEstablishment = establishmentService.getEtablissements();
        } catch (SQLException e) {
            // renvoyé sur la page d'erreur.
        }
        model.addAttribute("listEstablishment", listEstablishment);
        // listEstablishment is used for the display in the table.
        model.addAttribute("DTOEstablishmentAdd", new DTOEstablishment());
        // DTOEstablishmentAdd is used when the user try to add an Establishment
        model.addAttribute("DTOEstablishmentEdit", new DTOEstablishment());
        // DTOEstablishment is used when we try to modify an
        ReferrerService referrerService = new ReferrerService();
        List<DTOReferrer> allListReferrer = null;
        try {
            allListReferrer = referrerService.getAllReferrer();
        } catch (SQLException e) {
            //renvoyé sur la page d'erreur.
        }
        model.addAttribute("allListReferrer", allListReferrer);
        model.addAttribute("listReferrerSelected", new ArrayList<Integer>());
        return "coordinatrice/etablissements";
    }

    // Temporaire
    @GetMapping("/gestion")
    public String gestion(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);

        // Données hardcodés pour tester
        Establishment etab = new Establishment(1, "Haute École Provinciale de Hainaut - Condorcet", "061000000");
        Address adresse = new Address(6800, "Rue du Faubourg de la Prévoté, 142", "Fontaine-l'Évêque", null, null);
        etab.setAddresses(List.of(adresse));
        Referrer referrer = new Referrer(etab, "vanderberghe@example.com", "0476123456", "VANDERBERGHE-DUPONSELLE", "Jean-François");
        etab.setReferrers(List.of(referrer));

        model.addAttribute("etablissementList", List.of(etab));
        model.addAttribute("referentList", new ArrayList<>());
        model.addAttribute("professionalSkillList", new ArrayList<>());
        model.addAttribute("academicSkillList", new ArrayList<>());

        return "coordinatrice/gestion";
    }

    /**
     * This function create an establishment in DB using the data put in the form.
     *
     * @param dtoEstablishment is the DTOEstablishment the user is trying to add.
     * @param model            is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/createEstablishment")
    public String addEstablishment(@ModelAttribute("DTOEstablishmentAdd") DTOEstablishment dtoEstablishment,
                                   Model model) {
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.createEstablishment(dtoEstablishment);
        } catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }

    /**
     * This function add a Referrer in the database
     * using the data the user put in the form.
     *
     * @param model is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/addReferrer")
    public String attributeReferrer(
                            @ModelAttribute("DTOEstablishmentAdd") DTOEstablishment dtoEstablishment,
                            Model model) {

        ReferrerService referrerService = new ReferrerService();
        try {
            referrerService.attributeReferrer(dtoEstablishment.getListReferrerSelected(), dtoEstablishment.getNumEstablishment());

        } catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }

    /**
     * This functions update the Establishment with the
     * Establishment the user put in the form
     *
     * @param dtoEstablishment is the DTOEstablishment to update.
     * @param model            is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("etablissements/updateEstablishment")
    public String updateEstablishment(@ModelAttribute("DTOEstablishment") DTOEstablishment dtoEstablishment,
                                      Model model) {
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.updateEstablishment(dtoEstablishment);
        } catch (SQLException e) {
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
            default -> model.addAttribute("userRole", "INTERPRETER");
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

    /**
     * If the coordinator exists, the user will be redirected to the home page.
     * Otherwise, if it is null, the user will be redirected to the login page.
     *  @param session session the current HTTP session
     *  @return The HTML path to the home page if a coordinator is logged in, else redirects to /login.
     */
    @GetMapping("/accueil")
    public String accueil(HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null) {
            return "redirect:/login";
        }
        return "coordinatrice/accueil";
    }



}