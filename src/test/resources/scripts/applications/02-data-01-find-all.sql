

insert into applications (mid, name, created_by, last_modified_by, version_no)
values ('frontend-app', 'Frontend App', 'admin', 'admin', 1)
;
insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
values ('menu.home', 'Menu Home', 'frontend-app', 'view', 'admin', 'admin', 1)
;
insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
values ('menu.items', 'Menu Items', 'frontend-app', 'view', 'admin', 'admin', 1)
;
insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
values ('menu.shops', 'Menu Shops', 'frontend-app', 'view', 'admin', 'admin', 1)
;
insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
values ('menu.merchants', 'Menu Merchants', 'frontend-app', 'view', 'admin', 'admin', 1)
;
insert into app_resources (mid, name, app_mid, access_right, created_by, last_modified_by, version_no)
values ('menu.i18n', 'Menu i18n', 'frontend-app', 'view', 'admin', 'admin', 1)
;


