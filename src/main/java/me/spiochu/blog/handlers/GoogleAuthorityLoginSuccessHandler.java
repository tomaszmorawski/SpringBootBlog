package me.spiochu.blog.handlers;


import me.spiochu.blog.helpers.OAuth2EmailToUserHelper;
import me.spiochu.blog.model.User;
import me.spiochu.blog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class GoogleAuthorityLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;


    public GoogleAuthorityLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        System.out.println("----------------------------------------------------------------------------------");
        OAuth2EmailToUserHelper oAuth2EmailToUserHelper = new OAuth2EmailToUserHelper();
        oAuth2EmailToUserHelper.setOAuthUser(authentication);
        System.out.println(oAuth2EmailToUserHelper.getEmail());
        System.out.println("----------------------------------------------------------------------------------");
        HttpSession httpSession = httpServletRequest.getSession();
        User user = userRepository.findByEmail(oAuth2EmailToUserHelper.getEmail());
        httpSession.setAttribute("user", user);
        httpServletResponse.sendRedirect("/");
    }


}
