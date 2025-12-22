package com.mc.domain.service;

import com.mc.domain.model.entity.ColumnValue;

public interface ColumnValueDomainService {

    ColumnValue updateColumnValue(Long columnValueId, String newValue, String newColor, String newText);

}
