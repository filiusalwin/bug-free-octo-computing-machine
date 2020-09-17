package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.CategoryRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.ProductRepository;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/order")
@Controller
public class OrderController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("")
    protected String showCatalog(Model model) {
        model.addAttribute("allProducts", productRepository.findAll());
        model.addAttribute("allUsers", userRepository.findAll());
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("allCategories", categoryRepository.findAll());
        return "order";
    }

    @GetMapping("/new/prepaid")
    protected String addPrepaidCustomer(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        User user = new User();
        user.setRoles("ROLE_CUSTOMER");
        model.addAttribute("user", new User());
        return "order_new_prepaid_customer";
    }

    @GetMapping("/user/info/{username}")
    protected String showCustomer(Model model,
                                  @PathVariable("username") final String username) {
        loadPageWithUsername(model, username);
        return "order_new_prepaid_customer";
    }

    private void loadPageWithUsername(Model model, String username) {
        model.addAttribute("allUsers", userRepository.findAll());
        Optional<User> user = userRepository.findByUsername(username);
        model.addAttribute("allProducts", productRepository.findAll());
        if (user.isPresent()) {
            model.addAttribute(user.get());
        } else {
            model.addAttribute(new User());
        }
    }
}


