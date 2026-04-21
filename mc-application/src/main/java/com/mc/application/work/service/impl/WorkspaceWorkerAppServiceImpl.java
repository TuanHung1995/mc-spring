package com.mc.application.work.service.impl;

import com.mc.domain.work.port.in.WorkspaceWorkerPort;
import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.work.model.entity.Board;
import com.mc.domain.work.repository.BoardRepository;
import com.mc.domain.work.service.BatchDeletionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * WorkspaceWorkerAppServiceImpl — Application Service (Work Context)
 *
 * <p>Implements the background work necessary when cross-module integration events
 * arrive. This service orchestrates the deletion of all Boards that belong to a
 * deleted Workspace.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceWorkerAppServiceImpl implements WorkspaceWorkerPort {

    @Qualifier("workBoardRepository")
    private final BoardRepository boardRepository;
    private final BatchDeletionService batchDeletionService;

    @Override
    @Transactional
    public void processWorkspaceDeletion(WorkspaceDeletedIntegrationEvent event) {
        log.info("Processing asynchronous deletion for Workspace ID: {}", event.workspaceId());

        List<Board> boards = boardRepository.findAllByWorkspaceId(event.workspaceId());
        if (boards.isEmpty()) {
            log.info("No Boards found for Workspace ID: {}. Skipping deletion.", event.workspaceId());
            return;
        }

        log.info("Found {} Boards for Workspace ID: {}. Triggering soft delete.", boards.size(), event.workspaceId());

        int batchSize = 1000;
        int updatedCount;
        do {

            updatedCount = batchDeletionService.executeBatchUpdate(event.workspaceId(), event.deletedById(), batchSize);

            if (updatedCount > 0) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        } while (updatedCount == batchSize);

        log.info("Successfully processed workspace deletion for Workspace ID: {}", event.workspaceId());
    }
}
