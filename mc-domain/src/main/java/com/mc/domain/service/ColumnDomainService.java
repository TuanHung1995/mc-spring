package com.mc.domain.service;

import com.mc.domain.model.entity.Column;

import java.util.Optional;

public interface ColumnDomainService {

    Column reorderColumn(Long columnId, Long prevItemId, Long nextItemId);

}
