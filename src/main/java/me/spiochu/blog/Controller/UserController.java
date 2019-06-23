package me.spiochu.blog.Controller;

import me.spiochu.blog.Services.UserService;
import me.spiochu.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/adduser")
    public String adduser(Model model, HttpSession session) {
        User user = getUserFromSession(session);
        model.addAttribute("user", user);
        if (user.getId().equals(-1L)) {
            model.addAttribute("newuser", new User());
            model.addAttribute("content", "singup");
            model.addAttribute("header", "headersingup");

            return "index";
        }
        return "redirect:/";
    }

    @PostMapping("/adduser")
    public String addUser(@ModelAttribute("newuser") @Valid User newuser, BindingResult result,
                          Model model, HttpSession session) {

        if (result.hasErrors() ||
                !newuser.getPassword().equals(newuser.getPassword_confirm()) ||
                userService.isUserExistByEmail(newuser.getEmail())) {
            if (userService.isUserExistByEmail(newuser.getEmail())) {
                result.rejectValue("email", null, "There is already an account registered with that email");
            }
            System.out.println(result.getFieldErrors());
            User user = getUserFromSession(session);
            model.addAttribute("user", user);
            model.addAttribute("content", "singup");
            model.addAttribute("header", "headersingup");
            if (!newuser.getPassword().equals(newuser.getPassword_confirm())) {
                model.addAttribute("password_error", "Password not mach");
            }

            return "index";
        }

        userService.addUser(newuser);
        User user = getUserFromSession(session);
        model.addAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/activate/{token}")
    public String tokenActivation(Model model, @PathVariable String token) {
        User user = userService.activateUserByToken(token);
        model.addAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/";
        }
        return "login";
    }

    private User getUserFromSession(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setId(-1L);
            user.setEmail("Niezalogowany");
        }
        return user;
    }

}
