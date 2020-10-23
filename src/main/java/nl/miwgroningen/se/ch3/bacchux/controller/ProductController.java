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

    @GetMapping("/{categoryId}/add")
    protected String showProductForm(@PathVariable("categoryId") final Integer categoryId,
                                     Model model) {
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Product product = new Product();
            product.setCategory(category.get());
            model.addAttribute("product", product);
            model.addAttribute("allCategories", categoryRepository.findAll());
           return "productForm";
        }
        return "redirect:/catalog/product/" + categoryId;
    }

    @PostMapping("/{categoryId}/add")
    protected String saveOrUpdateProduct( Model model,
                                          @PathVariable("categoryId") final Integer categoryId,
                                          @ModelAttribute("product") Product product,
                                          BindingResult result,
                                          RedirectAttributes redirAttrs) {

        if (result.hasErrors()) {
            return "catalogOverview";
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
        return "redirect:/catalog/product/" + categoryId;
    }

    @GetMapping("/update/{productId}")
    protected String UpdateProductForm(Model model,
                                       @PathVariable("productId") final Integer productId) {
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Category category = product.get().getCategory();
            product.get().setCategory(category);
            model.addAttribute(product.get());
            model.addAttribute("categoryId", category.getCategoryId());
            return "productForm";
        } else {
            model.addAttribute(new Product());
        }
        return "redirect:/catalog/product/" + product.get().getCategory().getCategoryId();
    }

    @GetMapping("/delete/{productId}")
    protected String deleteProduct(@PathVariable("productId") final Integer productId) {
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            int categoryId = product.get().getCategory().getCategoryId();
            productRepository.deleteById(productId);
            return "redirect:/catalog/product/" + categoryId;
        }
        return "redirect:/catalog/product/{categoryId}/";
    }
}