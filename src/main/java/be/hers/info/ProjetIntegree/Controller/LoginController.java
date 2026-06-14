package be.hers.info.ProjetIntegree.Controller;

/**
 * @authors Halet Louis, Vatafu Jean
 * @reviewer
 */

import be.hers.info.ProjetIntegree.DTO.DTOLogin;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    /**
     * Displays the login page and prepares an empty LoginDTO that will be
     * completed in the Thymeleaf form fields submitted by the user
     * @param model the Spring UI model
     * @return the view name "login"
     */
    @GetMapping("login")
    public String loginPage(Model model,
                            @RequestParam(value = "error", required = false)
                            String error) {
        model.addAttribute("LoginDTO", new DTOLogin());
        if(error != null) model.addAttribute("error", error);

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
    public String login(@ModelAttribute("LoginDTO") DTOLogin loginDTO,
                        HttpServletRequest request) {

        User user = null;
        LoginService loginService = new LoginService();
        user = loginService.getAuthentification(loginDTO.getLogin(), loginDTO.getPassword());

        if(user == null) {
            return "redirect:/login?error";
        }

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);

        if(user instanceof Coordinator) {
            if(((Coordinator) user).isAdmin()) {
                return "redirect:/coordinatrice/accueil";
            }
            return "redirect:/resa/accueil";
        } else if (user instanceof Interpreter) {
            return "redirect:/interprete/planning";
        } else if(user instanceof Beneficiary) {
            return "redirect:/beneficiaire/planning";
        }

        return "redirect:/login?error";
    }
}

