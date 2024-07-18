
--
--insert into users (login_id, name, created_by, last_modified_by, version_no)
--values ('app-user-01', 'app-user-01', 'admin', 'admin', 1)
--;

insert into users (login_id, name, created_by, last_modified_by, version_no)
values ('app-user-02', 'app-user-02', 'admin', 'admin', 1)
;

--
--insert into applications (mid, name, created_by, last_modified_by, version_no)
--values ('frontend-app', 'Frontend App', 'admin', 'admin', 1)
--;
--insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
--values ('menu.home', 'Menu Home', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
--values ('menu.items', 'Menu Items', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
--values ('menu.shops', 'Menu Shops', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
--values ('menu.merchants', 'Menu Merchants', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
--values ('menu.i18n', 'Menu i18n', 'frontend-app', 'view', 'admin', 'admin', 1)
--;


insert into user_roles (mid, name, created_by, last_modified_by, version_no)
values ('role-user-01', 'User 01', 'admin', 'admin', 1)
;
insert into user_roles (mid, name, created_by, last_modified_by, version_no)
values ('role-admin-01', 'Admin 01', 'admin', 'admin', 1)
;
insert into user_role_permissions (user_role_mid, app_mid, resource_mid, access_right, created_by, last_modified_by, version_no)
values ('role-user-01', 'frontend-app', 'menu.home', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (user_role_mid, app_mid, resource_mid, access_right, created_by, last_modified_by, version_no)
values ('role-user-01', 'frontend-app', 'menu.items', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (user_role_mid, app_mid, resource_mid, access_right, created_by, last_modified_by, version_no)
values ('role-user-01', 'frontend-app', 'menu.shops', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (user_role_mid, app_mid, resource_mid, access_right, created_by, last_modified_by, version_no)
values ('role-user-01', 'frontend-app', 'menu.merchants', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (user_role_mid, app_mid, resource_mid, access_right, created_by, last_modified_by, version_no)
values ('role-user-01', 'frontend-app', 'menu.lang', 'view', 'admin', 'admin', 1)
;

insert into user_role_users (user_role_id, user_id, created_by, last_modified_by, version_no)
values (
(select id from user_roles where 1=1 and mid = 'role-user-01')
, (select id from users where 1=1 and login_id = 'app-user-02')
, 'admin', 'admin', 1)
;

