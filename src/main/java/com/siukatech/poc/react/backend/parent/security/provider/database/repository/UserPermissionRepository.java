package com.siukatech.poc.react.backend.parent.security.provider.database.repository;

import com.siukatech.poc.react.backend.parent.security.provider.database.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, Long> {

    // https://stackoverflow.com/a/73583022
    static String SQL_FIND_USER_PERMISSION_BY_LOGIN_ID = "" +
            "select up.* from ( " +
            "select u.login_id login_id, u.id user_id " +
//            "--, uru.id user_role_user_id, uru.user_id, uru.user_role_id, ur.id user_role_id_2 " +
            ", urp.id id, urp.user_role_mid user_role_mid, urp.app_mid app_mid, urp.resource_mid resource_mid, urp.access_right access_right " +
            "from users u " +
            "inner join user_role_users uru on 1=1 " +
            "and uru.user_id = u.id " +
            "inner join user_roles ur on 1=1 " +
            "and ur.id = uru.user_role_id " +
            "inner join user_role_permissions urp on 1=1 " +
            "and urp.user_role_mid = ur.mid " +
            "inner join app_resources ar on 1=1 " +
            "and ar.mid = urp.resource_mid and ar.app_mid = urp.app_mid " +
            "inner join applications a on 1=1 " +
            "and a.mid = ar.app_mid " +
            "where 1=1 " +
            "and u.login_id = :loginId " +
            "and a.mid = :appMid " +
            ") as up " +
            "";
    @Query(value = SQL_FIND_USER_PERMISSION_BY_LOGIN_ID
            , nativeQuery = true)
    List<UserPermissionEntity> findUserPermissionByLoginIdAndAppMid(@Param("loginId") String loginId, @Param("appMid") String appMid);
}
