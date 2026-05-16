package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.Services.BeneficiaryProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
@Controller
@RequestMapping("/beneficiaire")
public class BeneficiaryController {

    // Mon Planning

    /**
     * Displays the weekly calendar page for the connected beneficiary.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param beneficiary the connected beneficiary injected from the model (set by NavbarController)
     * @param model       the Spring UI model
     * @return the view "beneficiaire/planning", or a redirect to "/login"
     */
    @GetMapping("/planning")
    public String planning(@ModelAttribute("BeneficiaryConnected") Beneficiary beneficiary, Model model) {
        if (beneficiary == null) {
            return "redirect:/login";
        }
        model.addAttribute("activeTab", "planning");
        return "beneficiaire/planning";
    }

    // Mes Demandes

    /**
     * Displays the list of appointment requests for the connected beneficiary.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param beneficiary the connected beneficiary injected from the model (set by NavbarController)
     * @param model       the Spring UI model
     * @return the view "beneficiaire/demandes", or a redirect to "/login"
     */
    @GetMapping("/demandes")
    public String demandes(@ModelAttribute("BeneficiaryConnected") Beneficiary beneficiary, Model model) {
        if (beneficiary == null) {
            return "redirect:/login";
        }
        model.addAttribute("activeTab", "demandes");
        return "beneficiaire/demandes";
    }

    // Mon Profil

    /**
     * Displays the profile page for the connected beneficiary.
     * Builds a {@link DTOBeneficiaryProfile} from the connected beneficiary and adds it to the model so the Thymeleaf form can bind its fields.
     * Also adds an empty {@link DTOPasswordChange} for the password modal.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param beneficiary the connected beneficiary injected from the model (set by NavbarController)
     * @param model       the Spring UI model
     * @return the view "beneficiaire/profil", or a redirect to "/login"
     */
    @GetMapping("/profil")
    public String profil(@ModelAttribute("BeneficiaryConnected") Beneficiary beneficiary, Model model) {
        if (beneficiary == null) {
            return "redirect:/login";
        }

        BeneficiaryProfileService profileService = new BeneficiaryProfileService();
        DTOBeneficiaryProfile profileDTO = profileService.buildProfileDTO(beneficiary);

        model.addAttribute("profileDTO", profileDTO);
        model.addAttribute("passwordDTO", new DTOPasswordChange());
        model.addAttribute("activeTab", "profil");

        return "beneficiaire/profil";
    }

    /**
     * Handles the submission of the profile edit form.
     * Saves the modified personal data (lastName, firstName, phoneNumber, emailAddress, address) of the connected beneficiary.
     * The login and password are NOT modified here.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param profileDTO  the profile form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/beneficiaire/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOBeneficiaryProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Beneficiary beneficiary = (Beneficiary) session.getAttribute("currentUser");
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            BeneficiaryProfileService profileService = new BeneficiaryProfileService();
            profileService.saveProfile(beneficiary, profileDTO);
            session.setAttribute("currentUser", beneficiary);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/beneficiaire/planning";
    }

    /**
     * Handles the submission of the password change modal.
     * Verifies that newPassword and confirmPassword match, then updates the password in the database.
     * The DB trigger will hash the new password automatically on UPDATE.
     * Redirects to login if no beneficiary is found in session.
     * Redirects back to the profile page with an error parameter if the passwords do not match.
     *
     * @param passwordDTO the password change form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/beneficiaire/profil" after the operation, with "?passwordError=true" appended if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Beneficiary beneficiary = (Beneficiary) session.getAttribute("currentUser");
        if (beneficiary == null) {
            return "redirect:/login";
        }

        try {
            BeneficiaryProfileService profileService = new BeneficiaryProfileService();
            boolean success = profileService.changePassword(beneficiary, passwordDTO);

            if (!success) {
                return "redirect:/beneficiaire/profil?passwordError=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/beneficiaire/profil";
    }
}