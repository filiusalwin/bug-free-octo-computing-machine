package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/order")
    protected String showCatalog(Model model){
        model.addAttribute("allProducts", productRepository.findAll());
        return "order";
    }



}
