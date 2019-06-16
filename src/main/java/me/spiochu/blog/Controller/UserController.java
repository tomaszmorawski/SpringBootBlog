package me.spiochu.blog.Controller;

import me.spiochu.blog.Services.UserService;
import me.spiochu.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
            return "singup";
        }
        return "redirect:/";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User newUser, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "singup";
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
