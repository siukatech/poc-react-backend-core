package com.siukatech.poc.react.backend.parent.security.provider.database.repository;

import com.siukatech.poc.react.backend.parent.security.provider.database.entity.UserViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserViewRepository extends JpaRepository<UserViewEntity, Long> {

    Optional<UserViewEntity> findByLoginId(String loginId);
}
