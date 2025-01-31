package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.user.entity.UserPermissionJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionJpaEntity, String> {

    @EntityGraph(value = "UserPermissionJpaEntity.basic")
    List<UserPermissionJpaEntity> findByUserRoleEntityUserRoleUserEntitiesUserEntityUserId(String userId);

    @EntityGraph(value = "UserPermissionJpaEntity.basic")
    List<UserPermissionJpaEntity> findByApplicationEntityApplicationId(String applicationId);

    @EntityGraph(value = "UserPermissionJpaEntity.basic")
    List<UserPermissionJpaEntity> findByUserRoleEntityUserRoleUserEntitiesUserEntityUserIdAndApplicationEntityApplicationId(String userId, String applicationId);

}
