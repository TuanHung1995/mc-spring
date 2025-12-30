package com.mc.application.service;

import com.mc.domain.model.dto.TrashItem;
import java.util.List;

public interface TrashAppService {
    
    List<TrashItem> getTrashItems(Long userId);
    
    void restoreItem(String type, Long id);
    
    void deleteItemPermanently(String type, Long id);
}
