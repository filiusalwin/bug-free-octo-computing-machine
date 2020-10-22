package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.model.Product;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
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

    @GetMapping("/{categoryId}")
    protected String showProducts(@PathVariable("categoryId") final Integer categoryId,
                                  Model model){
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
        redirAttrs.addFlashAttribute("success1", "The product " + product.getName() + " is added.");
        return "redirect:/catalog/product/" + categoryId;
    }

    @PostMapping("/update/{productId}")
    protected String UpdateProductForm(@PathVariable("productId") final Integer productId,
                                       @RequestParam("categoryId") final Integer categoryId,
                                       @ModelAttribute("product") Product product,
                                       RedirectAttributes redirAttrs) {
        Optional<Product> optional = productRepository.findById(productId);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent()||!optional.isPresent()) {
            redirAttrs.addFlashAttribute("error1", "No product is added.");
            return "redirect:/catalog/";
        }
            optional.get().setCategory(optionalCategory.get());
            optional.get().setName(product.getName());
            optional.get().setPrice(product.getPrice());
            optional.get().setProductId(productId);
            productRepository.save(optional.get());
        redirAttrs.addFlashAttribute("success1", "The product " + product.getName() + " is added.");
        return "redirect:/catalog/product/" + optional.get().getCategory().getCategoryId();
    }

    @GetMapping("/delete/{productId}")
    protected String deleteProduct(@PathVariable("productId") final Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            int categoryId = product.get().getCategory().getCategoryId();
            productRepository.deleteById(productId);
            return "redirect:/catalog/product/" + categoryId;
        }
        return "redirect:/catalog/product/{categoryId}/";
    }
}