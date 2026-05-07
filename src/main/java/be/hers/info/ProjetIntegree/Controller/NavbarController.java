package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
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
     * Displays the Absences page of the connected user
     * This method requires that a User in the session
     * @param session The session containing the connected user
     * @param model The Thymeleaf view container
     * @return HTML path interprete/indisponibilites if any user connected, redirect:/login if no user
     * connected (or in case of any other issue)
     */
    @GetMapping("interprete/indisponibilites")
    public String displayAbsencesPage(HttpSession session, Model model) {
        User connectedUser = (User) session.getAttribute("user");

        if(connectedUser == null) {
            return "redirect:/login";
        }

        String userRole = null;
        char roleLetter = connectedUser.getLogin().charAt(0);

        if(roleLetter == 'I') {
            userRole = "INTERPRETER";
        } else if(roleLetter == 'C') {
            userRole = "COORDINATOR";
        } else if(roleLetter == 'B') {
            userRole = "BENEFICIARY";
        } else {
            return "redirect:/login";
        }

        model.addAttribute("userRole", userRole);
        model.addAttribute("userName", connectedUser.getFirstName()+" "+connectedUser.getLastName());
        model.addAttribute("activeTab", "indisponibilites");

        return "interprete/indisponibilites";
    }
}
