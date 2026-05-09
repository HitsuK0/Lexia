package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        // Donc il y aurait un cle userConnected pour dire "ouais l'utilisateur est connecte"
        // On sait pas qui il est mais on sait qu'il y a un utilisateur
    }

    /**
     * Populates the model with specific attributes required for the navigation bar
     * and UI. It determines the user's role and specific identity
     * (Interpreter, Coordinator, or Beneficiary) based on their login prefix
     * @param connectedUser The user retrieved from the session (passed by reference)
     * @param model The UI model to be populated with attributes
     */
    @ModelAttribute
    public void addNavbarAttributes(@ModelAttribute("connectedUser") User connectedUser, // ici on passe le modelattribute par reference(il me semble
                                    // et il va etre modifier en fonction)
                                    Model model) {

        String userRole = null;
        boolean isAdmin = false;

        if(connectedUser != null) {
            model.addAttribute("userName", connectedUser.getFirstName()+" "+connectedUser.getLastName());
            if(connectedUser.getLogin().charAt(0) == 'I') {
                userRole = "INTERPRETER";
                model.addAttribute("InterpreterConnected", connectedUser); // j'adapte la cle utilise en thymleaf comme ca
                                                                                        // quand par exemple InterpreterController va utiliser le
                                                                                        // ModelAttribute("InterpreterConnected") ca va s'adapter, pareil pour les autres
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

        // Ici je ne sais pas quoi rajouter comme else si la connectedUser est null,
        // je pourrais faire un simple return; mais jsp c'est moche et il y apeut etre moyen de faire autrement
    }
}
