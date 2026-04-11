package com.mc.domain.work.port;

import java.util.UUID;

/**
 * @param id
 * @param email
 * @param fullName
 */
public record WorkUserView(UUID id, String email, String fullName) {
}