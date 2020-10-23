package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.model.Product;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
import nl.miwgroningen.se.ch3.bacchux.service.CurrentSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@RequestMapping("/catalog")
@Controller
public class CategoryController {

    public static final int FIRST_IN_LIST = 0;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CurrentSession currentSession;

    @GetMapping("")
    protected String showCatalog(Model model) {
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        currentSession.setPreviousUrl("/catalog");
        List<Category> allCategories = categoryRepository.findAll();
        List<Product> allProducts = productRepository.findAll();
        allCategories.sort(Category::compareTo);
        allProducts.sort(Product::compareTo);
        model.addAttribute("allCategories", allCategories);
        model.addAttribute("allProducts", allProducts);
        model.addAttribute("product", new Product());
        if (allCategories.isEmpty()) {
            model.addAttribute("category", new Category());
        } else {
            model.addAttribute("category", allCategories.get(FIRST_IN_LIST));
        }
        return "catalogOverview";
    }


    @PostMapping("/add")
    protected String saveOrUpdateCategory( Model model,
                                       @ModelAttribute("category") Category category,
                                       BindingResult result, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
            return "catalogOverview";
        } else {
            try {
                categoryRepository.save(category);
            } catch (DataIntegrityViolationException exception) {
                model.addAttribute("allCategories", categoryRepository.findAll());
                redirAttrs.addFlashAttribute("error", "This category already exists!");
                return  "redirect:/catalog/";
            }
        }
        redirAttrs.addFlashAttribute("success", "The category " + category.getName() + " is saved.");
        return "redirect:/catalog/";
    }


    @GetMapping("/delete/{categoryId}")
    protected String deleteCategory(@PathVariable("categoryId") final Integer categoryId,
                                    RedirectAttributes redirAttrs) {
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            categoryRepository.deleteById(categoryId);
        }
        redirAttrs.addFlashAttribute("success", "The category " + category.get().getName() + " is deleted.");
        return "redirect:/catalog/";
    }

}
