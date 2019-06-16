package me.spiochu.blog.Services;

import javafx.geometry.Pos;
import me.spiochu.blog.model.Comment;
import me.spiochu.blog.model.Post;
import me.spiochu.blog.model.User;
import me.spiochu.blog.repository.CommentRepository;
import me.spiochu.blog.repository.PostRepository;
import me.spiochu.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PostService {
    PostRepository postRepository;
    CommentRepository commentRepository;
    UserRepository userRepository;


    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Post getPost(Long id) {
//        if (postRepository.findById(id).isPresent()){
            return postRepository.getOne(id);
//        }
//        return new Post();
    }

    public List<Comment> getAllComentsForPost(Long id) {

        return commentRepository.findAllByPostId(id);
    }


    public void addComentToPost(Long post_id, Long user_id, Comment comment) {
        User user = userRepository.getOne(user_id);
        comment.setUser(user);
        Post post = postRepository.getOne(post_id);
        comment.setPost(post);
        commentRepository.saveAndFlush(comment);
    }

    public Post addPost(Post post, Long user_id) {
        post.setUser(userRepository.getOne(user_id));
        return postRepository.saveAndFlush(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public void updatePost(Long post_id, Post new_post) {
        Post post = postRepository.getOne(post_id);
        post.setTitle(new_post.getTitle());
        post.setContent(new_post.getContent());
        post.setCategory(new_post.getCategory());
        postRepository.saveAndFlush(post);
    }
}
