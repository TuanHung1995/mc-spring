package com.mc.domain.model.dto;

import com.mc.domain.model.enums.BoardType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TrashItem {
    private Long id;
    private String name;
    private String type; // BOARD, ITEM, WORKSPACE
    private Date deletedAt;
    private Long deletedBy;
    private String deletedByName; // Optional, for UI display
}
