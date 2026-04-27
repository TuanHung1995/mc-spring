package com.mc.application.work.service.impl;

import com.mc.domain.work.repository.*;
import com.mc.application.work.service.BatchDeletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BatchDeletionServiceImpl implements BatchDeletionService {

    private final ColumnValueRepository columnValueRepository;
    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final TaskGroupRepository taskGroupRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int executeBoardBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize) {
        return boardRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int executeGroupBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize) {
        return taskGroupRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int executeColumnBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize) {
        return boardColumnRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int executeItemBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize) {
        return itemRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int executeValueBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize) {
        return columnValueRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }

}
