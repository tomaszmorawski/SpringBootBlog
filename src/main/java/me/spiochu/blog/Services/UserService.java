package me.spiochu.blog.Services;

import me.spiochu.blog.model.User;
import me.spiochu.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }
}
