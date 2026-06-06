package be.hers.info.ProjetIntegree.Controller;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */

import be.hers.info.ProjetIntegree.DAO.DAOCoordinator;
import be.hers.info.ProjetIntegree.DAO.DAOInterpreter;
import be.hers.info.ProjetIntegree.DAO.DAOBeneficiary;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.InterpreterProfileService;
import be.hers.info.ProjetIntegree.Services.BeneficiaryProfileService;
import be.hers.info.ProjetIntegree.Services.SkillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/coordinatrice/utilisateurs")
public class UserDetailController {

    /**
     * Retrieves the connected coordinator from the session.
     * Returns null if no user is connected or if the connected user is not a Coordinator.
     *
     * @param session the current HTTP session
     * @return the connected Coordinator, or null if not found
     */
    private Coordinator getCoordinatorFromSession(HttpSession session) {
        if (session == null) return null;
        Object user = session.getAttribute("currentUser");
        if (user instanceof Coordinator) {
            return (Coordinator) user;
        }
        return null;
    }

    /**
     * Determines the user role string from an Interpreter object.
     * Returns "COORDINATOR" if the interpreter is also a coordinator with isAdmin = true,
     * "RESA" if isAdmin = false, and "INTERPRETER" otherwise.
     *
     * @param interpreter the interpreter to check
     * @return the role string
     * @throws SQLException if a database error occurs
     */
    private String resolveInterpreterRole(Interpreter interpreter) throws SQLException {
        DAOCoordinator daoCoordinator = new DAOCoordinator();
        Coordinator coordinator = daoCoordinator.findByFKnumInterpreter(interpreter.getNumInterpreter());
        if (coordinator != null) {
            return coordinator.isAdmin() ? "COORDINATOR" : "RESA";
        }
        return "INTERPRETER";
    }

    /**
     * Displays the detail page for an Interpreter, Resa or Coordinator.
     * Loads the interpreter's personal data, address, professional and academic skills.
     * Also loads all available skills so the coordinator can add new ones.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numInterpreter of the user to display
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return the view "coordinatrice/utilisateur-detail", or a redirect to "/login"
     */
    @GetMapping("/interpreter/{id}")
    public String ficheInterpreter(@PathVariable int id, HttpSession session, Model model) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            Interpreter interpreter = daoInterpreter.find(id);
            if (interpreter == null) return "redirect:/coordinatrice/utilisateurs";

            String userRole = resolveInterpreterRole(interpreter);

            List<ProfessionalSkill> ownedProfSkills = daoInterpreter.getProfessionalSkill(id);
            List<AcademicSkill> ownedAcadSkills = daoInterpreter.getAcademicSkill(id);

            SkillService skillService = new SkillService();
            List<ProfessionalSkill> allProfSkills = skillService.getAllProfessionalSkills();
            List<AcademicSkill> allAcadSkills = skillService.getAllAcademicSkills();

