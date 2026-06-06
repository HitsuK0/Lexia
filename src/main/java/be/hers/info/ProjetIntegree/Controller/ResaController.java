package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;
import be.hers.info.ProjetIntegree.Services.InterpreterProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping("/resa")
public class ResaController {

    private final String ROLE = "COORDINATOR";

    /**
     * Retrieves the connected coordinator from the session.
     * Returns null if no user is connected or if the connected user is not a Coordinator.
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

    @GetMapping("/accueil")
    public String accueil(HttpSession session, Model model) {
        Coordinator resa = getCoordinatorFromSession(session);
        if(resa == null) {
            return "redirect:/login";
        }
        String userName = resa.getLastName().toUpperCase() + " " + resa.getFirstName();
        model.addAttribute("userName", userName);
        model.addAttribute("userRole", ROLE);
        model.addAttribute("isAdmin", resa.isAdmin());
        return "resa/accueil";
    }

    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        Coordinator resa = getCoordinatorFromSession(session);
        if(resa == null) {
            return "redirect:/login";
        }
        String userName = resa.getLastName().toUpperCase() + " " + resa.getFirstName();
        model.addAttribute("userName", userName);
        model.addAttribute("userRole", ROLE);
        model.addAttribute("isAdmin", resa.isAdmin());

        InterpreterProfileService profileService = new InterpreterProfileService();
        DTOInterpreterProfile profileDTO = profileService.buildProfileDTO(resa);

        model.addAttribute("profileDTO", profileDTO);
        model.addAttribute("passwordDTO", new DTOPasswordChange());
        model.addAttribute("activeTab", "profil");

        return "interprete/profil";

    }

    /** Controller for the page "Mon profil" of the connected resa.
     * Handles the submission of the profile edit form.
     * Saves the modified personal data.
     * The login and password are NOT modified here.
     * @param profileDTO the profile form data submitted by the user
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            profileService.saveProfile(coordinator, profileDTO);
            session.setAttribute("currentUser", coordinator);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/profil";
    }

    /** Controller for the page "Mon profil" of the connected resa.
     * Handles the submission of the password change modal.
     * @param passwordDTO the password change form data submitted by the user
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil" after the operation,
     *         with "?passwordError=true" appended if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean success = profileService.changePassword(coordinator, passwordDTO);
            if (!success) {
                return "redirect:/interprete/profil?passwordError=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/interprete/profil";
    }

    /** Controller for the page "Mon profil" of the connected resa.
     * Adds a professional skill to the connected coordinator.
     * Checks first if the coordinator already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * @param profileDTO the profile form data submitted by the user
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=metiers" after adding,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addProfessionalSkill")
    public String addProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                       HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();

            boolean alreadyOwned = coordinator.getProfessionalSkillsList() != null &&
                    coordinator.getProfessionalSkillsList().stream()
                            .anyMatch(s -> s.getNumProfessionalSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addProfessionalSkill(coordinator.getNumInterpreter(), numSkill);
                if (res) {
                    DAOProfessionalSkill dao = new DAOProfessionalSkill();
                    ProfessionalSkill p = dao.find(numSkill);
                    if (p != null) {
                        if (coordinator.getProfessionalSkillsList() == null) {
                            coordinator.setProfessionalSkillsList(new ArrayList<>());
                        }
                        coordinator.getProfessionalSkillsList().add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", coordinator);

        return "redirect:/interprete/profil?section=metiers";
    }

    /** Controller for the page "Mon profil" of the connected resa, section Professional Skill.
     * Deletes a professional skill from the connected coordinator.
     * Removes the skill from the coordinator's session list by matching its ID directly.
     * @param profileDTO the profile form data submitted by the user.
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=metiers" after deleting,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteProfessionalSkill")
    public String deleteProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                          HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();
            boolean res = profileService.deleteProfessionalSkill(coordinator.getNumInterpreter(), numSkill);
            if (res && coordinator.getProfessionalSkillsList() != null) {
                coordinator.getProfessionalSkillsList()
                        .removeIf(s -> s.getNumProfessionalSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", coordinator);
        return "redirect:/interprete/profil?section=metiers";
    }

    /**
     * Controller for the page "Mon profil" of the connected resa, section Academic Skill.
     * Adds an academic skill to the connected coordinator.
     * Checks first if the coordinator already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * @param profileDTO the profile form data submitted by the user.
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=academics" after adding,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addAcademicSkill")
    public String addAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                   HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();

            boolean alreadyOwned = coordinator.getAcademicSkillsList() != null &&
                    coordinator.getAcademicSkillsList().stream()
                            .anyMatch(s -> s.getNumAcademicSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addAcademicSkill(coordinator.getNumInterpreter(), numSkill);
                if (res) {
                    DAOAcademicSkill dao = new DAOAcademicSkill();
                    AcademicSkill a = dao.find(numSkill);
                    if (a != null) {
                        if (coordinator.getAcademicSkillsList() == null) {
                            coordinator.setAcademicSkillsList(new ArrayList<>());
                        }
                        coordinator.getAcademicSkillsList().add(a);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", coordinator);
        return "redirect:/interprete/profil?section=academics";
    }

    /**
     * Controller for the page "Mon profil" of the connected resa, section Academic Skill.
     * Deletes an academic skill from the connected coordinator.
     * @param profileDTO the profile form data submitted by the user.
     * @param session the current HTTP session
     * @return a redirect to "/interprete/profil?section=academics" after deleting,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteAcademicSkill")
    public String deleteAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                      HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();
            boolean res = profileService.deleteAcademicSkill(coordinator.getNumInterpreter(), numSkill);
            if (res && coordinator.getAcademicSkillsList() != null) {
                coordinator.getAcademicSkillsList()
                        .removeIf(s -> s.getNumAcademicSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", coordinator);
        return "redirect:/interprete/profil?section=academics";
    }


    
    // Temporaire — réutilise le même template que la coordinatrice
    @GetMapping("/gestion")
    public String etablissements(Model model) {
        return "coordinatrice/gestion";
    }

    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        return "resa/planning";
    }
}