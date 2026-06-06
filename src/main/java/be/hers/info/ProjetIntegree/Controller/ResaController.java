package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.DAO.DAOAcademicSkill;
import be.hers.info.ProjetIntegree.DAO.DAOProfessionalSkill;
import be.hers.info.ProjetIntegree.DTO.DTOEstablishment;
import be.hers.info.ProjetIntegree.DTO.DTOInterpreterProfile;
import be.hers.info.ProjetIntegree.DTO.DTOPasswordChange;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.AcademicSkill;
import be.hers.info.ProjetIntegree.POJO.Coordinator;
import be.hers.info.ProjetIntegree.POJO.Interpreter;
import be.hers.info.ProjetIntegree.POJO.ProfessionalSkill;
import be.hers.info.ProjetIntegree.Services.EstablishementService;
import be.hers.info.ProjetIntegree.Services.InterpreterProfileService;
import be.hers.info.ProjetIntegree.Services.ReferrerService;
import be.hers.info.ProjetIntegree.Services.SkillService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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


    /** Function who redirect to the page accueil for the resa.
     *
     * @param session is the session of the user,
     * @param model is used to give attribute to thymeleaf
     * @return the page resa/accueil if the user is connected.
     * else return /login for connection.
     */
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


    /** Function who redirect to the page profil for the resa.
     * Put all the attribute in model for the profil information.
     * @param session is the session of the user,
     * @param model is used to give attribute to thymeleaf
     * @return the page interprete/profil if the user is connected.
     * else return /login for connection.
     */
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

    /** Controller for the pages named "Mon profil"
     * Handles the submission of the profile edit form.
     * Saves the modified personal data (lastName, firstName, phoneNumber, emailAddress, address, weeklyWorkHours) of the connected resa.
     * The login and password are NOT modified here.
     * Reads the resa directly from the session.
     * Redirects to login if no resa is found in session.
     *
     * @param profileDTO the profile form data submitted by the user
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/resa/profil" after saving, or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil")
    public String saveProfile(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Coordinator resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            profileService.saveProfile(resa, profileDTO);
            session.setAttribute("currentUser", resa);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/resa/profil";
    }

    /** Controller for the pages named "Mon profil"
     * Handles the submission of the password change modal.
     * Verifies that newPassword and confirmPassword match, then updates the password in the database.
     * The DB trigger will hash the new password automatically on UPDATE.
     * Reads the resa directly from the session.
     * Redirects to login if no resa is found in session.
     * Redirects back to the profile page with an error parameter if the passwords do not match.
     *
     * @param passwordDTO the password change form data submitted by the user
     * @param request     the current HTTP request used to access the session
     * @return a redirect to "/resa/profil" after the operation,
     *         with "?passwordError=true" appended if passwords do not match,
     *         or a redirect to "/login" if the session is invalid
     */

    @PostMapping("/profil/password")
    public String changePassword(@ModelAttribute("passwordDTO") DTOPasswordChange passwordDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Coordinator resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            boolean success = profileService.changePassword(resa, passwordDTO);
            if (!success) {
                return "redirect:/resa/profil?passwordError=true";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/resa/profil";
    }

    /**
     * Controller for the pages named "Mon profil" part Professional Skill.
     * Adds a professional skill to the connected resa.
     * Checks first if the resa already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * Loads the skill from the database after insertion to update the resa's session list.
     * The profileDTO reconstructed by Spring via @ModelAttribute does not carry its skill lists
     * — only numProfessionalSkillSelected is used from it.
     * Reads the resa directly from the session.
     * Redirects to login if no resa is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numProfessionalSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/resa/profil" after adding,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addProfessionalSkill")
    public String addProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                       HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Coordinator resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();

            boolean alreadyOwned = resa.getProfessionalSkillsList() != null &&
                    resa.getProfessionalSkillsList().stream()
                            .anyMatch(s -> s.getNumProfessionalSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addProfessionalSkill(resa.getNumInterpreter(), numSkill);
                if (res) {
                    DAOProfessionalSkill dao = new DAOProfessionalSkill();
                    ProfessionalSkill p = dao.find(numSkill);
                    if (p != null) {
                        if (resa.getProfessionalSkillsList() == null) {
                            resa.setProfessionalSkillsList(new ArrayList<>());
                        }
                        resa.getProfessionalSkillsList().add(p);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", resa);
        return "redirect:/resa/profil?section=metiers";
    }

    /**
     * Controller for the pages named "Mon profil" part Professional Skill.
     * Deletes a professional skill from the connected resa.
     * Removes the skill from the resa's session list by matching its ID directly,
     * since the profileDTO reconstructed by Spring via @ModelAttribute does not carry its
     * skill lists — only numProfessionalSkillSelected is used from it.
     * Reads the resa directly from the session.
     * Redirects to login if no resa is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numProfessionalSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/resa/profil" after deleting,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteProfessionalSkill")
    public String deleteProfessionalSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                          HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Coordinator resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumProfessionalSkillSelected();
            boolean res = profileService.deleteProfessionalSkill(resa.getNumInterpreter(), numSkill);
            if (res && resa.getProfessionalSkillsList() != null) {
                resa.getProfessionalSkillsList()
                        .removeIf(s -> s.getNumProfessionalSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", resa);
        return "redirect:/resa/profil?section=metiers";
    }

    /**
     * Controller for the pages named "Mon profil" part Academic Skill.
     * Adds an academic skill to the connected resa.
     * Checks first if the resa already owns the skill to avoid a unique constraint
     * violation in the database on double form submission.
     * Loads the skill from the database after insertion to update the resa's session list.
     * The profileDTO reconstructed by Spring via @ModelAttribute does not carry its skill lists
     * — only numAcademicSkillSelected is used from it.
     * Reads the resa directly from the session.
     * Redirects to login if no resa is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numAcademicSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/resa/profil" after adding,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/addAcademicSkill")
    public String addAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                   HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Coordinator resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();

            boolean alreadyOwned = resa.getAcademicSkillsList() != null &&
                    resa.getAcademicSkillsList().stream()
                            .anyMatch(s -> s.getNumAcademicSkill() == numSkill);

            if (!alreadyOwned) {
                boolean res = profileService.addAcademicSkill(resa.getNumInterpreter(), numSkill);
                if (res) {
                    DAOAcademicSkill dao = new DAOAcademicSkill();
                    AcademicSkill a = dao.find(numSkill);
                    if (a != null) {
                        if (resa.getAcademicSkillsList() == null) {
                            resa.setAcademicSkillsList(new ArrayList<>());
                        }
                        resa.getAcademicSkillsList().add(a);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        session.setAttribute("currentUser", resa);
        return "redirect:/resa/profil?section=academics";
    }

    /**
     * Controller for the pages named "Mon profil" part Academic Skill.
     * Deletes an academic skill from the connected resa.
     * Removes the skill from the resa's session list by matching its ID directly,
     * since the profileDTO reconstructed by Spring via @ModelAttribute does not carry its
     * skill lists — only numAcademicSkillSelected is used from it.
     * Reads the resa directly from the session.
     * Redirects to login if no resa is found in session.
     *
     * @param profileDTO the profile form data submitted by the user,
     *                   only numAcademicSkillSelected is read from it
     * @param request    the current HTTP request used to access the session
     * @return a redirect to "/resa/profil" after deleting,
     *         or a redirect to "/login" if the session is invalid
     */
    @PostMapping("/profil/deleteAcademicSkill")
    public String deleteAcademicSkill(@ModelAttribute("profileDTO") DTOInterpreterProfile profileDTO,
                                      HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Interpreter resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }

        try {
            InterpreterProfileService profileService = new InterpreterProfileService();
            int numSkill = profileDTO.getNumAcademicSkillSelected();
            boolean res = profileService.deleteAcademicSkill(resa.getNumInterpreter(), numSkill);
            if (res && resa.getAcademicSkillsList() != null) {
                resa.getAcademicSkillsList()
                        .removeIf(s -> s.getNumAcademicSkill() == numSkill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        session.setAttribute("currentUser", resa);
        return "redirect:/resa/profil?section=academics";
    }

    

    /**
     * This function load the page "gestion".
     * It adds all the data needed for the page to display (Skills, Referents and Establishments).
     * If no user of Coordinator object was found in the session or if the Coordinator in the session
     * is not an admin, it redirects the user to the '/login' page
     *
     * @param model used by Spring to add all the data in the page
     * @param session the current HTTP session
     * @return the page displayed for the Coordinator admin user
     */
    @GetMapping("/gestion")
    public String gestion(Model model, HttpSession session) {
        Coordinator resa = getCoordinatorFromSession(session);
        if (resa == null) {
            return "redirect:/login";
        }
        String userName = resa.getLastName().toUpperCase() + " " + resa.getFirstName();
        model.addAttribute("userName", userName);
        model.addAttribute("userRole", ROLE);
        model.addAttribute("isAdmin", resa.isAdmin());
        try {
            model.addAttribute("referentList", new ReferrerService().getAllReferrer());
            model.addAttribute("etablissementList", new EstablishementService().getAllFullEstablishments());
            model.addAttribute("professionalSkillList", new SkillService().getAllProfessionalSkills());
            model.addAttribute("academicSkillList", new SkillService().getAllAcademicSkills());
        } catch(SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("DTOReferrer", new DTOReferrer());
        model.addAttribute("DTOEstablishment", new DTOEstablishment());

        return "coordinatrice/gestion";
    }

    /**
     * Creates a new AcademicSkill in the database with the designation submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param session the current HTTP session
     * @param designation the designation of the AcademicSkill to create
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/addAcademicSkill")
    public String academicSkillAdd(HttpSession session, @RequestParam("designation") String designation) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new SkillService().addAcademicSkill(designation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/resa/gestion?tab=competences";
    }

    /**
     * Deletes an AcademicSkill from the database using the id submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param session the current HTTP session
     * @param idAcademicSkill the id of the AcademicSkill to delete
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/deleteAcademicSkill")
    public String academicSkillDelete(HttpSession session, @RequestParam("idAcademicSkill") int idAcademicSkill) {
        Coordinator  coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new SkillService().deleteAcademicSkill(idAcademicSkill);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/resa/gestion?tab=competences";
    }

    /**
     * Creates a new ProfessionalSkill in the database with the designation submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param session the current HTTP session
     * @param designation the designation of the ProfessionalSkill to create
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/addProfessionalSkill")
    public String professionalSkillAdd(HttpSession session, @RequestParam("designation") String designation) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new SkillService().addProfessionalSkill(designation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/resa/gestion?tab=competences";
    }

    /**
     * Deletes a ProfessionalSkill from the database using the id submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param session the current HTTP session.
     * @param idProfessionalSkill the id of the ProfessionalSkill to delete
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/deleteProfessionalSkill")
    public String professionalSkillDelete(HttpSession session, @RequestParam("idProfessionalSkill") int idProfessionalSkill) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new SkillService().deleteProfessionalSkill(idProfessionalSkill);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/resa/gestion?tab=competences";
    }

    /**
     * This function create an establishment in DB using the data put in the form.
     *
     * @param dtoEstablishment is the DTOEstablishment the user is trying to add.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/createEstablishment")
    public String addEstablishment(@ModelAttribute("DTOEstablishmentAdd") DTOEstablishment dtoEstablishment) {
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.createEstablishment(dtoEstablishment);
        } catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }

    /**
     * This functions update the Establishment with the
     * Establishment the user put in the form
     *
     * @param dtoEstablishment is the DTOEstablishment to update.
     * @param model            is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/updateEstablishment")
    public String updateEstablishment(@ModelAttribute("DTOEstablishment") DTOEstablishment dtoEstablishment,
                                      Model model) {
        EstablishementService establishementService = new EstablishementService();
        try {
            establishementService.updateEstablishment(dtoEstablishment);
        } catch (SQLException e) {
            // renvoyé la page d'erreur.
        }
        return "redirect:/coordinatrice/etablissements";
    }


    // Temporaire
    @GetMapping("/planning")
    public String planning(Model model) {
        return "resa/planning";
    }
}