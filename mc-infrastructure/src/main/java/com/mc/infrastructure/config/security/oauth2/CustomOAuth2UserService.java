package com.mc.infrastructure.config.security.oauth2;

import com.mc.domain.model.entity.User;
import com.mc.domain.service.AuthDomainService;
import com.mc.infrastructure.config.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthDomainService authDomainService;

    public CustomOAuth2UserService(AuthDomainService authDomainService) {
        this.authDomainService = authDomainService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Lấy thông tin từ Google
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("picture");


        log.info("email={}, name={}, avatarUrl={}", email, name, avatarUrl);
        // Gọi Domain Service để xử lý logic nghiệp vụ (Tạo hoặc Update)
        User user = authDomainService.processOAuthPostLogin(email, name, avatarUrl);

        // Trả về Principal cho Spring Security Context
        new CustomUserDetails(user, oAuth2User.getAttributes());

        return oAuth2User;
    }
}
