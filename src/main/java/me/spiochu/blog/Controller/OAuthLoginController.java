package me.spiochu.blog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuthLoginController {

    @GetMapping("/googlelogin")
    public String googleLogin() {
        return "redirect:/";
    }
}
