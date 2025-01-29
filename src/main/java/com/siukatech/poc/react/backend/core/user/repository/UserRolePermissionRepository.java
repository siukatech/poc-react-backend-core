package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.user.entity.UserRolePermissionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolePermissionRepository extends JpaRepository<UserRolePermissionEntity, String> {

    @EntityGraph(value = "UserRolePermission.findBy")
    List<UserRolePermissionEntity> findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginId(String loginId);

    @EntityGraph(value = "UserRolePermission.findBy")
    List<UserRolePermissionEntity> findByApplicationEntityId(String applicationId);

    @EntityGraph(value = "UserRolePermission.findBy")
    List<UserRolePermissionEntity> findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginIdAndApplicationEntityId(String loginId, String applicationId);

}
