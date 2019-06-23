package me.spiochu.blog.configuration;

import me.spiochu.blog.handlers.MyAuthorityLoginSuccessHandler;
import me.spiochu.blog.handlers.MyLogoutSuccessHandler;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableOAuth2Sso
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
                .antMatchers("/addPost*").hasAnyRole("USER")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin().loginPage("/login").successHandler(myAuthorityLoginSuccessHandler).usernameParameter("email").passwordParameter("password")
                .and()
                .logout().logoutSuccessHandler(myLogoutSuccessHandler).permitAll()
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
