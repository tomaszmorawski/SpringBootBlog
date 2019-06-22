package me.spiochu.blog.Services;

import me.spiochu.blog.model.Role;
import me.spiochu.blog.model.User;
import me.spiochu.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if (user != null){
            return org.springframework.security.core.userdetails.User.withUsername(s)
                    .disabled(!user.isActivity())
                    .password(user.getPassword())
/*
                    .roles(user.getRoles().stream().map(Role::getRole_name).toArray(String[]::new))
*/
                    .authorities(user.getRoles().stream().map(Role::getRole_name).toArray(String[]::new))
                    .build();
        }else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
