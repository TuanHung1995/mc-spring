package com.mc.domain.port;

import java.util.Optional;

public interface UserContextPort {

    /**
     * Lấy ID của user đang đăng nhập hiện tại.
     * @return ID user
     * @throws RuntimeException nếu không tìm thấy user (chưa login)
     */
    Long getCurrentUserId();

    /**
     * Lấy ID user (trả về Optional để caller tự xử lý nếu null)
     */
    Optional<Long> findCurrentUserId();

}
