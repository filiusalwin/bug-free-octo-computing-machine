package nl.miwgroningen.se.ch3.bacchux.controller;

import nl.miwgroningen.se.ch3.bacchux.model.User;
import nl.miwgroningen.se.ch3.bacchux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequestMapping("/profile")
@Controller
public class ProfileController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    protected String changeProfile() {
        return "profileOverview";
    }

    @GetMapping("/password")
    protected String changePassword() {
        return "changePassword";
    }

    @PostMapping("/password")
    protected String doChangePassword(Model model, @RequestParam("currentPassword") String currentPassword,
                                      @RequestParam("newPassword") String newPassword,
                                      RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/profile";
    }

    @GetMapping("/passwordreset/{userId}")
    protected String loadResetPassword(Model model, @PathVariable("userId") final Integer userId, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findById(userId);
        if (user == null || user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong. You are redirect to the userpage.");
            return "redirect:/user";
        }
        model.addAttribute(user.get());
        return "resetPassword";
    }

    @PostMapping("/passwordreset/reset/{userId}")
    protected String doResetPassword(Model model, @PathVariable("userId") final Integer userId,
                                      @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong. You are redirect to the userpage.");
            return "redirect:/user";
        }
        if (user.get().getPassword() == newPassword) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong. You are redirect to the userpage.");
            return "redirect:/resetPassword";
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        user.get().setPasswordNeedsChange(true);
        userRepository.save(user.get());
        model.addAttribute(user.get());
        redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/user";
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return null;
        }
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }

    @GetMapping("/pin")
    protected String changePin() {
        return "changePin";
    }

    @PostMapping("/pin")
    protected String doChangePin(Model model,
                                 @RequestParam("newPin") String newPin,
                                 RedirectAttributes redirectAttributes) {
        Optional<User> user = getCurrentUser();
        if (user == null || user.isEmpty()) {
            return "redirect:/login";
        }
        user.get().setPin(passwordEncoder.encode(newPin));
        userRepository.save(user.get());
        redirectAttributes.addFlashAttribute("success", "Pin code changed successfully.");
        return "redirect:/profile";
    }
}