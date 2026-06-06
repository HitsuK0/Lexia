package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Coordinator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/resa")
public class ResaController {

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
    @GetMapping("/accueil")
    public String accueil(HttpSession session, Model model) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null) {
            return "redirect:/login";
        }
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