            model.addAttribute("user", interpreter);
            model.addAttribute("userRole", userRole);
            model.addAttribute("professionalSkillListUser", ownedProfSkills);
            model.addAttribute("academicSkillListUser", ownedAcadSkills);
            model.addAttribute("allProfessionalSkills", allProfSkills);
            model.addAttribute("allAcademicSkills", allAcadSkills);
            model.addAttribute("passwordDTO", new DTOPasswordChange());
            model.addAttribute("profileDTO", new DTOInterpreterProfile());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "coordinatrice/utilisateur-detail";
    }

    /**
     * Displays the detail page for a Beneficiary.
     * Loads the beneficiary's personal data and address.
     * Also loads all available interpreters so the coordinator can assign a referent.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the user to display
     * @param session the current HTTP session
     * @param model the Spring UI model
     * @return the view "coordinatrice/utilisateur-detail", or a redirect to "/login"
     */
    @GetMapping("/beneficiary/{id}")
    public String ficheBeneficiary(@PathVariable int id, HttpSession session, Model model) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            Beneficiary beneficiary = daoBeneficiary.find(id);
            if (beneficiary == null) return "redirect:/coordinatrice/utilisateurs";

            DAOInterpreter daoInterpreter = new DAOInterpreter();
            List<Interpreter> interpreterList = daoInterpreter.findAll();

            model.addAttribute("user", beneficiary);
            model.addAttribute("userRole", "BENEFICIARY");
            model.addAttribute("interpreterList", interpreterList);
            model.addAttribute("passwordDTO", new DTOPasswordChange());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "coordinatrice/utilisateur-detail";
    }

    /**
     * Saves the profile changes for an Interpreter, Resa or Coordinator submitted by the coordinator.
     * Reuses InterpreterProfileService.saveProfile() to update personal data and address.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numInterpreter of the user to update
     * @param profileDTO the profile form data submitted by the coordinator
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after saving,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}")
    public String saveInterpreter(@PathVariable int id, @ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            Interpreter interpreter = daoInterpreter.find(id);
            if (interpreter != null) {
                InterpreterProfileService profileService = new InterpreterProfileService();
                profileService.saveProfile(interpreter, profileDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Saves the profile changes for a Beneficiary submitted by the coordinator.
     * Reuses BeneficiaryProfileService.saveProfile() to update personal data and address.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the user to update
     * @param profileDTO the profile form data submitted by the coordinator
     * @param session the current HTTP session
     * @return a redirect to the beneficiary detail page after saving,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/beneficiary/{id}")
    public String saveBeneficiary(@PathVariable int id, @ModelAttribute("profileDTO") be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile profileDTO,
                                  HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            Beneficiary beneficiary = daoBeneficiary.find(id);
            if (beneficiary != null) {
                BeneficiaryProfileService profileService = new BeneficiaryProfileService();
                profileService.saveProfile(beneficiary, profileDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id;
    }

    /**
     * Resets the password of an Interpreter, Resa or Coordinator.
     * The new password is stored as-is — the DB trigger will hash it on UPDATE.
     * Verifies that newPassword and confirmPassword match before applying the change.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numInterpreter of the user whose password is reset
     * @param passwordDTO the password reset form data submitted by the coordinator
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation,
     *         with "?passwordError=true" if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/password")
    public String resetPasswordInterpreter(@PathVariable int id, @ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            if (passwordDTO.getNewPassword() == null || !passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
                return "redirect:/coordinatrice/utilisateurs/interpreter/" + id + "?passwordError=true";
            }

            DAOInterpreter daoInterpreter = new DAOInterpreter();
            Interpreter interpreter = daoInterpreter.find(id);
            if (interpreter != null) {
                interpreter.setPassword(passwordDTO.getNewPassword());
                daoInterpreter.updatePassword(interpreter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Resets the password of a Beneficiary.
     * The new password is stored as-is — the DB trigger will hash it on UPDATE.
     * Verifies that newPassword and confirmPassword match before applying the change.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the user whose password is reset
     * @param passwordDTO the password reset form data submitted by the coordinator
     * @param session the current HTTP session
     * @return a redirect to the beneficiary detail page after the operation,
     *         with "?passwordError=true" if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/beneficiary/{id}/password")
    public String resetPasswordBeneficiary(@PathVariable int id, @ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            if (passwordDTO.getNewPassword() == null || !passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
                return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id + "?passwordError=true";
            }

            DAOBeneficiary daoBeneficiary = new DAOBeneficiary();
            Beneficiary beneficiary = daoBeneficiary.find(id);
            if (beneficiary != null) {
                beneficiary.setPassword(passwordDTO.getNewPassword());
                daoBeneficiary.updatePassword(beneficiary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id;
    }

    /**
     * Changes the role of an Interpreter, Resa or Coordinator.
     * Supported transitions :
     * - RESA <-> COORDINATOR : only updates isAdmin in the Coordinator table, login unchanged.
     * - INTERPRETER -> RESA/COORDINATOR : changes the login prefix from 'I' to 'C',
     *   then inserts a new row in the Coordinator table with isAdmin set accordingly.
     * - RESA/COORDINATOR -> INTERPRETER : changes the login prefix from 'C' to 'I',
     *   then deletes the row from the Coordinator table.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numInterpreter of the user whose role is changed
     * @param newRole the new role : "INTERPRETER", "RESA" or "COORDINATOR"
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/role")
    public String changeRole(@PathVariable int id, @RequestParam("newRole") String newRole, HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            DAOInterpreter daoInterpreter = new DAOInterpreter();
            DAOCoordinator daoCoordinator = new DAOCoordinator();
            Interpreter interpreter = daoInterpreter.find(id);
            if (interpreter == null) return "redirect:/coordinatrice/utilisateurs";

            Coordinator existingCoordinator = daoCoordinator.findByFKnumInterpreter(id);
            String currentRole = existingCoordinator != null
                    ? (existingCoordinator.isAdmin() ? "COORDINATOR" : "RESA")
                    : "INTERPRETER";

            // Case 1 : RESA <-> COORDINATOR — just change isAdmin
            if ((currentRole.equals("RESA") && newRole.equals("COORDINATOR")) || (currentRole.equals("COORDINATOR") && newRole.equals("RESA"))) {
                existingCoordinator.setAdmin(newRole.equals("COORDINATOR"));
                daoCoordinator.update(existingCoordinator);
            }

            // Case 2 : INTERPRETER -> RESA or COORDINATOR
            else if (currentRole.equals("INTERPRETER") && (newRole.equals("RESA") || newRole.equals("COORDINATOR"))) {
                // Change login : I0006 -> C0006
                String oldLogin = interpreter.getLogin();
                String newLogin = "C" + oldLogin.substring(1);
                interpreter.setLogin(newLogin);
                daoInterpreter.update(interpreter);

                // Insert in Coordinator
                Coordinator newCoordinator = new Coordinator();
                newCoordinator.setNumInterpreter(id);
                newCoordinator.setAdmin(newRole.equals("COORDINATOR"));
                daoCoordinator.create(newCoordinator);
            }

            // Case 3 : RESA or COORDINATOR -> INTERPRETER
            else if ((currentRole.equals("RESA") || currentRole.equals("COORDINATOR")) && newRole.equals("INTERPRETER")) {
                // delete to Coordinator
                daoCoordinator.delete(existingCoordinator);

                // Change login : C0006 -> I0006
                String oldLogin = interpreter.getLogin();
                String newLogin = "I" + oldLogin.substring(1);
                interpreter.setLogin(newLogin);
                daoInterpreter.update(interpreter);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }
}
