package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
    @Secured("ROLE_ADMIN")
    protected String showNewUserForm(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        model.addAttribute("user", new User());
        return "userOverview";
    }

    @PostMapping("/new")
    @Secured("ROLE_ADMIN")
    protected String saveOrUpdateUser(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "userOverview";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/user";
        }
    }

    @GetMapping("/delete/{userId}")
    @Secured("ROLE_ADMIN")
    protected String deleteUser(@PathVariable("userId") final Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
        }
        return "redirect:/user";
    }

}
