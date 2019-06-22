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
    public String addUser(@ModelAttribute @Valid User newUser, BindingResult result, Model model, HttpSession session, Authentication auth) {
        model.addAttribute("auth", auth);
        if (result.hasErrors() || !newUser.getPassword().equals(newUser.getPassword_confirm())) {
            User user = getUserFromSession(session);
            model.addAttribute("user", user);
            model.addAttribute("newuser", newUser);
            model.addAttribute("content", "singup");
            model.addAttribute("header", "headersingup");
            if (!newUser.getPassword().equals(newUser.getPassword_confirm())) {
                model.addAttribute("password_error", "Password not mach");
            }

            return "index";
        }

        userService.addUser(newUser);
        User user = getUserFromSession(session);
        model.addAttribute("user", user);
        return "redirect:/";
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
