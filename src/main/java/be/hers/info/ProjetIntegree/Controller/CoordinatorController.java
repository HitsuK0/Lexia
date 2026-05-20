package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Coordinator;
import jakarta.servlet.http.HttpSession;
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

    // Temporaire
    @GetMapping("/etablissements")
    public String etablissements(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/etablissements";
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



    /**
     Retrieves the connected coordinator from the session.
     Returns null if no user is connected or if the connected user is not a Coordinator.
     @param session the current HTTP session
     @return the connected Coordinator, or null if not found
     */
    private Coordinator getCoordinatorFromSession(HttpSession session) {
        if (session == null) return null;
        Coordinator coordinator = (Coordinator) session.getAttribute("currentUser");
        if (coordinator == null) {
            return null;}
        return coordinator;}

    /**
     * If the coordinator exists, the user will be redirected to the home page.
     * Otherwise, if it is null, the user will be redirected to the login page.
     *  @param model The UI model to be populated with attributes
     *  @param session session the current HTTP session
     *  @return The HTML path to the home page if a coordinator is logged in, else redirects to /login.
     */
    @GetMapping("/accueil")
    public String accueil(Model model, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", coordinator.getLastName()+" "+coordinator.getFirstName());
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", coordinator.isAdmin());
        return "coordinatrice/accueil";
    }



}