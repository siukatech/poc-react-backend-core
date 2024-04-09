


create table if not exists applications (
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
--create unique index applications_idx1_mid on applications(mid);


create table if not exists app_resources (
  id serial not null
  , mid varchar(150) not null
  , name varchar(350) not null
  , app_mid varchar(150) not null
  , access_right varchar(150) not null
  , created_by varchar(150) not null
  , created_datetime timestamp not null default now()
  , last_modified_by varchar(150) not null
  , last_modified_datetime timestamp not null default now()
  , version_no int not null
  , primary key (id)
  , unique (app_mid, mid)
  , constraint app_resources_fk1_app_mid foreign key(app_mid) references applications(mid) on delete set null
);
comment on column app_resources.access_right is 'Resource requires access right';
--create unique index app_resources_idx1_mid on app_resources(app_mid, mid);

