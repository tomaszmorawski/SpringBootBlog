package me.spiochu.blog.configuration;

import me.spiochu.blog.handlers.MyAuthorityLoginSuccessHandler;
import me.spiochu.blog.handlers.MyLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    UserDetailsService userDetailsService;
    MyAuthorityLoginSuccessHandler myAuthorityLoginSuccessHandler;
    MyLogoutSuccessHandler myLogoutSuccessHandler;

    public SpringSecurityConfiguration(UserDetailsService userDetailsService,
                                       MyAuthorityLoginSuccessHandler myAuthorityLoginSuccessHandler,
                                       MyLogoutSuccessHandler myLogoutSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.myAuthorityLoginSuccessHandler = myAuthorityLoginSuccessHandler;
        this.myLogoutSuccessHandler = myLogoutSuccessHandler;
    }

    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/addPost*").hasAnyAuthority("USER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin().successHandler(myAuthorityLoginSuccessHandler)
                .and()
                .logout().logoutSuccessHandler(myLogoutSuccessHandler).permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
