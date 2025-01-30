package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @EntityGraph(value = "UserEntity.basic")
    Optional<UserEntity> findByLoginId(String loginId);

}

