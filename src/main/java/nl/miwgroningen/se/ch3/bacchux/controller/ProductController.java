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

@RequestMapping("/catalog/product")
@Controller
public class ProductController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CurrentSession currentSession;

    @GetMapping("/{categoryId}")
    protected String showProducts(@PathVariable("categoryId") final Integer categoryId,
                                  Model model){
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        model.addAttribute("allCategories", categoryRepository.findAll());
        model.addAttribute("allProducts", productRepository.findAll());
        model.addAttribute("product", new Product());
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());

            return "catalogOverview";
        }
        return "redirect:/catalog/";
    }

    @PostMapping("/{categoryId}/add")
    protected String saveOrUpdateProduct( Model model,
                                          @PathVariable("categoryId") final Integer categoryId,
                                          @ModelAttribute("product") Product product,
                                          BindingResult result,
                                          RedirectAttributes redirAttrs) {

        if (result.hasErrors()) {
            redirAttrs.addFlashAttribute("error1", "The information is not correct!");
            return "redirect:/catalog";
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            try {
                model.addAttribute("category", category.get());
                product.setCategory(category.get());
                productRepository.save(product);
            } catch (DataIntegrityViolationException exception) {
                redirAttrs.addFlashAttribute("error1", "This product already exists!");
                return "redirect:/catalog/product/" + categoryId;
            }
        }
        redirAttrs.addFlashAttribute("success1", "The product " + product.getName() + " is saved.");
        return "redirect:/catalog/product/" + categoryId;
    }

    @GetMapping("/delete/{productId}")
    protected String deleteProduct(@PathVariable("productId") final Integer productId,
                                   RedirectAttributes redirAttrs) {
        int categoryId = 0;
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            categoryId = product.get().getCategory().getCategoryId();
            productRepository.deleteById(productId);
            redirAttrs.addFlashAttribute("success1", "The product " + product.get().getName() + " is deleted.");

        }
        return "redirect:/catalog/product/" + categoryId;
    }
}