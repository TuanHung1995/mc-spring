package com.mc.domain.core.port.in;

import java.util.UUID;

public interface RealTimeUpdatePort {
    /**
     * Gửi thông báo cập nhật cho một Board cụ thể.
     * Các client đang xem Board này sẽ nhận được message.
     * @param boardId ID của board
     * @param payload Dữ liệu thay đổi (VD: Task mới, Column mới...)
     */
    void sendBoardUpdate(UUID boardId, Object payload);

    /**
     * Dispatches a real-time update to all clients subscribed to a specific workspace.
     * * @param workspaceId The unique identifier of the workspace.
     * @param payload The data to be broadcasted (e.g., chat message, workspace settings update).
     */
    void sendWorkspaceUpdate(UUID workspaceId, Object payload);

}
