package com.mc.domain.service.util;

import org.springframework.stereotype.Component;

@Component
public class PositionCalculationService {

    private static final double DEFAULT_GAP = 10000.0;
    private static final double INITIAL_POSITION = 60000.0; // Vị trí bắt đầu an toàn

    public double calculateNewPosition(Double prevPos, Double nextPos) {
        // Case 1: Move to Top (Không có item trước)
        if (prevPos == null && nextPos != null) {
            return nextPos / 2;
        }

        // Case 2: Move to Bottom (Không có item sau)
        if (prevPos != null && nextPos == null) {
            return prevPos + DEFAULT_GAP;
        }

        // Case 3: Move Between (Kẹp giữa 2 items)
        if (prevPos != null && nextPos != null) {
            return (prevPos + nextPos) / 2;
        }

        // Case 4: List Empty (Item đầu tiên của list)
        return INITIAL_POSITION;
    }
}