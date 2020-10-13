package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.IbanValidation;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        if (user.getCreditPaymentBankAccountNumber() != null
                && !user.getCreditPaymentBankAccountNumber().isBlank()
                && !ibanValidation.validateIban(user.getCreditPaymentBankAccountNumber())) {
            redirAttrs.addFlashAttribute
                    ("error", "The bank account number is not correct. New user not added.");
            return "redirect:/user/";
        }
        checkPinPass(user, redirAttrs);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            model.addAttribute("allUsers", userRepository.findAll());
            model.addAttribute("error", "This username already exists!");
            return "userOverview";
        }
        redirAttrs.addFlashAttribute("success", "New user added.");
        return "redirect:/user/";
    }

    // Only check the pin and password if the new user is not a customer
    private void checkPinPass(User user, RedirectAttributes redirAttrs) {
        if (!user.getRoles().equals("ROLE_CUSTOMER")){
            if (user.getPin() != null && !user.getPin().isBlank() && user.getPin().length() == 4 ) {
                user.setPin(passwordEncoder.encode(user.getPin()));
            } else {
                redirAttrs.addFlashAttribute("error", "There was a problem with the Pincode. New user not added.");
            }
            if (user.getPassword() != null && !user.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                redirAttrs.addFlashAttribute("error", "There was a problem with the Password. New user not added.");
            }
        }
    }

    //to update a user without changing password
    @PostMapping ("/save")
    protected String updateUser( Model model,
                                       @ModelAttribute("user") User user,
                                       BindingResult result, RedirectAttributes redirAttrs) {
        model.addAttribute("allUsers", userRepository.findAll());
        if (result.hasErrors()) {
            return "userOverview";
        }
        IbanValidation ibanValidation = new IbanValidation();
        if (!user.getCreditPaymentBankAccountNumber().equals("")
                && !ibanValidation.validateIban(user.getCreditPaymentBankAccountNumber())) {
            redirAttrs.addFlashAttribute("error", "The bank account number is not correct. User not updated");
            return "redirect:/user/";
        }
        Optional<User> user1 = userRepository.findById(user.getUserId());
        if (user1.isEmpty()) {
            model.addAttribute("error", "User not found, cannot be updated.");
            return "userOverview";
        }
        user.setPassword(user1.get().getPassword());
        user.setBalance(user1.get().getBalance());
        user.setPin(user1.get().getPin());
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername.isPresent() && userByUsername.get().getUserId() != user.getUserId()) {
            model.addAttribute("error", "This username is taken by another user.");
            return "userOverview";
        }
        if (user1.get().getUserId().equals(getCurrentUser().get().getUserId())){
            user.setRoles(user1.get().getRoles());
            model.addAttribute("error", "You can not change your own roles.");
            return "userOverview";
        }

        redirAttrs.addFlashAttribute("success", "User updated.");
        userRepository.save(user);
        return "redirect:/user/";
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
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
