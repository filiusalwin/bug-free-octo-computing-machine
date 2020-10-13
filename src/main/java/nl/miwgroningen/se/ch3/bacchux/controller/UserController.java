package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.IbanValidation;
import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
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
        // If the users have profile picture, then it will be converted to a base64 string so it can be displayed
        for (User user1: userRepository.findAll()) {
            model.addAttribute("picture", convertToBase64(user1));
        }
        return "userOverview";
    }

    public String convertToBase64(User user) {
        String imageInBase64 = "";
        try {
            // Check if there is an image uploaded, if there is not set the profile picture to the default image
            if (user.getPicture() == null){
                // Set a default image
                File image = new File("src/main/resources/static/images/defaultPicture.png");
                FileInputStream imageInFile = new FileInputStream(image);
                byte[] imageInBytes = imageInFile.readAllBytes();
                imageInBase64 += Base64.getEncoder().encodeToString(imageInBytes);
            }
            // if the user uploaded an image then convert it to Base64
            else {
                imageInBase64 += Base64.getEncoder().encodeToString(user.getPicture());
            }
        }
        catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return imageInBase64;
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
                                       @RequestParam("file") MultipartFile picture,
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
        checkPicture(user, redirAttrs, picture);

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

    private void checkPicture(User user, RedirectAttributes redirAttrs, MultipartFile picture) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));

        // Check if the file's name contains invalid characters
        if (fileName.contains("..")) {
            redirAttrs.addFlashAttribute("error","Sorry! Filename contains invalid path sequence " + fileName);

       // If there is no image uploaded, save default image.
        } else if (user.getPicture() == null) {
                try {
                    File image = new File("src/main/resources/static/images/defaultPicture.png");
                    FileInputStream imageInFile = new FileInputStream(image);
                    byte[] imageInBytes = imageInFile.readAllBytes();
                    user.setPicture(imageInBytes);
                } catch (IOException e) {
                    redirAttrs.addFlashAttribute("error", "Could not store this profile picture. New user not added.");
                }

        // if the user uploaded an image then use that image
        } else {
            try {
                user.setPicture(picture.getBytes());
            } catch (IOException e) {
                redirAttrs.addFlashAttribute("error", "Could not store this profile picture. New user not added.");
            }
        }
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

        if (user.getPicture() == null) {
            user.setPicture(user1.get().getPicture());
        }

        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername.isPresent() && !userByUsername.get().getUserId().equals(user.getUserId())) {
            model.addAttribute("error", "This username is taken by another user.");
            return "userOverview";
        }
        redirAttrs.addFlashAttribute("success", "User updated.");
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
