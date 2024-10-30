package com.siukatech.poc.react.backend.parent.user.repository;

import com.siukatech.poc.react.backend.parent.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByLoginId(String loginId);

}

