package com.mc.domain.work.service.impl;

import com.mc.domain.work.repository.BoardRepository;
import com.mc.domain.work.service.BatchDeletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BatchDeletionServiceImpl implements BatchDeletionService {

    @Qualifier("workBoardRepository")
    private final BoardRepository boardRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int executeBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize) {
        return boardRepository.softDeleteByWorkspaceIdInBatch(workspaceId, deletedById, batchSize);
    }
}
