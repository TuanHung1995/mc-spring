package com.mc.application.work.event.handler;

import com.mc.application.work.service.BatchDeletionService;
import com.mc.domain.core.event.broker.rabbitMQ.WorkspaceDeletedIntegrationEvent;
import com.mc.domain.work.port.in.WorkspaceWorkerPort;
import com.mc.domain.work.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkspaceDeletedEventHandler implements WorkspaceWorkerPort {

    private final BoardRepository boardRepository;
    private final BatchDeletionService batchDeletionService;

    @Override
    @Transactional
    public void processWorkspaceDeletion(WorkspaceDeletedIntegrationEvent event) {
        log.info("Processing asynchronous deletion for Workspace ID: {}", event.getWorkspaceId());

        Long boards = boardRepository.countByWorkspaceId(event.getWorkspaceId());
        if (boards == 0) {
            log.info("No Boards found for Workspace ID: {}. Skipping deletion.", event.getWorkspaceId());
            return;
        }

        log.info("Found {} Boards for Workspace ID: {}. Triggering soft delete.", boards, event.getWorkspaceId());

        int boardBatchSize = 1000;
        int boardsDeleted;
        do {

            boardsDeleted = batchDeletionService.executeBoardBatchUpdate(event.getWorkspaceId(), event.getDeletedByUserId(), boardBatchSize);

            if (boardsDeleted > 0) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        } while (boardsDeleted == boardBatchSize);

        int taskGroupBatchSize = 1000;
        int taskGroupDeleted;
        do {

            taskGroupDeleted = batchDeletionService.executeGroupBatchUpdate(event.getWorkspaceId(), event.getDeletedByUserId(), taskGroupBatchSize);

            if (taskGroupDeleted > 0) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        } while (taskGroupDeleted == taskGroupBatchSize);

        int columnBatchSize = 1000;
        int columnsDeleted;
        do {

            columnsDeleted = batchDeletionService.executeColumnBatchUpdate(event.getWorkspaceId(), event.getDeletedByUserId(), columnBatchSize);

            if (columnsDeleted > 0) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        } while (columnsDeleted == columnBatchSize);

        int itemBatchSize = 1000;
        int itemsDeleted;
        do {

            itemsDeleted = batchDeletionService.executeItemBatchUpdate(event.getWorkspaceId(), event.getDeletedByUserId(), itemBatchSize);

            if (itemsDeleted > 0) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        } while (itemsDeleted == itemBatchSize);

        int valueBatchSize = 1000;
        int valueDeleted;
        do {

            valueDeleted = batchDeletionService.executeValueBatchUpdate(event.getWorkspaceId(), event.getDeletedByUserId(), itemBatchSize);

            if (valueDeleted > 0) {
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
            }
        } while (valueDeleted == valueBatchSize);

        log.info("Successfully processed workspace deletion for Workspace ID: {}", event.getWorkspaceId());
    }

}
