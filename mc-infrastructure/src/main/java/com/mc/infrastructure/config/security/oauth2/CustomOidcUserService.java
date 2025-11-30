package com.mc.infrastructure.config.security.oauth2;

import com.mc.domain.model.entity.User;
import com.mc.domain.service.AuthDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOidcUserService extends OidcUserService {

    private final AuthDomainService authDomainService;

    public CustomOidcUserService(AuthDomainService authDomainService) {
        this.authDomainService = authDomainService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");
        String avatarUrl = oidcUser.getAttribute("picture");

        log.info("Google OAuth login detected: email={}, name={}", email, name);

        // Xử lý logic nghiệp vụ (Lưu/Update DB)
        authDomainService.processOAuthPostLogin(email, name, avatarUrl);

        // Quan trọng: Trả về OidcUser với 'email' là nameAttributeKey.
        // Điều này đảm bảo authentication.getName() trả về email thay vì Google ID (sub),
        // giúp JwtTokenProvider sinh token đúng format email.
        return new DefaultOidcUser(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }
}
