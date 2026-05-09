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

    @ModelAttribute("connectedUser")
    public User getConnectedUser(HttpSession session) {
        return (User) session.getAttribute("userConnected");
        // Donc il y aurait un cle userConnected pour dire "ouais l'utilisateur est connecte"
        // On sait pas qui il est mais on sait qu'il y a un utilisateur
    }

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
