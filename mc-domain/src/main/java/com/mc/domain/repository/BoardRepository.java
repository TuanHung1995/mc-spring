package com.mc.domain.repository;

import com.mc.domain.model.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findById(Long boardId);

    List<Board> findAllByUserId(Long userId);

    Board save(Board board);

    /**
     * Soft delete the given board from the repository.
     * @param board the board to be soft-deleted
     */
    void delete(Board board);

    /**
     * Permanently removes the board from the database.
     * This operation cannot be undone.
     *
     * @param boardId the ID of the board to delete
     */
    void deletePhysical(Long boardId);

    /**
     * Finds all boards that have been soft-deleted by a specific user.
     * @param userId the ID of the user who deleted the board
     * @return List of soft-deleted boards
     */
    List<Board> findAllDeletedBoards(Long userId);

    /**
     * Restores a soft-deleted board by setting is_deleted = false.
     * @param boardId the ID of the board to restore
     */
    void restoreBoard(Long boardId);

}
