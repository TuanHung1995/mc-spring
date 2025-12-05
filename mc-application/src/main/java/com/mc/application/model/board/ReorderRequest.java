package com.mc.application.model.board;

import lombok.Data;

@Data
public class ReorderRequest {
    private Long targetId;      // ID của đối tượng đang được kéo
    private Long previousId;    // ID của đối tượng đứng ngay TRƯỚC vị trí mới (null nếu lên đầu)
    private Long nextId;        // ID của đối tượng đứng ngay SAU vị trí mới (null nếu xuống cuối)


    private Long targetGroupId;
}
