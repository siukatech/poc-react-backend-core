
--
--insert into users (id, login_id, name, created_by, last_modified_by, version_no)
--values ('app-user-01', 'app-user-01', 'app-user-01', 'admin', 'admin', 1)
--;

insert into users (id, login_id, name, created_by, last_modified_by, version_no)
values ('app-user-02', 'app-user-02', 'app-user-02', 'admin', 'admin', 1)
;

--
--insert into applications (id, name, created_by, last_modified_by, version_no)
--values ('frontend-app', 'Frontend App', 'admin', 'admin', 1)
--;
--insert into app_resources (id, name, application_id, access_right, created_by, last_modified_by, version_no)
--values ('menu.home', 'Menu Home', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (id, name, application_id, access_right, created_by, last_modified_by, version_no)
--values ('menu.items', 'Menu Items', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (id, name, application_id, access_right, created_by, last_modified_by, version_no)
--values ('menu.shops', 'Menu Shops', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (id, name, application_id, access_right, created_by, last_modified_by, version_no)
--values ('menu.merchants', 'Menu Merchants', 'frontend-app', 'view', 'admin', 'admin', 1)
--;
--insert into app_resources (id, name, application_id, access_right, created_by, last_modified_by, version_no)
--values ('menu.i18n', 'Menu i18n', 'frontend-app', 'view', 'admin', 'admin', 1)
--;


insert into user_roles (id, name, created_by, last_modified_by, version_no)
values ('role-user-01', 'User 01', 'admin', 'admin', 1)
;
insert into user_roles (id, name, created_by, last_modified_by, version_no)
values ('role-admin-01', 'Admin 01', 'admin', 'admin', 1)
;
insert into user_role_permissions (id, user_role_id, application_id, app_resource_id, access_right, created_by, last_modified_by, version_no)
values ('user_role_permissions-01', 'role-user-01', 'frontend-app', 'menu.home', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (id, user_role_id, application_id, app_resource_id, access_right, created_by, last_modified_by, version_no)
values ('user_role_permissions-02', 'role-user-01', 'frontend-app', 'menu.items', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (id, user_role_id, application_id, app_resource_id, access_right, created_by, last_modified_by, version_no)
values ('user_role_permissions-03', 'role-user-01', 'frontend-app', 'menu.shops', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (id, user_role_id, application_id, app_resource_id, access_right, created_by, last_modified_by, version_no)
values ('user_role_permissions-04', 'role-user-01', 'frontend-app', 'menu.merchants', 'view', 'admin', 'admin', 1)
;
insert into user_role_permissions (id, user_role_id, application_id, app_resource_id, access_right, created_by, last_modified_by, version_no)
values ('user_role_permissions-05', 'role-user-01', 'frontend-app', 'menu.lang', 'view', 'admin', 'admin', 1)
;

insert into user_role_users (id, user_role_id, user_id, created_by, last_modified_by, version_no)
values (
random_uuid()
, (select id from user_roles where 1=1 and id = 'role-user-01')
, (select id from users where 1=1 and login_id = 'app-user-02')
, 'admin', 'admin', 1)
;

