package me.spiochu.blog.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

public class OAuth2EmailToUserHelper {

    private Map<String, Object> userDetail = new LinkedHashMap<String, Object>();
    @JsonIgnore
    private Principal principal;

    public void setOAuthUser(Principal principal) {
        this.principal = principal;
        init();
    }

    private void init() {
        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            UsernamePasswordAuthenticationToken userAuthenticationToken = (UsernamePasswordAuthenticationToken) oAuth2Authentication.getUserAuthentication();
            if (userAuthenticationToken != null) {
                LinkedHashMap<String, Object> detailMap = (LinkedHashMap<String, Object>) userAuthenticationToken.getDetails();
                userAuthenticationToken.getCredentials();
                if (detailMap != null) {
                    for (Map.Entry<String, Object> mapEntry : detailMap.entrySet()) {
                        System.out.println("#### detail Key = " + mapEntry.getKey());
                        System.out.println("#### detail Value = " + mapEntry.getValue());
                        getUserDetail().put(mapEntry.getKey(), mapEntry.getValue());
                    }

                }

            }


        }
    }


    public String getEmail() {
        return (String) userDetail.get("email");
    }

    public Map<String, Object> getUserDetail() {
        return userDetail;
    }
}
