package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Jean-François Nicolas, Jean Vatafu, Ainhoa Leroy Rodriguez
 * @reviewer Jean-François Nicolas , Louis Halet
 */

@Controller
@ControllerAdvice
public class NavbarController {

    private final HttpSession httpSession;

    public NavbarController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * Logs out the current user and destroys all data stored in the session
     *
     * @param session The current session to be destroyed
     * @return Redirect to the "/login" page
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Populates the model with specific attributes required for the navigation bar
     * and UI. It determines the user's role and specific identity
     * (Interpreter, Coordinator, or Beneficiary) based on their login prefix
     * @param model The UI model to be populated with attributes
     */
    @ModelAttribute
    public void addNavbarAttributes(HttpSession session,
                                    Model model) {
        User connectedUser = (User) session.getAttribute("currentUser");

        String userRole = null;
        boolean isAdmin = false;

        if(connectedUser != null) {
            model.addAttribute("userName", connectedUser.getFirstName()+" "+connectedUser.getLastName());
            if(connectedUser.getLogin().charAt(0) == 'I') {
                userRole = "INTERPRETER";
                model.addAttribute("InterpreterConnected", connectedUser);
            } else if(connectedUser.getLogin().charAt(0) == 'C') {
                userRole = "COORDINATOR";
                if(connectedUser instanceof Coordinator) {
                    isAdmin = ((Coordinator) connectedUser).isAdmin();
                }
                model.addAttribute("CoordinatorConnected", connectedUser);
            } else if(connectedUser.getLogin().charAt(0) == 'B') {
                userRole = "BENEFICIARY";
                model.addAttribute("BeneficiaryConnected", connectedUser);
            }

            model.addAttribute("currentUser", connectedUser);
            model.addAttribute("userName", connectedUser.getFirstName()+" "+connectedUser.getLastName());
            model.addAttribute("userRole", userRole);
            model.addAttribute("isAdmin", isAdmin);
        }
    }


    /**
     * Redirects the user to their schedule
     * @param session The session containing the connected user
     *@return The HTML path to the schedule page if a user is logged in, else redirects to /login.
     */
    @GetMapping("/planning")
    public String displayHomePage(HttpSession session) {

        User connectedUser = (User) session.getAttribute("currentUser");
        String redirection = "";

        if(connectedUser == null) {
            redirection = "redirect:/login";
        }else{

            char userRole = connectedUser.getLogin().charAt(0);

            if(userRole == 'C'){
                if(((Coordinator) connectedUser).isAdmin()){
                    redirection = "redirect:/coordinatrice/accueil";
                }else{
                    redirection = "redirect:/resa/accueil";
                }
            }else if(userRole == 'I'){
                redirection = "redirect:/interprete/planning";
            }else if(userRole == 'B'){
                redirection = "redirect:/beneficiaire/planning";
            }
        }
        return redirection;
    }
}
