package me.spiochu.blog.Services;


import me.spiochu.blog.model.User;
import me.spiochu.blog.repository.RoleRepository;
import me.spiochu.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AutoMailService mailService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, AutoMailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public void addUser(User newUser) {
        newUser.getRoles().add(roleRepository.getOne(1L));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRegistrationToken(encryptThisString(newUser.toString()));
        try {
            mailService.sendMailTo(newUser.getEmail(), "Registration",
                    "Click link below to activate you account !" +
                            "<br>" +
                            "<a href='https://thespiochu.herokuapp.com/activate/" +
                            newUser.getRegistrationToken() +
                            "'>Click IT</a>");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        userRepository.save(newUser);

    }

    public boolean isUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User activateUserByToken(String token) {
        User user = userRepository.findFirstByRegistrationToken(token);
        if (user != null) {
            user.setPassword_confirm(user.getPassword());
            userRepository.saveAndFlush(activateUser(user.getId()));
            return user;
        }
        user = new User();
        user.setId(-1L);
        user.setEmail("Niezalogowany");
        return user;
    }

    private User activateUser(Long id) {
        User user = userRepository.findFirstById(id);
        user.setActivity(true);
        return user;
    }

    public static String encryptThisString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




}
