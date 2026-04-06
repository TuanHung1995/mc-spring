package com.mc.domain.organization.port;

/**
 * OrgUserContextPort — Outbound Port (Organization Bounded Context)
 *
 * <p><strong>WHY THIS PORT EXISTS (and why it's separate from the legacy {@code UserContextPort}):</strong>
 *
 * The existing {@code UserContextPort.getCurrentUserId()} returns {@code Long} — it is
 * tied to the legacy {@code users} table (auto-increment Long PK). The Organization
 * context was refactored to use {@code iam_users} (UUID PK).</p>
 *
 * <p>Creating a separate port here:
 * <ol>
 *   <li>Keeps the Organization context decoupled from the legacy Long ID world.</li>
 *   <li>Returns {@link OrgUserView} — the ACL read model — rather than leaking
 *       IAM's internal {@code User} entity into this context.</li>
 *   <li>Can be implemented by the IAM infrastructure adapter without touching legacy code.</li>
 * </ol>
 * </p>
 *
 * <p>The implementation lives in {@code mc-infrastructure} and resolves the current
 * user from Spring Security's {@code SecurityContextHolder}.</p>
 */
public interface OrgUserContextPort {

    /**
     * Returns a view of the currently authenticated user, as seen by the Organization context.
     *
     * <p>Throws a runtime exception if no user is authenticated (i.e., the request
     * is unauthenticated — this should never reach the application layer in a secured endpoint).</p>
     *
     * @return {@link OrgUserView} containing the authenticated user's UUID, email, and name.
     * @throws com.mc.domain.iam.exception.UserNotFoundException if no authenticated user is found.
     */
    OrgUserView getCurrentUser();
}
