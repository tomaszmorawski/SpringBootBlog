package me.spiochu.blog.repository;


import me.spiochu.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findFirstByTitle(String title);
}
