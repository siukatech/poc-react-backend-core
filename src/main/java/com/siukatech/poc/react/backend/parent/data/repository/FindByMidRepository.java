package com.siukatech.poc.react.backend.parent.data.repository;

import java.util.Optional;

/**
 *
 * *** READ this ***
 * This is NOT working, DO NOT define an abstract repository interface for more than 3 types.
 * public interface AbstractRepository<T, ID, MID> extends JpaRepository<T, ID>, FindByMidRepository<T, MID> {}
 *
 * @param <T>
 * @param <MID>
 */
public interface FindByMidRepository<T, MID> {
    Optional<T> findByMid(MID mid);
}
