package com.mc.domain.work.service;

import java.util.UUID;

public interface BatchDeletionService {

    /**
     * Executes a batch update to soft-delete entities associated with a workspace.
     *
     * @param workspaceId The ID of the workspace whose associated entities are to be soft-deleted.
     * @param deletedById The ID of the user performing the deletion.
     * @param batchSize   The number of records to process in each batch.
     * @return The number of records soft-deleted in this batch.
     */
    int executeBatchUpdate(UUID workspaceId, UUID deletedById, int batchSize);
}
