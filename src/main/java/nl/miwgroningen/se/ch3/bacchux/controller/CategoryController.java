package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/catalog")
@Controller
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    protected String showCategoryForm(Model model) {
        model.addAttribute("allCategories", categoryRepository.findAll());
        Category category = new Category();
        model.addAttribute("category", category);
        return "catalogOverview";
    }

    @PostMapping("/add")
    protected String saveOrUpdateCategory( Model model,
                                       @ModelAttribute("category") Category category,
                                       BindingResult result) {
        if (result.hasErrors()) {
            return "catalogOverview";
        } else {
            try {
                categoryRepository.save(category);
            } catch (DataIntegrityViolationException exception) {
                model.addAttribute("allCategories", categoryRepository.findAll());
                model.addAttribute("error", "This category already/exists");
                return "catalogOverview";
            }
        }
        return "redirect:/catalog";
    }

    @GetMapping("update/{categoryId}")
    protected String UpdateCategoryForm(Model model,
                                    @PathVariable("categoryId") final Integer categoryId) {
        model.addAttribute("allCategories", categoryRepository.findAll());
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            model.addAttribute(category.get());
        } else {
            model.addAttribute(new Category());
        }
        return "catalogOverview";
    }
}
