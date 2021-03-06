package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import nl.miwgroningen.se.ch3.bacchux.service.CurrentSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    CurrentSession currentSession;

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
        currentSession.setLockscreenEnabled(false);
        checkForFirstUser();
        return "login";
    }

    public void checkForFirstUser() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.size() != 0) {
            return;
        }
        User newUser = new User();
        newUser.setUsername("admin");
        newUser.setName("admin");
        newUser.setPassword(passwordEncoder.encode("admin"));
        newUser.setPin(passwordEncoder.encode("1234"));
        newUser.setRoles("ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER");
        newUser.setPasswordNeedsChange(true);
        newUser.setPrepaidAllowed(true);
        newUser.setCreditAllowed(true);
        try {
            File image = new File("src/main/resources/static/images/defaultPicture.png");
            FileInputStream imageInFile = new FileInputStream(image);
            byte[] imageInBytes = imageInFile.readAllBytes();
            newUser.setPicture(imageInBytes);
        } catch (IOException e) {
            System.out.println("Could not store this profile picture. New user not added.");
        }
        userRepository.save(newUser);
    }

    @GetMapping("/403")
    public String error403() {
        if (currentSession.isLockscreenEnabled()) {
            return "lockscreen";
        }
        return "/403";
    }

    @GetMapping("/lockout")
    public String lockout(Model model) {
        Optional<User> user = getCurrentUser();
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("userName", user.get().getName());
        currentSession.setLockscreenEnabled(true);
        return "lockscreen";
    }

    @PostMapping("/lockout")
    protected String lockoutPin(@RequestParam("pin") String currentPin) {
        Optional<User> user = getCurrentUser();
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        if (passwordEncoder.matches(currentPin, user.get().getPin())) {
            currentSession.setLockscreenEnabled(false);
            return "redirect:" + currentSession.getPreviousUrl();
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