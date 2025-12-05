package com.mc.domain.port;

public interface RealTimeUpdatePort {
    /**
     * Gửi thông báo cập nhật cho một Board cụ thể.
     * Các client đang xem Board này sẽ nhận được message.
     * @param boardId ID của board
     * @param payload Dữ liệu thay đổi (VD: Task mới, Column mới...)
     */
    void sendBoardUpdate(Long boardId, Object payload);
}
