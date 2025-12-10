package com.mc.domain.service;

import com.mc.domain.model.entity.TaskGroup;

public interface TaskGroupDomainService {

    TaskGroup reorderGroup(Long targetGroup, Long prevItemId, Long nextItemId);

}
