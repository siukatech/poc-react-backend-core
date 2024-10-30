package com.siukatech.poc.react.backend.parent.user.repository;

import com.siukatech.poc.react.backend.parent.user.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, Long> {

    // https://stackoverflow.com/a/73583022
    static String SQL_FIND_USER_PERMISSION_BY_LOGIN_ID = "" +
            "select up.* from ( " +
            "select u.login_id login_id, u.id user_id " +
////            "--, uru.id user_role_user_id, uru.user_id, uru.user_role_id, ur.id user_role_id_2 " +
            ", urp.id id, urp.user_role_id user_role_id, urp.application_id application_id, urp.app_resource_id app_resource_id, urp.access_right access_right " +
            "from users u " +
            "inner join user_role_users uru on 1=1 " +
            "and uru.user_id = u.id " +
            "inner join user_roles ur on 1=1 " +
            "and ur.id = uru.user_role_id " +
            "inner join user_role_permissions urp on 1=1 " +
            "and urp.user_role_id = ur.id " +
            "inner join app_resources ar on 1=1 " +
            "and ar.id = urp.app_resource_id and ar.application_id = urp.application_id " +
            "inner join applications a on 1=1 " +
            "and a.id = ar.application_id " +
            "where 1=1 " +
            "and u.login_id = :loginId " +
            "and a.id = :applicationId " +
            ") as up " +
            "";
    @Query(value = SQL_FIND_USER_PERMISSION_BY_LOGIN_ID
            , nativeQuery = true)
    List<UserPermissionEntity> findUserPermissionByLoginIdAndApplicationId(@Param("loginId") String loginId, @Param("applicationId") String applicationId);
}
