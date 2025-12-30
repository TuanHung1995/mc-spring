package com.mc.application.mapper;

import com.mc.application.model.board.ColumnValueDTO;
import com.mc.domain.model.entity.ColumnValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColumnValueMapper {

    ColumnValueDTO toColumnValueDTO(ColumnValue columnValue);

}
