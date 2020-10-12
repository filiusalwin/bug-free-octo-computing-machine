package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping({"", "/"})
    protected String landingPage() {
        Optional<User> user = getCurrentUser();
        if (user == null || user.isEmpty()) {
            return "redirect:/login";
        }
        Boolean passwordNeedsChange = user.get().getPasswordNeedsChange();
        if (passwordNeedsChange != null && passwordNeedsChange) {
            return "redirect:/profile/password";
        }
        return "redirect:/order/";
    }

    @GetMapping("/login")
    public String login() {
        checkForFirstUser();
        return "login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }

    public void checkForFirstUser() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.size() != 0) {
            return;
        }

        byte[] defaultPictureInBytes = new byte[0];
        try {
            File image = new File("src/main/resources/static/images/defaultPicture.png");
            FileInputStream imageInFile = new FileInputStream(image);
            defaultPictureInBytes = imageInFile.readAllBytes();
        } catch (IOException e) {
            System.out.println("Exception while reading the Image " + e);
        }

        User newUser = new User();
        newUser.setUsername("admin");
        newUser.setName("admin");
        newUser.setPassword(passwordEncoder.encode("admin"));
        newUser.setPin(passwordEncoder.encode("1234"));
        newUser.setRoles("ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER");
        newUser.setPasswordNeedsChange(true);
        newUser.setPicture(defaultPictureInBytes);
        userRepository.save(newUser);
    }

    @GetMapping("/lockout")
    public String lockout() {
        Optional<User> user = getCurrentUser();
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        return "lockscreen";
    }

    @PostMapping("/lockout")
    protected String lockoutPin(@RequestParam("pin") String currentPin) {
        Optional<User> user = getCurrentUser();
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        if (passwordEncoder.matches(currentPin, (String) user.get().getPin())) {
            return "redirect:/order/";
        }
            return "redirect:/lockout?error" ;
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }

}