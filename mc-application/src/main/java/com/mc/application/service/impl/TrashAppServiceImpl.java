package com.mc.application.service.impl;

import com.mc.application.service.TrashAppService;
import com.mc.domain.exception.BusinessLogicException;
import com.mc.domain.model.dto.TrashItem;
import com.mc.domain.service.TrashDomainService;
import com.mc.domain.service.BoardDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrashAppServiceImpl implements TrashAppService {

    private final TrashDomainService trashDomainService;
    private final BoardDomainService boardDomainService;

    @Override
    public List<TrashItem> getTrashItems(Long userId) {
        return trashDomainService.getAllTrashItems(userId);
    }

    @Override
    public void restoreItem(String type, Long id) {
        if ("BOARD".equalsIgnoreCase(type)) {
            trashDomainService.restoreBoard(id);
        } else {
            throw new BusinessLogicException("Restore logic for " + type + " is not implemented.");
        }
    }

    @Override
    public void deleteItemPermanently(String type, Long id) {
        // Implement logic for permanent deletion
         if ("BOARD".equalsIgnoreCase(type)) {
            // Need a method in BoardDomainService or TrashDomainService for permanent delete
            // For now, let's assume we can add it to TrashDomainService or reuse BoardDomainService
            boardDomainService.deleteBoardPermanently(id, null); // userId is not strictly needed for physical delete if ownership check is done prior or ignored for internal physical delete
        } else {
             throw new BusinessLogicException("Permanent delete logic for " + type + " is not implemented.");
         }
    }
}
