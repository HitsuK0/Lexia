package be.hers.info.ProjetIntegree.Controller;

import be.hers.info.ProjetIntegree.POJO.Establishment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/templates/resa")
public class AccueilController {

    // page d'accueil
//    @GetMapping("/")
//    public String accueil() {
//        return "resa/page-accueil";
//    }

    @GetMapping("/")
    public String accueilModel(Model model) {
        model.addAttribute("userName", "Quentin Vanderheyden");
//        return "templates/resa/etablissement";
        return "resa/etablissement2";
    }

    @PostMapping("/templates/resa/Createetablisement")
    public String create(@ModelAttribute Establishment etab, Model model) {
        System.out.println(etab);
        return "redirect:/student/list";
    }
}
