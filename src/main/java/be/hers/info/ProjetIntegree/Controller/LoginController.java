package be.hers.info.ProjetIntegree.Controller;

/**
 * @authors Halet Louis, Vatafu Jean
 * @reviewer
 */

import be.hers.info.ProjetIntegree.DTO.LoginDTO;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.User;
import be.hers.info.ProjetIntegree.Services.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    /**
     * Displays the login page and prepares an empty LoginDTO that will be
     * completed in the Thymeleaf form fields submitted by the user
     * @param model the Spring UI model
     * @return the view name "login"
     */
    @GetMapping("login")
    public String loginPage(Model model) {
        model.addAttribute("LoginDTO", new LoginDTO());

        return "login";
    }

    /**
     * Handles the submission of the login form
     * Authenticates the user and stores it in the HTTP session
     * Redirects to the home page matching their role
     * @param loginDTO the DTO bound to the login form
     * @param request the current HTTP request, used to access the session
     * @return a redirect view name to the role-specific home page, or to
     *         "/login" if authentication fails
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("LoginDTO") LoginDTO loginDTO,
                        HttpServletRequest request) {

        User user = null;
        LoginService loginService = new LoginService();
        user = loginService.getAuthentification(loginDTO.getLogin(), loginDTO.getPassword());

        if(user == null) {
            return "redirect:/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);

        if(user instanceof Coordinator) {
            if(((Coordinator) user).isAdmin()) {
                return "redirect:/coordinatrice/accueil";
            }
            return "redirect:/Resa/accueil";
        } else if (user instanceof Interpreter) {
            return "redirect:/interprete/planning";
        } else if(user instanceof Beneficiary) {
            return "redirect:/beneficiaire/planning";
        }

        return "redirect:/login";
    }
}

