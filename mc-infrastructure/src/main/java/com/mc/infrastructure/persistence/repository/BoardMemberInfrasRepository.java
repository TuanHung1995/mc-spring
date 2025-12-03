package com.mc.infrastructure.persistence.repository;

import com.mc.domain.model.entity.BoardMember;
import com.mc.domain.model.entity.Role;
import com.mc.domain.repository.BoardMemberRepository;
import com.mc.infrastructure.config.cache.caffeine.CacheConfig;
import com.mc.infrastructure.persistence.mapper.BoardMemberJPAMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Infrastructure implementation of the BoardMemberRepository.
 *
 * <p>This repository provides data access operations for board member entities,
 * implementing the repository interface defined in the domain layer following
 * the Hexagonal Architecture pattern (Ports and Adapters).</p>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Managing board member data persistence</li>
 *   <li>Caching frequently accessed role lookups to improve performance</li>
 *   <li>Translating between domain entities and JPA entities</li>
 * </ul>
 *
 * <p>Note: The EntityManager is currently injected but not used. Consider removing
 * it if not needed for future custom queries.</p>
 *
 * @author Tuan Hung Nguyen
 * @version 1.0
 * @see BoardMemberRepository
 * @see BoardMemberJPAMapper
 */
@Repository
@RequiredArgsConstructor
public class BoardMemberInfrasRepository implements BoardMemberRepository {

    /**
     * JPA EntityManager for custom query operations.
     * Currently not utilized - consider removing if not needed.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPA mapper for board member operations.
     * Handles the actual database interactions through Spring Data JPA.
     */
    private final BoardMemberJPAMapper boardMemberJPAMapper;

    /**
     * Retrieves the role of a user within a specific board.
     *
     * <p>This method implements a caching strategy to reduce database queries
     * for frequently accessed role information. The cache key is constructed
     * using both boardId and userId to ensure unique cache entries.</p>
     *
     * <p>Performance considerations:</p>
     * <ul>
     *   <li>Cached results improve authorization checks performance</li>
     *   <li>Cache is invalidated when board membership changes</li>
     *   <li>Null results are not cached to prevent negative caching issues</li>
     * </ul>
     *
     * @param boardId the unique identifier of the board
     * @param userId the unique identifier of the user
     * @return an Optional containing the Role if found, empty otherwise
     * @throws none - exceptions are caught and converted to empty Optional
     */
    @Override
    @Cacheable(value = CacheConfig.BOARD_ROLE_CACHE, key = "#boardId + '-' + #userId", unless = "#result == null")
    public Optional<Role> findRoleByBoardIdAndUserId(Long boardId, Long userId) {
        try {
            return boardMemberJPAMapper
                    .findRoleByBoardIdAndUserId(boardId, userId);
        } catch (Exception e) {
            // Silently handle exceptions by returning empty Optional
            // TODO: Consider logging this exception for monitoring purposes
            return Optional.empty();
        }
    }

    /**
     * Persists a board member entity to the database.
     *
     * <p>This method invalidates the role cache for the specific board-user
     * combination to ensure cache consistency after updates. The cache eviction
     * is crucial for maintaining data integrity in the caching layer.</p>
     *
     * <p>Cache strategy:</p>
     * <ul>
     *   <li>Evicts cache entry before saving to prevent stale data</li>
     *   <li>Next read will fetch fresh data from database</li>
     *   <li>Ensures eventual consistency between cache and database</li>
     * </ul>
     *
     * @param boardMember the board member entity to save
     * @throws any exceptions thrown by the underlying JPA operation
     */
    @Override
    @CacheEvict(value = CacheConfig.BOARD_ROLE_CACHE, key = "#boardMember.board.id + '-' + #boardMember.user.id")
    public void save(BoardMember boardMember) {
        boardMemberJPAMapper.save(boardMember);
    }

    /**
     * Checks if a user is a member of a specific board.
     *
     * <p>This method reuses the existing findRoleByBoardIdAndUserId query
     * to determine membership. While functional, this approach may benefit
     * from optimization if only existence checking is needed.</p>
     *
     * <p>Performance note: Consider implementing a dedicated EXISTS query
     * if this method is called frequently, as it would be more efficient
     * than retrieving the full role object.</p>
     *
     * @param boardId the unique identifier of the board
     * @param userId the unique identifier of the user
     * @return true if the user is a member of the board, false otherwise
     */
    @Override
    public boolean existsByBoardIdAndUserId(Long boardId, Long userId) {
        return boardMemberJPAMapper.findRoleByBoardIdAndUserId(boardId, userId).isPresent();
    }

}
