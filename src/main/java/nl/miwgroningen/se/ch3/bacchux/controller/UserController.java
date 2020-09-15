package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    protected String showUserForm(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        // to check Radio button "Customer"
        User user = new User();
        user.setRoles("ROLE_CUSTOMER");
        model.addAttribute("user", user);
        user.setUserId(user.getUserId());
        user.setCreditAllowed(false);
        return "userOverview";
    }

    @GetMapping("update/{userId}")
    protected String UpdateUserForm(Model model,
                                    @PathVariable("userId") final Integer userId) {
        model.addAttribute("allUsers", userRepository.findAll());
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            model.addAttribute(user.get());
            model.addAttribute("user", user.get());
            model.addAttribute("userId", userId);
            model.addAttribute("roles", user.get().getRoles());
        } else {
            model.addAttribute(new User());
        }
        return "userOverview";
    }

    @PostMapping ("/add")
    protected String saveorUpdateUser( Model model,
                                       @ModelAttribute("user") User user,
                                       BindingResult result) {
            if (result.hasErrors()) {
                return "userOverview";
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                try {
                    userRepository.save(user);
                } catch (DataIntegrityViolationException exception) {
                    model.addAttribute("allUsers", userRepository.findAll());
                    model.addAttribute("error", "This username already/exists");
                    return "userOverview";
                }
            }
        return "redirect:/user";
    }

    @GetMapping("/delete/{userId}")
    protected String deleteUser(@PathVariable("userId") final Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
        }
        return "redirect:/user";
    }

}
