package com.mc.domain.service;

public interface ItemDomainService {

    void reorderItem(Long itemId, Long targetGroup, Long prevItemId, Long nextItemId);

}
