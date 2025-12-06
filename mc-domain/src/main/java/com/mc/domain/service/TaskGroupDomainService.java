package com.mc.domain.service;

public interface TaskGroupDomainService {

    void reorderGroup(Long targetGroup, Long prevItemId, Long nextItemId);

}
