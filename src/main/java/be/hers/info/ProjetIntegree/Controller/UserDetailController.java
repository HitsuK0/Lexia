package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DTO.DTOBeneficiaryProfile;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Nicolas Jean-François
 * @reviewer Halet Louis, Wellinger Chloé
 */
@Controller
@RequestMapping("/coordinatrice/utilisateurs")
public class UserDetailController {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailController.class);
    private final EmailService emailService;

    public UserDetailController(EmailService emailService) {
        this.emailService = emailService;
    }

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
     * @param id      the numInterpreter of the user to display
     * @param session the current HTTP session
     * @param model   the Spring UI model
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
            InterpreterProfileService profileService = new InterpreterProfileService();

            model.addAttribute("user", interpreter);
            model.addAttribute("userRole", userRole);
            model.addAttribute("professionalSkillListUser", ownedProfSkills);
            model.addAttribute("academicSkillListUser", ownedAcadSkills);
            model.addAttribute("allProfessionalSkills", allProfSkills);
            model.addAttribute("allAcademicSkills", allAcadSkills);
            model.addAttribute("passwordDTO", new DTOPasswordChange());
            model.addAttribute("profileDTO", new DTOInterpreterProfile());
            model.addAttribute("profileDTO", profileService.buildProfileDTO(interpreter));

        } catch (SQLException e) {
            logger.error("Erreur lors du chargement de la fiche interprète {}", id, e);
        }

        return "coordinatrice/utilisateur-detail";
    }

    /**
     * Displays the detail page for a Beneficiary.
     * Loads the beneficiary's personal data and address.
     * Also loads all available interpreters so the coordinator can assign a referent.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id      the numBeneficiary of the user to display
     * @param session the current HTTP session
     * @param model   the Spring UI model
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
            BeneficiaryProfileService profileService = new BeneficiaryProfileService();

            model.addAttribute("user", beneficiary);
            model.addAttribute("userRole", "BENEFICIARY");
            model.addAttribute("interpreterList", interpreterList);
            model.addAttribute("passwordDTO", new DTOPasswordChange());
            model.addAttribute("profileDTO", profileService.buildProfileDTO(beneficiary));

        } catch (SQLException e) {
            logger.error("Erreur lors du chargement de la fiche bénéficiaire {}", id, e);
        }

        return "coordinatrice/utilisateur-detail";
    }

    /**
     * Assigns (or changes) the referent Interpreter of a Beneficiary.
     * Loads the Beneficiary by its id, loads the Interpreter selected by the coordinator,
     * sets it as the Beneficiary's referent.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id            the numBeneficiary of the beneficiary whose referent interpreter is assigned
     * @param idInterpreter the numInterpreter of the interpreter selected as referent
     * @param session       the current HTTP session
     * @return a redirect to the beneficiary detail page after saving
     * a redirect to "/coordinatrice/utilisateurs" if the beneficiary does not exist
     * a redirect to "/login" if the session is invalid
     */
    @PostMapping("/beneficiary/{id}/referenceInterpreter")
    public String beneficiaryReferenceInterpreter(@PathVariable int id,
                                                  @RequestParam("interpreterId") int idInterpreter,
                                                  HttpSession session) {
        if (getCoordinatorFromSession(session) == null) return "redirect:/login";

        try {
            Beneficiary beneficiary = new UserDetailService().findBeneficiaryById(id);
            if (beneficiary == null) return "redirect:/coordinatrice/utilisateurs";

            beneficiary.setInterpreter(new UserDetailService().findInterpreterById(idInterpreter));
            new UserDetailService().updateBeneficiary(beneficiary);

        } catch (SQLException e) {
            logger.error("Erreur lors de l'attribution du référent interprète {} au bénéficiaire {}", idInterpreter, id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id;
    }

    /**
     * Saves the profile changes for an Interpreter, Resa or Coordinator submitted by the coordinator.
     * Reuses InterpreterProfileService.saveProfile() to update personal data and address.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id         the numInterpreter of the user to update
     * @param profileDTO the profile form data submitted by the coordinator
     * @param session    the current HTTP session
     * @return a redirect to the interpreter detail page after saving,
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors de la sauvegarde du profil interprète {}", id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Saves the profile changes for a Beneficiary submitted by the coordinator.
     * Reuses BeneficiaryProfileService.saveProfile() to update personal data and address.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id         the numBeneficiary of the user to update
     * @param profileDTO the profile form data submitted by the coordinator
     * @param session    the current HTTP session
     * @return a redirect to the beneficiary detail page after saving,
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors de la sauvegarde du profil bénéficiaire {}", id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id;
    }

    /**
     * Resets the password of an Interpreter, Resa or Coordinator with a randomly generated password.
     * The new password is sent by email to the user, and stored as-is in the database — the DB trigger will hash it on UPDATE.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id      the numInterpreter of the user whose password is reset
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/interpreter/{id}/password")
    public String resetPasswordInterpreter(@PathVariable int id, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return "redirect:/login";

        try {
            UserDetailService userDetailService = new UserDetailService();
            Interpreter interpreter = userDetailService.findInterpreterById(id);
            if (interpreter != null) {
                String newPassword = generateRandomPassword();
                interpreter.setPassword(newPassword);
                userDetailService.updateInterpreterPassword(interpreter);

                emailService.sendPasswordResetEmail(
                        interpreter.getEmailAddress(),
                        interpreter.getFirstName(),
                        interpreter.getLastName(),
                        newPassword
                );
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la réinitialisation du mot de passe interprète {}", id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Adds a ProfessionalSkill to an Interpreter, Resa or Coordinator.
     * Loads the interpreter to verify it exists, then checks the skills it already owns
     * to avoid inserting a duplicate
     * Redirects to login if no coordinator is found in session
     *
     * @param id      the numInterpreter of the interpreter receiving the skill
     * @param idSkill the numProfessionalSkill of the skill to add
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     * a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors de l'ajout de la compétence métier {} à l'interprète {}", idSkill, id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Removes a ProfessionalSkill from an Interpreter, Resa or Coordinator
     * Loads the interpreter to verify it exists, then deletes the link between the interpreter
     * and the skill in ProfessionalSkillInterpreter
     * Redirects to login if no coordinator is found in session
     *
     * @param id      the numInterpreter of the interpreter losing the skill
     * @param idSkill the numProfessionalSkill of the skill to remove
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     * a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors de la suppression de la compétence métier {} de l'interprète {}", idSkill, id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Adds an AcademicSkill to an Interpreter, Resa or Coordinator
     * Loads the interpreter to verify it exists, then checks the skills it already owns
     * to avoid inserting a duplicate
     * Redirects to login if no coordinator is found in session
     *
     * @param id      the numInterpreter of the interpreter receiving the skill
     * @param idSkill the numAcademicSkill of the skill to add
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     * a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors de l'ajout de la compétence académique {} à l'interprète {}", idSkill, id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Removes an AcademicSkill from an Interpreter, Resa or Coordinator
     * Loads the interpreter to verify it exists, then deletes the link between the interpreter
     * and the skill in AcademicSkillInterpreter
     * Redirects to login if no coordinator is found in session
     *
     * @param id      the numInterpreter of the interpreter losing the skill
     * @param idSkill the numAcademicSkill of the skill to remove
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation
     * a redirect to "/coordinatrice/utilisateurs" if the interpreter does not exist
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors de la suppression de la compétence académique {} de l'interprète {}", idSkill, id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Resets the password of a Beneficiary with a randomly generated password.
     * The new password is sent by email to the beneficiary, and stored as-is in the database — the DB trigger will hash it on UPDATE.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id      the numBeneficiary of the user whose password is reset
     * @param session the current HTTP session
     * @return a redirect to the beneficiary detail page after the operation, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/beneficiary/{id}/password")
    public String resetPasswordBeneficiary(@PathVariable int id, HttpSession session) {
        if (getCoordinatorFromSession(session) == null)
            return "redirect:/login";

        try {
            UserDetailService userDetailService = new UserDetailService();
            Beneficiary beneficiary = userDetailService.findBeneficiaryById(id);
            if (beneficiary != null) {
                String newPassword = generateRandomPassword();
                beneficiary.setPassword(newPassword);
                userDetailService.updateBeneficiaryPassword(beneficiary);

                emailService.sendPasswordResetEmail(
                        beneficiary.getEmailAddress(),
                        beneficiary.getFirstName(),
                        beneficiary.getLastName(),
                        newPassword
                );
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la réinitialisation du mot de passe bénéficiaire {}", id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/beneficiary/" + id;
    }

    /**
     * Changes the role of an Interpreter, Resa or Coordinator.
     * Supported transitions :
     * - RESA <-> COORDINATOR : only updates isAdmin in the Coordinator table, login unchanged.
     * - INTERPRETER -> RESA/COORDINATOR : changes the login prefix from 'I' to 'C',
     * then inserts a new row in the Coordinator table with isAdmin set accordingly.
     * - RESA/COORDINATOR -> INTERPRETER : changes the login prefix from 'C' to 'I',
     * then deletes the row from the Coordinator table.
     * Redirects to login if no coordinator is found in session.
     *
     * @param id      the numInterpreter of the user whose role is changed
     * @param newRole the new role : "INTERPRETER", "RESA" or "COORDINATOR"
     * @param session the current HTTP session
     * @return a redirect to the interpreter detail page after the operation,
     * or a redirect to "/login" if the session is invalid
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
            logger.error("Erreur lors du changement de rôle vers {} pour l'interprète {}", newRole, id, e);
        }

        return "redirect:/coordinatrice/utilisateurs/interpreter/" + id;
    }

    /**
     * Generates a random password with at least 8 characters,
     * including an uppercase letter, a lowercase letter, a digit and a special character.
     *
     * @return the generated plaintext password
     */
    private String generateRandomPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%&*?";
        String all = upper + lower + digits + special;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));
        sb.append(special.charAt(random.nextInt(special.length())));
        for (int i = 4; i < 10; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }

        List<Character> chars = new ArrayList<>();
        for (char c : sb.toString().toCharArray()){
            chars.add(c);
        }
        Collections.shuffle(chars);

        StringBuilder result = new StringBuilder();
        for (char c : chars) result.append(c);
        return result.toString();
    }
}