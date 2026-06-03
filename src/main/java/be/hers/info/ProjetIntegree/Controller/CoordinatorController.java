package be.hers.info.ProjetIntegree.Controller;

/**
 * @authors Willinger Chloé, Leroy Rodriguez Aïnhoa, Vanderheyden Quentin, Vatafu Jean
 * @reviewer Halet Louis
 */

import be.hers.info.ProjetIntegree.DTO.DTOEstablishment;
import be.hers.info.ProjetIntegree.DTO.DTOReferrer;
import be.hers.info.ProjetIntegree.POJO.*;
import be.hers.info.ProjetIntegree.Services.EstablishementService;

import be.hers.info.ProjetIntegree.Services.ReferrerService;
import be.hers.info.ProjetIntegree.Services.SkillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coordinatrice")
public class CoordinatorController {

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

    // Temporaire
    @GetMapping("/profil")
    public String profil(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "interprete/profil";
    }

    // Temporaire
    @GetMapping("/plannings")
    public String plannings(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/plannings";
    }

    // Temporaire
    @GetMapping("/validations")
    public String validations(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/validations";
    }

    /**
     * This function load the page "etablissement".
     * It also add all the data needed for the display (all the establishment registered in DB)
     * The page enable to add and modify an Establishment and to attribute a referrer for an Establishment.
     *
     * @param model is param used by Spring to add all the data in the page.
     * @return the page displayed for the users.
     */
    @GetMapping("/etablissements")
    public String etablissements(Model model, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null)
            return "redirect:login";
        String userName = coordinator.getLastName().toUpperCase() + " " + coordinator.getFirstName();
        model.addAttribute("userName", userName);
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        EstablishementService establishmentService = new EstablishementService();
        List<DTOEstablishment> listEstablishment = null;
        try {
            listEstablishment = establishmentService.getEtablissements();
        } catch (SQLException e) {
            // renvoyé sur la page d'erreur.
        }
        model.addAttribute("listEstablishment", listEstablishment);
        // listEstablishment is used for the display in the table.
        model.addAttribute("DTOEstablishmentAdd", new DTOEstablishment());
        // DTOEstablishmentAdd is used when the user try to add an Establishment
        model.addAttribute("DTOEstablishmentEdit", new DTOEstablishment());
        // DTOEstablishment is used when we try to modify an
        ReferrerService referrerService = new ReferrerService();
        List<Referrer> allListReferrer = null;
        try {
            allListReferrer = referrerService.getAllReferrer();
        } catch (SQLException e) {
            //renvoyé sur la page d'erreur.
        }
        model.addAttribute("allListReferrer", allListReferrer);
        model.addAttribute("listReferrerSelected", new ArrayList<Integer>());
        return "coordinatrice/etablissements";
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

        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            model.addAttribute("referentList", new ReferrerService().getAllReferrer());
            model.addAttribute("etablissementList", new EstablishementService().getAllFullEstablishments());
            model.addAttribute("professionalSkillList", new SkillService().getAllProfessionalSkills());
            model.addAttribute("academicSkillList", new SkillService().getAllAcademicSkills());
        } catch(SQLException e) {
            e.printStackTrace();
        }

        model.addAttribute("DTOReferrer", new DTOReferrer());
        model.addAttribute("DTOEstablishmentAdd", new DTOReferrer());

        return "coordinatrice/gestion";
    }

    /** Creates a new Referrer in the database using the data submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param dtoReferrer the DTOReferrer containing the data of the Referrer to create
     * @param session the current HTTP session
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/addReferrer")
    public String attributeReferrer(
                            @ModelAttribute("DTOReferrer") DTOReferrer dtoReferrer, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new ReferrerService().createReferrer(dtoReferrer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/coordinatrice/gestion";
    }

    /** Updates an existing Referrer in the database with the data submitted from the form.
     * If no user of Coordinator type is found in the session or if the Coordinator is not an admin,
     * the user is redirected to the '/login' page
     *
     * @param dtoReferrer the DTOReferrer containing the updated data of the Referrer
     * @param session the current HTTP session
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/updateReferrer")
    public String referrerUpdate(DTOReferrer dtoReferrer, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new ReferrerService().updateReferrer(dtoReferrer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/gestion";
    }

    /** Deletes a Referrer from the database using the id contained in the DTOReferrer submitted
     * from the form. If no user of Coordinator type is found in the session or if the Coordinator
     * is not an admin, the user is redirected to the '/login' page
     *
     * @param dtoReferrer the DTOReferrer containing the id of the Referrer to delete
     * @param session the current HTTP session
     * @return a redirection to the "/coordinatrice/gestion" page
     */
    @PostMapping("/etablissements/deleteReferrer")
    public String referrerDelete(DTOReferrer dtoReferrer, HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null || !coordinator.isAdmin()) {
            return "redirect:/login";
        }

        try {
            new ReferrerService().deleteReferrer(dtoReferrer);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/coordinatrice/gestion";
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

        return "redirect:/coordinatrice/gestion";
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

        return "redirect:/coordinatrice/gestion";
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

        return "redirect:/coordinatrice/gestion";
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

        return "redirect:/coordinatrice/gestion";
    }

    /**
     * This function create an establishment in DB using the data put in the form.
     *
     * @param dtoEstablishment is the DTOEstablishment the user is trying to add.
     * @param model            is param used by Spring to add all the data in the page.
     * @return the page "etablissements" where it comes from.
     */
    @PostMapping("/etablissements/createEstablishment")
    public String addEstablishment(@ModelAttribute("DTOEstablishmentAdd") DTOEstablishment dtoEstablishment,
                                   Model model) {
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
    @PostMapping("etablissements/updateEstablishment")
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
    @GetMapping("/utilisateurs")
    public String utilisateurs(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/utilisateurs";
    }

    // Temporaire
    @GetMapping("/utilisateurs/{id}")
    public String utilisateurDetail(@PathVariable String id, Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("isAdmin", true);

        switch (id) {
            case "1" -> model.addAttribute("userRole", "RESA");
            case "2" -> model.addAttribute("userRole", "INTERPRETER");
            case "3" -> model.addAttribute("userRole", "BENEFICIARY");
            case "4" -> model.addAttribute("userRole", "COORDINATOR");
            default -> model.addAttribute("userRole", "INTERPRETER");
        }

        return "coordinatrice/utilisateur-detail";
    }

    @PostMapping("utilisateurs/addUser")
    public String addUser(HttpSession session){
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null)
            return "redirect:/login";

        //En pause en attendant les modifs de Chloé
    }

    @GetMapping("utilisateurs/updateUser")
    public String updateUser(HttpSession session){
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null)
            return "redirect:/login";

        //En pause en attendant les modifs de Chloé
    }

    @GetMapping("utilisateurs/deleteUser")
    public String deleteUser(HttpSession session, @PathVariable String login){
        Coordinator coordinator = getCoordinatorFromSession(session);
        if (coordinator == null)
            return "redirect:/login";

        String messageToFrondEnd = deleteUser(login);

        return "utilisateurs";
    }

    // Temporaire
    @GetMapping("/horaire/base")
    public String horaireBase(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/horaire-base";
    }

    // Temporaire
    @GetMapping("/gestion/competences")
    public String gestionCompetences(Model model) {
        model.addAttribute("userName", "NOM Prenom");
        model.addAttribute("userRole", "COORDINATOR");
        model.addAttribute("isAdmin", true);
        return "coordinatrice/gestion-competences";
    }

    /**
     * If the coordinator exists, the user will be redirected to the home page.
     * Otherwise, if it is null, the user will be redirected to the login page.
     *  @param session session the current HTTP session
     *  @return The HTML path to the home page if a coordinator is logged in, else redirects to /login.
     */
    @GetMapping("/accueil")
    public String accueil(HttpSession session) {
        Coordinator coordinator = getCoordinatorFromSession(session);
        if(coordinator == null) {
            return "redirect:/login";
        }
        return "coordinatrice/accueil";
    }



}