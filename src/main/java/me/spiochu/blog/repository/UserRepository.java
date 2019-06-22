package me.spiochu.blog.repository;

import me.spiochu.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByEmail(String email);

    User findFirstById(Long userId);

    User findFirstByRegistrationToken(String token);

}
