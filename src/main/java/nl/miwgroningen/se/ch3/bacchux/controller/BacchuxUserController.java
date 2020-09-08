package nl.miwgroningen.se.ch3.bacchux.controller;


import nl.miwgroningen.se.ch3.bacchux.model.BacchuxUser;
import nl.miwgroningen.se.ch3.bacchux.repository.BacchuxUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BacchuxUserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    BacchuxUserRepository bacchuxUserRepository;

    @GetMapping("/user/new")
    @Secured("ROLE_ADMIN")
    protected String showNewUserForm(Model model){
        model.addAttribute("user", new BacchuxUser());
        return "userForm";
    }

    @PostMapping("/user/new")
    @Secured("ROLE_ADMIN")
    protected String saveOrUpdateUser(@ModelAttribute("user") BacchuxUser user, BindingResult result){
        if(result.hasErrors()){
            return "userForm";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            bacchuxUserRepository.save(user);
            return "redirect:/";
        }
    }
}
