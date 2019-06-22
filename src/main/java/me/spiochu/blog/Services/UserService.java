package me.spiochu.blog.Services;

import me.spiochu.blog.model.User;
import me.spiochu.blog.repository.RoleRepository;
import me.spiochu.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void addUser(User newUser) {
        newUser.getRoles().add(roleRepository.getOne(1L));
        userRepository.save(newUser);

    }

    public boolean isUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
