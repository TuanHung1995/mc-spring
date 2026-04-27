package com.mc.infrastructure.organization.security;

import com.mc.domain.iam.exception.UserNotFoundException;
import com.mc.domain.organization.port.OrgUserContextPort;
import com.mc.domain.organization.port.OrgUserView;
import com.mc.infrastructure.iam.security.userdetails.IamUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * OrgUserContextPortImpl — Infrastructure Adapter (Organization Context)
 *
 * <p><strong>WHY THIS ADAPTER EXISTS:</strong>
 * The Organization context needs a UUID-based user identity from the Spring Security
 * context. The existing {@code UserContextPort} returns a {@code Long} (legacy).
 * This adapter resolves the current user from {@code SecurityContextHolder} and maps
 * it to {@link OrgUserView} — the ACL read model for the Organization context.</p>
 *
 * <p>It reads from {@link IamUserDetails}, which wraps the IAM {@code User} domain model
 * and is set in the security context by {@code JwtAuthenticationFilter}.
 * This adapter lives in {@code mc-infrastructure} because it depends on Spring Security.</p>
 */
@Component
public class OrgUserContextPortImpl implements OrgUserContextPort {

    /**
     * Resolves the authenticated user from Spring Security's {@code SecurityContextHolder}
     * and returns it as an {@link OrgUserView}.
     *
     * <p><strong>FAIL-FAST:</strong> If no authenticated user is found, we throw immediately.
     * Any Organization endpoint that reaches this point without authentication is a
     * misconfigured security filter — we surface it loudly rather than returning null
     * (which would cause a NullPointerException deep in business logic).</p>
     *
     * @throws UserNotFoundException if no authenticated IAM user is in the security context.
     */
    @Override
    public OrgUserView getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof IamUserDetails details)) {
            throw new UserNotFoundException("No authenticated user found in security context");
        }

        return new OrgUserView(
                details.getUserId(),
                details.getEmail(),
                details.getUser().getProfile().getFullName()
        );
    }
}
