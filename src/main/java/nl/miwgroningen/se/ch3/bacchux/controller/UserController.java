package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.IbanValidation;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("user", user);
        return "userOverview";
    }

    @GetMapping("/update/{userId}")
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

    @GetMapping("/update/username/{username}")
    protected String UpdateUserFormByUsername(Model model,
                                    @PathVariable("username") final String username) {
        model.addAttribute("allUsers", userRepository.findAll());
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            model.addAttribute(user.get());
        } else {
            model.addAttribute(new User());
        }
        return "userOverview";
    }

    // to add/save a new user
    @PostMapping ("/add")
    protected String saveNewUser( Model model,
                                       @ModelAttribute("user") User user,
                                       BindingResult result, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
            return "userOverview";
        }
        IbanValidation ibanValidation = new IbanValidation();
        if (!ibanValidation.validateIban(user.getCreditPaymentBankAccountNumber())
                && !user.getCreditPaymentBankAccountNumber().isEmpty()) {
            redirAttrs.addFlashAttribute
                    ("error", "The bank account number is not correct. New user not added.");
            return "redirect:/user/";
        }
        redirAttrs.addFlashAttribute("success", "New user added.");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPin(passwordEncoder.encode(user.getPin()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            model.addAttribute("allUsers", userRepository.findAll());
            model.addAttribute("error", "This username already exists!");
            return "userOverview";
        }
        return "redirect:/user/";
    }

    //to update a user without changing password
    @PostMapping ("/save")
    protected String updateUser( Model model,
                                       @ModelAttribute("user") User user,
                                       BindingResult result, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
            return "userOverview";
        }
        IbanValidation ibanValidation = new IbanValidation();
        if (!ibanValidation.validateIban(user.getCreditPaymentBankAccountNumber())) {
            redirAttrs.addFlashAttribute
                    ("error", "The bank account number is not correct. User not updated");

            model.addAttribute("allUsers", userRepository.findAll());
            return "redirect:/user/";
        }
        redirAttrs.addFlashAttribute("success", "User updated.");
        Optional<User> user1 = userRepository.findById(user.getUserId());
        user.setPassword(user1.get().getPassword());
        user.setBalance(user1.get().getBalance());
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (!userByUsername.isEmpty() && userByUsername.get().getUserId() != user.getUserId()) {
            model.addAttribute("error", "This username is taken by another user.");
            model.addAttribute("allUsers", userRepository.findAll());
            return "userOverview";
        }
        userRepository.save(user);
        return "redirect:/user/";
    }

    @GetMapping("/delete/{userId}")
    protected String deleteUser(@PathVariable("userId") final Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
        }
        return "redirect:/user/";
    }
}
