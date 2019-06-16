package me.spiochu.blog.Controller;

import me.spiochu.blog.Services.PostService;
import me.spiochu.blog.model.CategoryEnum;
import me.spiochu.blog.model.Comment;
import me.spiochu.blog.model.Post;
import me.spiochu.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PostsController {

    PostService postService;

    @Autowired
    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession httpSession) {
        List<Post> posts = postService.getAll();
        User user = getUserFromSession(httpSession);
        model.addAttribute("user",user);
        model.addAttribute("posts",posts);
        return "posts";
    }



    @GetMapping("/addPost")
    public String addPost(Model model, HttpSession session) {
        model.addAttribute("user",getUserFromSession(session));

        List<CategoryEnum> categoryEnum = new ArrayList<>(Arrays.asList(CategoryEnum.values()));
        model.addAttribute("categoryList",categoryEnum);
        model.addAttribute("post", new Post());
        return "addpost";
    }

    @PostMapping("/addPost/{user_id}")
    public String addPost(Model model, @PathVariable Long user_id, Post post) {
        Post tmp = postService.addPost(post,user_id);
        return "redirect:/post/"+tmp.getId();
    }


    @GetMapping("/post/{id}")
    public String singlePost(@PathVariable Long id,Model model, HttpSession session) {
        model.addAttribute("user",getUserFromSession(session));

        Post post = postService.getPost(id);
        List<Comment> comments = postService.getAllComentsForPost(id);
        model.addAttribute("post",post);
        model.addAttribute("comment",new Comment());
        return "post";
    }

    @PostMapping("/addComment/{post_id}/{user_id}")
    public String addComment(@PathVariable Long post_id, @PathVariable Long user_id,Comment comment, Model model){
        postService.addComentToPost(post_id,user_id,comment);
        return "redirect:/post/"+post_id;
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, Model model) {
        Optional<User> user = Optional.of(getUserFromSession(session));
        model.addAttribute("user", user.get());
        if (user.get().getId() != null) {
            postService.deleteById(id, user.get().getId());
        }
        return "redirect:/";
    }

    @GetMapping("/edit/{post_id}")
    public String editPost(@PathVariable Long post_id, Model model, HttpSession session) {
        Optional<User> user = Optional.of(getUserFromSession(session));
        model.addAttribute("user", getUserFromSession(session));
        if (user.get().getId() != null) {
            if (postService.isOwner(post_id, user.get().getId())) {
                List<CategoryEnum> categoryEnum = new ArrayList<>(Arrays.asList(CategoryEnum.values()));
                model.addAttribute("categoryList", categoryEnum);
                Post post = postService.getPost(post_id);
                model.addAttribute("post", post);
                return "edit";
            }

        }
        return "redirect:/";
    }

    @PutMapping("/edit/{post_id}")
    public String editPost(@PathVariable Long post_id,Model model,@ModelAttribute Post new_post){
        postService.updatePost(post_id,new_post);
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
