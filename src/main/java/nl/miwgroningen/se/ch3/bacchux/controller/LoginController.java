package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void checkForFirstUser() {
        List<User> allUsers = userRepository.findAll();
        if (allUsers.size() == 0) {
            User newUser = new User();
            newUser.setUsername("admin");
            newUser.setPassword(passwordEncoder.encode("admin"));
            newUser.setRoles("ROLE_CUSTOMER,ROLE_BARTENDER,ROLE_BARMANAGER");
            newUser.setPasswordNeedsChange(true);
            userRepository.save(newUser);
        }
    }

    @GetMapping("/login")
    public String login(){
        checkForFirstUser();
        return "login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }

    @GetMapping("/")
    protected String landingPage() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return "redirect:/login";
        }
        String username = ((UserDetails)principal).getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        Boolean passwordNeedsChange = user.get().getPasswordNeedsChange();
        if (passwordNeedsChange != null && passwordNeedsChange) {
            return "redirect:/profile/password";
        }
        return "redirect:/order/";
    }
}
