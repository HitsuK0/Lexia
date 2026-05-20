package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOAppointmentRequest;
import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.Appointment;
import be.hers.info.ProjetIntegree.POJO.BadStatusException;
import be.hers.info.ProjetIntegree.POJO.Beneficiary;
import be.hers.info.ProjetIntegree.Services.AppointmentService;
import be.hers.info.ProjetIntegree.Services.BeneficiaryProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
@Controller
@RequestMapping("/beneficiaire")
public class BeneficiaryController {

    /**
     * Retrieves the connected beneficiary from the session.
     * Returns null if no user is connected or if the connected user is not a Beneficiary.
     * This helper avoids relying on @ModelAttribute which may inject an empty POJO
     * instead of null when no session attribute exists.
     *
     * @param session the current HTTP session
     * @return the connected Beneficiary, or null if not found
     */
    private Beneficiary getBeneficiaryFromSession(HttpSession session) {
        if (session == null) return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Beneficiary) {
            return (Beneficiary) user;
        }
        return null;
    }

    /** Controller for the pages named "Mon planning"
     * Displays the weekly calendar page for the connected beneficiary.
     * Reads the beneficiary directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "beneficiaire/planning", or a redirect to "/login"
     */
    @GetMapping("/planning")
    public String planning(HttpSession session, Model model) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }
        model.addAttribute("activeTab", "planning");
        return "beneficiaire/planning";
    }

    /** Controller for the page "Mes demandes".
     * Displays the list of appointment requests for the connected beneficiary,
     * optionally filtered by status passed as a parameter.
     * Reads the beneficiary directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param status  the status to filter the requests on ("accepte", "refuse", "en attente"),
     *                or null/empty to return all requests
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return the view "beneficiaire/demandes", or a redirect to "/login" if the session is invalid
     */
    @GetMapping("/demandes")
    public String demandes(@RequestParam(required = false) String status,
                           HttpSession session, Model model) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if (beneficiary == null) {
            return "redirect:/login";
        }

        List<Appointment> appointmentList = new ArrayList<Appointment>();
        try {
            AppointmentService appointmentService = new AppointmentService();
            appointmentList = appointmentService.findRequestsForBeneficiary(beneficiary, status);
        } catch (SQLException | BadStatusException e) {
            e.printStackTrace();
        }

        model.addAttribute("requests", appointmentList);
        model.addAttribute("status", status);
        model.addAttribute("activeTab", "demandes");
        return "beneficiaire/demandes";
    }

    /** Controller for the pages named "Mon profil"
     * Displays the profile page for the connected beneficiary.
     * Reads the beneficiary directly from the session to avoid Spring injecting an empty POJO when no user is connected.
     * Builds a {@link DTOBeneficiaryProfile} from the connected beneficiary and adds it to the model so the Thymeleaf form can bind its fields.
     * Also adds an empty {@link DTOPasswordChange} for the password change modal.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param session the current HTTP session
     * @param model   the Spring UI model
     * @return the view "beneficiaire/profil", or a redirect to "/login"
     */
    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
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

    /** Controller for the pages named "Mon profil"
     * Handles the submission of the profile edit form.
     * Saves the modified personal data (lastName, firstName, phoneNumber, emailAddress, address) of the connected beneficiary.
     * The login and password are NOT modified here.
     * Reads the beneficiary directly from the session.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/beneficiaire/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOBeneficiaryProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
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

        return "redirect:/beneficiaire/profil";
    }

    /** Controller for the pages named "Mon profil"
     * Handles the submission of the password change modal.
     * Verifies that newPassword and confirmPassword match, then updates the password in the database.
     * The DB trigger will hash the new password automatically on UPDATE.
     * Reads the beneficiary directly from the session.
     * Redirects to login if no beneficiary is found in session.
     * Redirects back to the profile page with an error parameter if the passwords do not match.
     *
     * @param passwordDTO the password change form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/beneficiaire/profil" after the operation,
     *         with "?passwordError=true" appended if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
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

    /** Controller for the "Demander un RDV" button.
     * Reads the beneficiary directly from the session.
     * Redirects back to the page from which the form was submitted (Referer).
     *
     * @param dtoAppointmentRequest the appointment request form data submitted by the user
     * @param session the current HTTP session
     * @param header the URL of the page from which the form was submitted
     * @return a redirect to the originating page, or to "/login" if no beneficiary in session and by default
     */
    @PostMapping("/demandes/submit")
    public String submitAppointmentRequest(@ModelAttribute DTOAppointmentRequest dtoAppointmentRequest, HttpSession session,
                                           @RequestHeader(value = "Referer", required = false) String header) {
        Beneficiary currentBeneficiary = getBeneficiaryFromSession(session);

        if(currentBeneficiary == null) {
            return "redirect:/login";
        }

        try {
            AppointmentService appointmentService = new AppointmentService();
            appointmentService.createAppointmentRequest(dtoAppointmentRequest, currentBeneficiary);
        } catch (SQLException | BadStatusException e) {
            e.printStackTrace();
        }

        if(header != null) {
            return "redirect:" + header;
        }

        return "redirect:/login";
    }

    /** Controller for the trash icon button on the "Mes demandes" page.
     * Deletes the appointment request identified by numAppointment.
     * Reads the beneficiary directly from the session.
     * Redirects to login if no beneficiary is found in session.
     *
     * @param numAppointment the id of the appointment request to delete
     * @param session the current HTTP session
     * @return a redirect to "/beneficiaire/demandes" after the deletion attempt,
     *         a redirect to "/login" if the session is invalid
     */
    @PostMapping("/demandes/delete")
    public String deleteRequest(@RequestParam int numAppointment, HttpSession session) {
        Beneficiary beneficiary = getBeneficiaryFromSession(session);
        if(beneficiary == null) {
            return "redirect:/login";
        }

        try {
            AppointmentService appointmentService = new AppointmentService();
            appointmentService.deleteAppointmentRequest(numAppointment);
        } catch (SQLException | BadStatusException e) {
            e.printStackTrace();
        }
        return "redirect:/beneficiaire/demandes";
    }
}