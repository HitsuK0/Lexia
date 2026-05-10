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
     * Retrieves the currently authenticated user from the session
     * This object is added to the model under the name "connectedUser"
     * @param session The current session
     * @return The User object stored in the session, or null if no user is connected
     */
    @ModelAttribute("connectedUser")
    public User getConnectedUser(HttpSession session) {
        return (User) session.getAttribute("userConnected");
    }

    /**
     * Populates the model with specific attributes required for the navigation bar
     * and UI. It determines the user's role and specific identity
     * (Interpreter, Coordinator, or Beneficiary) based on their login prefix
     * @param connectedUser The user retrieved from the session (passed by reference)
     * @param model The UI model to be populated with attributes
     */
    @ModelAttribute
    public void addNavbarAttributes(@ModelAttribute("connectedUser") User connectedUser,
                                    Model model) {

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

        User connectedUser = (User) session.getAttribute("user");
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
