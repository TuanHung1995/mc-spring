package com.mc.domain.service;

import com.mc.domain.model.entity.Item;

public interface ItemDomainService {

    Item reorderItem(Long itemId, Long targetGroup, Long prevItemId, Long nextItemId);

}
