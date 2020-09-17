package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.Category;
import nl.miwgroningen.se.ch3.bacchux.model.Product;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/product")
@Controller
public class ProductController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/{categoryId}")
    protected String showProducts(@PathVariable("categoryId") final Integer categoryId, Model model){
        Optional<Category> category = categoryRepository.findById(categoryId);
        Product product = new Product();
        model.addAttribute("product", product);
        if (category.isPresent()) {
            model.addAttribute("productsByCriterium", category.get().getProducts());
            model.addAttribute("categoryId", categoryId);
            return "catalogOverview";
        } else {
            return "redirect:/catalog/";
        }
    }
}
