package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.Role;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Optional;



@RequestMapping("/user")
@Secured("ROLE_ADMIN")
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
        user.setRole(Role.CUSTOMER);
        model.addAttribute("user", user);
        return "userOverview";
    }

    @GetMapping("update/{userId}")
    protected String UpdateUserForm(Model model,
                                    @PathVariable("userId") final Integer userId) {
        model.addAttribute("allUsers", userRepository.findAll());
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            model.addAttribute(user.get());
        } else {
            model.addAttribute(new User());
        }
        return "userOverview";
    }


    @RequestMapping(value="/add", method=RequestMethod.POST, params="action=save")
    protected String saveNewUser(  Model model,
                                        @ModelAttribute("user") User user,
                                        BindingResult result) {
        if (result.hasErrors()) {
            return "userOverview";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "redirect:/user";
        }
    }

    @RequestMapping(value="/add", method=RequestMethod.POST, params="action=update")
    protected String UpdateUser(Model model,
                                BindingResult result,
                                @PathVariable("userId") final Integer userId) {
        if (result.hasErrors()) {
            return "userOverview";
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            model.addAttribute(user.get());
            user.get().setPassword(passwordEncoder.encode(user.get().getPassword()));
            try {
                userRepository.save(user.get());

            } catch (DataIntegrityViolationException exception) {
                model.addAttribute("allUsers", userRepository.findAll());
                model.addAttribute("error", "This username already/exists");
                System.out.println(exception);
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
