package be.hers.info.ProjetIntegree.Controller;

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

    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("LoginDTO", new LoginDTO());

        return "/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("LoginDTO") LoginDTO loginDTO,
                        HttpServletRequest request,
                        Model model) {
        System.out.println("Login reçu : " + loginDTO.getLogin());
        System.out.println("Password reçu : " + loginDTO.getPassword());

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

