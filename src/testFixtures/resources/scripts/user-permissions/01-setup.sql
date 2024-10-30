
--
--create table if not exists users (
--  id serial not null
--  , login_id varchar(350) not null
--  , name varchar(350) not null
--  , public_key text
--  , private_key text
--  , created_by varchar(150) not null
--  , created_datetime timestamp not null default now()
--  , last_modified_by varchar(150) not null
--  , last_modified_datetime timestamp not null default now()
--  , version_no int not null
--  , primary key (id)
--);
--create index users_idx1_login_id on users(login_id);
--
--
--
--create table if not exists applications (
--  id serial not null
--  , id varchar(150) not null
--  , name varchar(350) not null
--  , created_by varchar(150) not null
--  , created_datetime timestamp not null default now()
--  , last_modified_by varchar(150) not null
--  , last_modified_datetime timestamp not null default now()
--  , version_no int not null
--  , primary key (id)
--  , unique (id)
--);
----create unique index applications_idx1_id on applications(id);
--
--
--create table if not exists app_resources (
--  id serial not null
--  , id varchar(150) not null
--  , name varchar(350) not null
--  , application_id varchar(150) not null
--  , access_right varchar(150) not null
--  , created_by varchar(150) not null
--  , created_datetime timestamp not null default now()
--  , last_modified_by varchar(150) not null
--  , last_modified_datetime timestamp not null default now()
--  , version_no int not null
--  , primary key (id)
--  , unique (application_id, id)
--  , constraint app_resources_fk1_application_id foreign key(application_id) references applications(id) on delete set null
--);
--comment on column app_resources.access_right is 'Resource requires access right';
----create unique index app_resources_idx1_id on app_resources(application_id, id);
--



create table if not exists user_roles (
--  id serial not null
  id varchar(150) not null default random_uuid()
--  , mid varchar(150) not null
  , name varchar(350) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
--  , unique (mid)
);
--create unique index user_roles_idx1_id on user_roles(id);



create table if not exists user_role_permissions (
--  id serial not null
  id varchar(150) not null default random_uuid()
  , user_role_id varchar(150) not null
  , application_id varchar(150) not null
  , app_resource_id varchar(150) not null
  , access_right varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , constraint user_role_permissions_fk1_user_role_id foreign key(user_role_id) references user_roles(id) on delete set null
  , constraint user_role_permissions_fk2_app_resource_id foreign key(application_id, app_resource_id) references app_resources(application_id, id) on delete set null
);


create table if not exists user_role_users (
--  id serial not null
  id varchar(150) not null default random_uuid()
--  , user_role_id int not null
--  , user_id int not null
  , user_role_id varchar(150) not null
  , user_id varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , constraint user_role_users_fk1_user_role_id foreign key(user_role_id) references user_roles(id) on delete set null
  , constraint user_role_users_fk2_user_id foreign key(user_id) references users(id) on delete set null
);

