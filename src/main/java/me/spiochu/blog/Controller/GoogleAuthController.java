package me.spiochu.blog.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleAuthController {

    @GetMapping("/google731c40bf8b8a10c1.html")
    public String googleauth() {
        return "google731c40bf8b8a10c1";
    }
}
