package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        if (allUsers.size() == 0) {
            User newUser = new User();
            newUser.setUsername("admin");
            newUser.setPassword(passwordEncoder.encode("admin"));
            newUser.setPin(passwordEncoder.encode("1234"));
            newUser.setRoles("ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER");
            newUser.setPasswordNeedsChange(true);
            userRepository.save(newUser);
        }
    }

    @GetMapping("/lockout")
    public String lockout() {
        return "lockscreen";
    }

    @PostMapping("/lockout")
    protected String lockoutPin(Model model, @RequestParam("pw") String currentPin) {
        Optional<User> user = getCurrentUser();
        if (user == null || user.isEmpty()) {
            return "redirect:/login";
        }
        if (!passwordEncoder.matches(currentPin, (String) user.get().getPin())) {
            model.addAttribute("error", "Wrong pin code.");
            return "lockscreen";
        }
        return "redirect:/order/";
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