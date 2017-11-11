package hu.buko.szoftarchrecipedb.controller;

import hu.buko.szoftarchrecipedb.model.user.UserDTO;
import hu.buko.szoftarchrecipedb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String initLoginPage() {
        return "login";
    }

    @GetMapping(value = "/signup")
    public String initSignupPage() {
        return "signup";
    }

    @PostMapping(value = "/signup")
    public String signup(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        logger.debug("signup post called");
        logger.debug("userDTO: " + userDTO);
        userService.addUser(userDTO.getUsername(), userDTO.getPassword());
        redirectAttributes.addFlashAttribute("message", "Sikeres regisztráció!");
        logger.debug("Successful sign up " + userDTO.getUsername());
        return "login";
    }
}
