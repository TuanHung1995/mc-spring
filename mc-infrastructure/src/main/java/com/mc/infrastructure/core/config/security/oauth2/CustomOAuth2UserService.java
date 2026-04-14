package com.mc.infrastructure.core.config.security.oauth2;

import com.mc.domain.iam.model.User;
import com.mc.domain.iam.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthenticationService authenticationService;

    public CustomOAuth2UserService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name"); // Với Github có thể là "login"
        // Github avatar thường là "avatar_url"
        String avatarUrl = oAuth2User.getAttribute("avatar_url");
        if (avatarUrl == null) {
            avatarUrl = oAuth2User.getAttribute("picture");
        }

        log.info("OAuth2 login detected: email={}, name={}", email, name);

        if (email == null) {
            // Github có thể không trả về email nếu user để private, cần logic gọi thêm API Github nếu cần
            // Ở đây tạm thời log warning
            log.warn("Email not found in OAuth2 provider response");
        }

        User user = authenticationService.processOAuthPostLogin(email, name, avatarUrl);

        // Trả về oAuth2User gốc (hoặc có thể bọc lại nếu muốn custom attributes)
        return oAuth2User;
    }
}
