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

import java.util.Optional;

@Controller
public class ProfileController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    protected String changeProfile() {
        return "profileOverview";
    }

    @GetMapping("/profile/password")
    protected String changePassword() {
        return "changePassword";
    }

    @PostMapping("/profile/password")
    protected String doChangePassword(Model model, @RequestParam("currentPassword") String currentPassword,
                                      @RequestParam("newPassword") String newPassword) {
        Optional<User> user = getCurrentUser();
        if (user == null || user.isEmpty()) {
            return "redirect:/login";
        }
        if (!passwordEncoder.matches(currentPassword, user.get().getPassword())) {
            model.addAttribute("error", "Wrong password.");
            return "changePassword";
        }
        if (passwordEncoder.matches(newPassword, user.get().getPassword())) {
            model.addAttribute("error", "New and old password may not be the same!");
            return "changePassword";
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        user.get().setPasswordNeedsChange(false);
        userRepository.save(user.get());
        model.addAttribute("success", "Password changed successfully.");
        return "changePassword";
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }

    @GetMapping("/profile/pin")
    protected String changePin() {
        return "changePin";
    }

}
