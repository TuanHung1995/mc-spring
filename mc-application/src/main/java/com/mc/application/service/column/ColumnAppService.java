package com.mc.application.service.column;

import com.mc.application.model.column.*;

import java.util.List;

public interface ColumnAppService {

    CreateColumnResponse createColumn(CreateColumnRequest request);

    GetColumnResponse getColumn(Long columnId);

    List<GetColumnResponse> getColumnsByBoard(Long boardId);

    UpdateColumnResponse updateColumn(UpdateColumnRequest request);

    void deleteColumn(DeleteColumnRequest request);

}
