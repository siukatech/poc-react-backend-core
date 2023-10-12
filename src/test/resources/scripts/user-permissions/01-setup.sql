
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
--  , mid varchar(150) not null
--  , name varchar(350) not null
--  , created_by varchar(150) not null
--  , created_datetime timestamp not null default now()
--  , last_modified_by varchar(150) not null
--  , last_modified_datetime timestamp not null default now()
--  , version_no int not null
--  , primary key (id)
--  , unique (mid)
--);
----create unique index applications_idx1_mid on applications(mid);
--
--
--create table if not exists app_resources (
--  id serial not null
--  , mid varchar(150) not null
--  , name varchar(350) not null
--  , app_mid varchar(150) not null
--  , access_right varchar(150) not null
--  , created_by varchar(150) not null
--  , created_datetime timestamp not null default now()
--  , last_modified_by varchar(150) not null
--  , last_modified_datetime timestamp not null default now()
--  , version_no int not null
--  , primary key (id)
--  , unique (app_mid, mid)
--  , constraint app_resources_fk1_app_mid foreign key(app_mid) references applications(mid) on delete set null
--);
--comment on column app_resources.access_right is 'Resource requires access right';
----create unique index app_resources_idx1_mid on app_resources(app_mid, mid);
--



create table if not exists user_roles (
  id serial not null
  , mid varchar(150) not null
  , name varchar(350) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , unique (mid)
);
--create unique index user_roles_idx1_mid on user_roles(mid);



create table if not exists user_role_permissions (
  id serial not null
  , user_role_mid varchar(150) not null
  , app_mid varchar(150) not null
  , resource_mid varchar(150) not null
  , access_right varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , constraint user_role_users_fk1_user_role_mid foreign key(user_role_mid) references user_roles(mid) on delete set null
  , constraint user_role_users_fk2_app_resource_mid foreign key(app_mid, resource_mid) references app_resources(app_mid, mid) on delete set null
);


create table if not exists user_role_users (
  id serial not null
  , user_role_id int not null
  , user_id int not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , constraint user_role_users_fk1_user_role_id foreign key(user_role_id) references user_roles(id) on delete set null
  , constraint user_role_users_fk2_user_id foreign key(user_id) references users(id) on delete set null
);

