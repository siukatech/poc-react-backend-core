package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.user.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    @EntityGraph(attributePaths = {"appResourceEntities"
//            , "userRolePermissionEntities"
    })
    Optional<ApplicationEntity> findWithGraphAttrPathById(String id);

    @Query(value = "select o from ApplicationEntity o "
            + "join fetch o.appResourceEntities ar "
////            + "join fetch o.userRolePermissionEntities urp "
////            + "join fetch ar.userRolePermissionEntities urp "
////            + "join fetch urp.userRoleEntity ur "
////            + "join fetch ur.userRoleUserEntities uru "
////            + "join fetch uru.userEntity u "
//            + "join fetch o.appResourceEntities.userRolePermissionEntities urp "
            + "where 1=1 and o.id = :id ")
    Optional<ApplicationEntity> findWithJpqlAppResourcesById(String id);

    @Query(value = "select o from ApplicationEntity o "
//            + "join fetch o.appResourceEntities ar "
            + "join fetch o.userRolePermissionEntities urp "
            + "join fetch urp.userRoleEntity ur "
//            + "join fetch ur.userRoleUserEntities uru "
//            + "join fetch uru.userEntity u "
            + "where 1=1 and o.id = :id ")
    Optional<ApplicationEntity> findWithJpqlUserRolePermissionEntitiesById(String id);

    @EntityGraph(value = "ApplicationEntity.appResourceEntities")
    Optional<ApplicationEntity> findWithAppResourceEntitiesById(String id);

    @EntityGraph(value = "ApplicationEntity.userRolePermissionEntities")
    Optional<ApplicationEntity> findWithUserRolePermissionEntitiesById(String id);

}
