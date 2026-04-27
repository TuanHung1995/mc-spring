package com.mc.domain.organization.port;

import java.util.UUID;

/**
 * OrgUserView — Anti-Corruption Layer Read Model (Organization Context)
 *
 * <p><strong>WHY THIS EXISTS:</strong>
 * The Organization context needs to know who the currently-authenticated user is
 * in order to set {@code ownerId}, {@code createdBy}, etc. However, it must NOT
 * import anything from the IAM context's internal model (e.g., {@code com.mc.domain.iam.model.User}).
 * Doing so would tightly couple two bounded contexts at the domain level.</p>
 *
 * <p>Instead, the Organization context defines its own minimal "view" of a user
 * — only the fields it actually cares about. The IAM infrastructure adapter
 * implements {@link OrgUserContextPort} and maps from the IAM model to this view.
 * This is the Anti-Corruption Layer (ACL) pattern.</p>
 *
 * <p>Implemented as a {@code record} because it is immutable data — a snapshot
 * of user identity at the time of the request.</p>
 *
 * @param id        The IAM user's UUID (from {@code iam_users.id}).
 * @param email     The user's email address.
 * @param fullName  The user's display name.
 */
public record OrgUserView(UUID id, String email, String fullName) {
}
