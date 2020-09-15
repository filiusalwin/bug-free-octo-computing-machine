package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/catalog")
@Controller
public class CatalogController {


    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    protected String showCatalogForm(Model model) {
        model.addAttribute("allCategories", categoryRepository.findAll());
        return "catalogOverview";
    }


}
