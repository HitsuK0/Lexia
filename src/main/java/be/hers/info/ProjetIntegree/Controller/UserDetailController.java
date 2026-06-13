package be.hers.info.ProjetIntegree.Controller;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */

import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.BeneficiaryProfileService;
import be.hers.info.ProjetIntegree.Services.InterpreterProfileService;
import be.hers.info.ProjetIntegree.Services.SkillService;
import be.hers.info.ProjetIntegree.Services.UserDetailService;
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
            UserDetailService userDetailService = new UserDetailService();
            Interpreter interpreter = userDetailService.findInterpreterById(id);
            if (interpreter == null)
                return "redirect:/coordinatrice/utilisateurs";

            String userRole = userDetailService.resolveInterpreterRole(id);

            List<ProfessionalSkill> ownedProfSkills = userDetailService.getProfessionalSkillsOfInterpreter(id);
            List<AcademicSkill> ownedAcadSkills = userDetailService.getAcademicSkillsOfInterpreter(id);

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
            UserDetailService userDetailService = new UserDetailService();
            Beneficiary beneficiary = userDetailService.findBeneficiaryById(id);
            if (beneficiary == null)
                return "redirect:/coordinatrice/utilisateurs";

            List<Interpreter> interpreterList = userDetailService.findAllInterpreters();

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
     * Assigns (or changes) the referent Interpreter of a Beneficiary.
     * Loads the Beneficiary by its id, loads the Interpreter selected by the coordinator,
     * sets it as the Beneficiary's referent.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id the numBeneficiary of the beneficiary whose referent interpreter is assigned
     * @param idInterpreter the numInterpreter of the interpreter selected as referent
     * @param session the current HTTP session
     * @return a redirect to the beneficiary detail page after saving
     *         a redirect to "/coordinatrice/utilisateurs" if the beneficiary does not exist
     *         a redirect to "/login" if the session is invalid
     */
    @PostMapping("/beneficiary/{id}/referenceInterpreter")
    public String beneficiaryReferenceInterpreter(@PathVariable int id,
                                                  @RequestParam("interpreterId") int idInterpreter,
                                                  HttpSession session) {
        if(getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            Beneficiary beneficiary = new UserDetailService().findBeneficiaryById(id);
            if (beneficiary == null) return "redirect:/coordinatrice/utilisateurs";

            beneficiary.setInterpreter(new UserDetailService().findInterpreterById(idInterpreter));
            new UserDetailService().updateBeneficiary(beneficiary);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id;
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
            UserDetailService userDetailService = new UserDetailService();
            Interpreter interpreter = userDetailService.findInterpreterById(id);
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
    public String saveBeneficiary(@PathVariable int id, @ModelAttribute("profileDTO") DTOBeneficiaryProfile profileDTO,
                                  HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            UserDetailService userDetailService = new UserDetailService();
            Beneficiary beneficiary = userDetailService.findBeneficiaryById(id);
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

            UserDetailService userDetailService = new UserDetailService();
            Interpreter interpreter = userDetailService.findInterpreterById(id);
            if (interpreter != null) {
                interpreter.setPassword(passwordDTO.getNewPassword());
                userDetailService.updateInterpreterPassword(interpreter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /** Adds a ProfessionalSkill to an Interpreter, Resa or Coordinator.
     * Loads the interpreter to verify it exists, then checks the skills it already owns
     * to avoid inserting a duplicate
     * Redirects to login if no coordinator is found in session
     *
     * @param id the numInterpreter of the interpreter receiving the skill
     * @param idSkill the numProfessionalSkill of the skill to add
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     *         a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/addProfessionalSkill")
    public String addProfessionalSkill(@PathVariable int id,
                                       @RequestParam("skillId") int idSkill,
                                       HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            UserDetailService userDetailService = new UserDetailService();

            Interpreter interpreter = userDetailService.findInterpreterById(id);
            if (interpreter == null) return "redirect:/coordinatrice/utilisateurs";

            List<ProfessionalSkill> ownedSkills = userDetailService.getProfessionalSkillsOfInterpreter(id);
            boolean alreadyOwned = ownedSkills.stream()
                    .anyMatch(s -> s.getNumProfessionalSkill() == idSkill);

            if (!alreadyOwned) {
                new InterpreterProfileService().addProfessionalSkill(interpreter.getNumInterpreter(), idSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Removes a ProfessionalSkill from an Interpreter, Resa or Coordinator
     * Loads the interpreter to verify it exists, then deletes the link between the interpreter
     * and the skill in ProfessionalSkillInterpreter
     * Redirects to login if no coordinator is found in session
     *
     * @param id the numInterpreter of the interpreter losing the skill
     * @param idSkill the numProfessionalSkill of the skill to remove
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     *         a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/deleteProfessionalSkill")
    public String deleteProfessionalSkill(@PathVariable int id,
                                          @RequestParam("skillId") int idSkill,
                                          HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            Interpreter interpreter = new UserDetailService().findInterpreterById(id);
            if (interpreter == null) return "redirect:/coordinatrice/utilisateurs";

            new InterpreterProfileService().deleteProfessionalSkill(interpreter.getNumInterpreter(), idSkill);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Adds an AcademicSkill to an Interpreter, Resa or Coordinator
     * Loads the interpreter to verify it exists, then checks the skills it already owns
     * to avoid inserting a duplicate
     * Redirects to login if no coordinator is found in session
     *
     * @param id the numInterpreter of the interpreter receiving the skill
     * @param idSkill the numAcademicSkill of the skill to add
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     *         a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/addAcademicSkill")
    public String addAcademicSkill(@PathVariable int id,
                                   @RequestParam("skillId") int idSkill,
                                   HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            UserDetailService userDetailService = new UserDetailService();

            Interpreter interpreter = userDetailService.findInterpreterById(id);
            if (interpreter == null) return "redirect:/coordinatrice/utilisateurs";

            List<AcademicSkill> ownedSkills = userDetailService.getAcademicSkillsOfInterpreter(id);
            boolean alreadyOwned = ownedSkills.stream()
                    .anyMatch(s -> s.getNumAcademicSkill() == idSkill);

            if (!alreadyOwned) {
                new InterpreterProfileService().addAcademicSkill(interpreter.getNumInterpreter(), idSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Removes an AcademicSkill from an Interpreter, Resa or Coordinator
     * Loads the interpreter to verify it exists, then deletes the link between the interpreter
     * and the skill in AcademicSkillInterpreter
     * Redirects to login if no coordinator is found in session
     *
     * @param id the numInterpreter of the interpreter losing the skill
     * @param idSkill the numAcademicSkill of the skill to remove
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     *         a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/deleteAcademicSkill")
    public String deleteAcademicSkill(@PathVariable int id,
                                      @RequestParam("skillId") int idSkill,
                                      HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            Interpreter interpreter = new UserDetailService().findInterpreterById(id);
            if (interpreter == null) return "redirect:/coordinatrice/utilisateurs";

            new InterpreterProfileService().deleteAcademicSkill(interpreter.getNumInterpreter(), idSkill);

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

            UserDetailService userDetailService = new UserDetailService();
            Beneficiary beneficiary = userDetailService.findBeneficiaryById(id);
            if (beneficiary != null) {
                beneficiary.setPassword(passwordDTO.getNewPassword());
                userDetailService.updateBeneficiaryPassword(beneficiary);
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
        if (getCoordinatorFromSession(session) == null)
            return "redirect:/login";
        newRole = newRole.toUpperCase();
        try {
            UserDetailService userDetailService = new UserDetailService();
            Interpreter interpreter = userDetailService.findInterpreterById(id);
            if (interpreter == null)
                return "redirect:/coordinatrice/utilisateurs";

            Coordinator existingCoordinator = userDetailService.findCoordinatorByInterpreter(id);
            String currentRole = existingCoordinator != null
                    ? (existingCoordinator.isAdmin() ? "COORDINATOR" : "RESA")
                    : "INTERPRETER";

            // Case 1 : RESA <-> COORDINATOR — just change isAdmin
            if ((currentRole.equals("RESA") && newRole.equals("COORDINATOR")) || (currentRole.equals("COORDINATOR") && newRole.equals("RESA"))) {
                existingCoordinator.setAdmin(newRole.equals("COORDINATOR"));
                userDetailService.updateCoordinator(existingCoordinator);
            }

            // Case 2 : INTERPRETER -> RESA or COORDINATOR
            else if (currentRole.equals("INTERPRETER") && (newRole.equals("RESA") || newRole.equals("COORDINATOR"))) {
                // Change login : I0006 -> C0006
                String newLogin = "C" + interpreter.getLogin().substring(1);
                interpreter.setLogin(newLogin);
                userDetailService.updateInterpreter(interpreter);

                // Insert in Coordinator
                Coordinator newCoordinator = new Coordinator();
                newCoordinator.setNumInterpreter(id);
                newCoordinator.setAdmin(newRole.equals("COORDINATOR"));
                userDetailService.createCoordinator(newCoordinator);
            }

            // Case 3 : RESA or COORDINATOR -> INTERPRETER
            else if ((currentRole.equals("RESA") || currentRole.equals("COORDINATOR")) && newRole.equals("INTERPRETER")) {
                userDetailService.deleteCoordinator(existingCoordinator);

                // Change login : C0006 -> I0006
                String newLogin = "I" + interpreter.getLogin().substring(1);
                interpreter.setLogin(newLogin);
                userDetailService.updateInterpreter(interpreter);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }
}