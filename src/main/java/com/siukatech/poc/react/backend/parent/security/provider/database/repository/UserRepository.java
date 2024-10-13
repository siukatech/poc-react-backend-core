package com.siukatech.poc.react.backend.parent.security.provider.database.repository;

import com.siukatech.poc.react.backend.parent.security.provider.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLoginId(String loginId);

}

