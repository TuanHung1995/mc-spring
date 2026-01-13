create table flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index flyway_schema_history_s_idx
    on flyway_schema_history (success);

create table media
(
    id               bigint auto_increment
        primary key,
    UUID             char(36)                                                        not null,
    file_name        varchar(512)                                                    null,
    file_type        varchar(100)                                                    null,
    file_size        bigint                                                          null,
    storage_provider enum ('LOCAL', 'S3', 'GCS', 'AZURE')                            null,
    storage_path     varchar(1000)                                                   null,
    url              varchar(1000)                                                   null,
    is_public        tinyint(1)                         default 0                    null,
    metadata         json                                                            null,
    status           enum ('ACTIVE', 'TEMP', 'DELETED') default 'ACTIVE'             null,
    entity_type      enum ('USER', 'BOARD', 'TASK', 'COMMENT', 'GROUP', 'WORKSPACE') null,
    entity_id        bigint                                                          null,
    created_at       datetime                           default CURRENT_TIMESTAMP    null,
    updated_at       datetime                           default CURRENT_TIMESTAMP    null on update CURRENT_TIMESTAMP,
    deleted_at       datetime                                                        null,
    constraint UUID
        unique (UUID)
);

create table iam_users
(
    id                    binary(16)                                                     not null
        primary key,
    email                 varchar(255)                                                   not null,
    status                enum ('ACTIVE', 'LOCKED', 'PENDING') default 'ACTIVE'          not null,
    avatar_media_id       bigint                                                         null,
    full_name             varchar(255)                                                   null,
    password              varchar(255)                                                   null,
    provider              varchar(50)                                                    null,
    provider_id           varchar(255)                                                   null,
    avatar_url            varchar(255)                                                   null,
    job_title             varchar(255)                                                   null,
    phone                 varchar(255)                                                   null,
    address               varchar(255)                                                   null,
    birthday              datetime(6)                                                    null,
    email_verified_at     datetime                                                       null,
    reset_token           varchar(255)                                                   null,
    failed_login_attempts int                                  default 0                 null,
    unlock_token          varchar(255)                                                   null,
    created_at            datetime                             default CURRENT_TIMESTAMP null,
    updated_at            datetime                             default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_deleted            tinyint(1)                           default 0                 not null,
    email_verified        tinyint(1)                           default 0                 not null,
    constraint email
        unique (email),
    constraint fk_iam_users_avatar
        foreign key (avatar_media_id) references media (id)
            on delete set null
);

create table iam_refresh_tokens
(
    id          bigint auto_increment
        primary key,
    token       varchar(255)                       not null,
    user_id     binary(16)                         not null,
    expiry_date datetime                           not null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    constraint token
        unique (token),
    constraint fk_iam_refresh_token_user
        foreign key (user_id) references iam_users (id)
            on delete cascade
);

create index idx_media_entity
    on media (entity_type, entity_id);

create table permissions
(
    id          bigint auto_increment
        primary key,
    name        varchar(255) not null,
    description varchar(255) null,
    constraint name
        unique (name)
);

create table roles
(
    id          bigint auto_increment
        primary key,
    name        varchar(255)                     null,
    scope       enum ('SYSTEM', 'TEAM', 'BOARD') not null,
    description varchar(255)                     null,
    constraint uq_role_name_scope
        unique (name, scope)
);

create table role_permissions
(
    role_id       bigint not null,
    permission_id bigint not null,
    primary key (role_id, permission_id),
    constraint fk_rp_permission
        foreign key (permission_id) references permissions (id)
            on delete cascade,
    constraint fk_rp_role
        foreign key (role_id) references roles (id)
            on delete cascade
);

create table token_blacklist
(
    id          bigint auto_increment
        primary key,
    token       varchar(512)                       not null,
    expiry_date datetime                           not null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    constraint token
        unique (token)
);

create index idx_blacklist_token
    on token_blacklist (token);

create table users
(
    id                    bigint auto_increment
        primary key,
    email                 varchar(255)                                                   not null,
    status                enum ('ACTIVE', 'LOCKED', 'PENDING') default 'ACTIVE'          not null,
    avatar_media_id       bigint                                                         null,
    full_name             varchar(255)                                                   null,
    password              varchar(255)                                                   null,
    provider              varchar(50)                                                    null,
    provider_id           varchar(255)                                                   null,
    avatar_url            varchar(255)                                                   null,
    job_title             varchar(255)                                                   null,
    phone                 varchar(255)                                                   null,
    address               varchar(255)                                                   null,
    birthday              datetime(6)                                                    null,
    email_verified_at     datetime                                                       null,
    reset_token           varchar(255)                                                   null,
    failed_login_attempts int                                  default 0                 null,
    unlock_token          varchar(255)                                                   null,
    created_at            datetime                             default CURRENT_TIMESTAMP null,
    updated_at            datetime                             default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    is_deleted            tinyint(1)                           default 0                 not null,
    email_verified        tinyint(1)                           default 0                 not null,
    constraint email
        unique (email),
    constraint fk_users_avatar
        foreign key (avatar_media_id) references media (id)
            on delete set null
);

create table refresh_tokens
(
    id          bigint auto_increment
        primary key,
    token       varchar(255)                       not null,
    user_id     bigint                             not null,
    expiry_date datetime                           not null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    constraint token
        unique (token),
    constraint fk_refresh_token_user
        foreign key (user_id) references users (id)
            on delete cascade
);

create table teams
(
    id          bigint auto_increment
        primary key,
    created_by  bigint                             not null,
    name        varchar(255)                       null,
    slug        varchar(255)                       null,
    description varchar(255)                       null,
    created_at  datetime default CURRENT_TIMESTAMP null,
    constraint slug
        unique (slug),
    constraint fk_teams_created_by
        foreign key (created_by) references users (id)
            on delete cascade
);

create table team_members
(
    id        bigint auto_increment
        primary key,
    team_id   bigint                             not null,
    user_id   bigint                             not null,
    role_id   bigint                             not null,
    joined_at datetime default CURRENT_TIMESTAMP null,
    constraint uq_team_user
        unique (team_id, user_id),
    constraint fk_tm_role
        foreign key (role_id) references roles (id),
    constraint fk_tm_team
        foreign key (team_id) references teams (id)
            on delete cascade,
    constraint fk_tm_user
        foreign key (user_id) references users (id)
            on delete cascade
);

create table user_medias
(
    user_id  bigint not null,
    media_id bigint not null,
    constraint fk_user_media_media
        foreign key (media_id) references media (id),
    constraint fk_user_media_user
        foreign key (user_id) references users (id)
);

create index idx_iam_users_id
    on users (id);

create index idx_iam_users_status
    on users (status);

create index idx_users_status
    on users (status);

create table workspaces
(
    id         bigint auto_increment
        primary key,
    created_by bigint                             not null,
    deleted_by bigint                             null,
    team_id    bigint                             not null,
    name       varchar(255)                       null,
    status     varchar(255)                       null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    deleted_at datetime                           null,
    constraint fk_workspace_created_by
        foreign key (created_by) references users (id),
    constraint fk_workspace_deleted_by
        foreign key (deleted_by) references users (id),
    constraint fk_workspace_team
        foreign key (team_id) references teams (id)
            on delete cascade
);

create table apartments
(
    id             bigint unsigned auto_increment
        primary key,
    name           varchar(255)                       null,
    workspace_id   bigint                             not null,
    owner_id       bigint                             not null,
    team_id        bigint                             not null,
    description    varchar(255)                       null,
    background_url varchar(255)                       null,
    created_at     datetime default CURRENT_TIMESTAMP null,
    updated_at     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint fk_ap_owner
        foreign key (owner_id) references users (id),
    constraint fk_ap_team
        foreign key (team_id) references teams (id)
            on delete cascade,
    constraint fk_ap_workspace
        foreign key (workspace_id) references workspaces (id)
);

create table apartment_members
(
    id           bigint unsigned auto_increment
        primary key,
    apartment_id bigint unsigned                        not null,
    user_id      bigint                                 not null,
    role_id      bigint                                 null,
    is_owner     tinyint(1)                             null,
    status       enum ('ACTIVE', 'PENDING', 'REJECTED') null,
    joined_at    datetime default CURRENT_TIMESTAMP     null,
    constraint fk_apm_apartment
        foreign key (apartment_id) references apartments (id)
            on delete cascade,
    constraint fk_apm_role
        foreign key (role_id) references roles (id)
            on delete set null,
    constraint fk_apm_user
        foreign key (user_id) references users (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create table boards
(
    id           bigint unsigned auto_increment
        primary key,
    workspace_id bigint                                                            not null,
    created_by   bigint                                                            null,
    deleted_by   bigint                                                            null,
    name         varchar(255)                                                      not null,
    type         enum ('PUBLIC', 'PRIVATE', 'SHAREABLE') default 'PUBLIC'          null,
    purpose      varchar(255)                                                      null,
    description  varchar(255)                                                      null,
    is_deleted   tinyint(1)                              default 0                 not null,
    created_at   datetime                                default CURRENT_TIMESTAMP null,
    updated_at   datetime                                default (now())           null on update CURRENT_TIMESTAMP,
    deleted_at   datetime                                                          null,
    constraint fk_board_creator
        foreign key (created_by) references users (id)
            on delete set null,
    constraint fk_board_ws
        foreign key (workspace_id) references workspaces (id)
            on delete cascade,
    constraint fk_boards_deleter
        foreign key (deleted_by) references users (id)
)
    collate = utf8mb4_unicode_ci;

create table board_columns
(
    id          bigint unsigned auto_increment
        primary key,
    board_id    bigint unsigned                                                                                 not null,
    created_by  bigint                                                                                          null,
    deleted_by  bigint                                                                                          null,
    archived_by bigint                                                                                          null,
    title       varchar(255)                                                                                    not null,
    type        enum ('TEXT', 'STATUS', 'DATE', 'PERSON', 'NUMBER', 'TIMELINE', 'CHECKBOX', 'LINK', 'DROPDOWN') not null,
    settings    varbinary(255)                                                                                  null,
    description varchar(255)                                                                                    null,
    position    double                                                                                          not null,
    width       int        default 150                                                                          null,
    is_hidden   tinyint(1) default 0                                                                            null,
    is_deleted  tinyint(1) default 0                                                                            not null,
    is_archived tinyint(1) default 0                                                                            not null,
    created_at  datetime   default CURRENT_TIMESTAMP                                                            null,
    deleted_at  datetime                                                                                        null,
    updated_at  datetime   default (now())                                                                      null on update CURRENT_TIMESTAMP,
    archived_at datetime                                                                                        null,
    constraint fk_bc_archived_by
        foreign key (archived_by) references users (id)
            on delete set null,
    constraint fk_bc_board
        foreign key (board_id) references boards (id)
            on delete cascade,
    constraint fk_bc_created_by
        foreign key (created_by) references users (id)
            on delete set null,
    constraint fk_bc_deleted_by
        foreign key (deleted_by) references users (id)
            on delete set null
)
    collate = utf8mb4_unicode_ci;

create index idx_board_columns
    on board_columns (board_id);

create table board_members
(
    id        bigint auto_increment
        primary key,
    board_id  bigint unsigned                    not null,
    user_id   bigint                             not null,
    role_id   bigint                             not null,
    joined_at datetime default CURRENT_TIMESTAMP null,
    constraint uq_board_user
        unique (board_id, user_id),
    constraint fk_bm_board
        foreign key (board_id) references boards (id)
            on delete cascade,
    constraint fk_bm_role
        foreign key (role_id) references roles (id),
    constraint fk_bm_user
        foreign key (user_id) references users (id)
            on delete cascade
);

create index idx_workspace
    on boards (workspace_id);

create table folders
(
    id           bigint auto_increment
        primary key,
    created_by   bigint                             not null,
    deleted_by   bigint                             not null,
    archived_by  bigint                             not null,
    workspace_id bigint                             not null,
    name         varchar(255)                       null,
    slug         varchar(255)                       null,
    created_at   datetime default CURRENT_TIMESTAMP null,
    updated_at   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    deleted_at   datetime default CURRENT_TIMESTAMP null,
    archived_at  datetime default CURRENT_TIMESTAMP null,
    constraint slug
        unique (slug),
    constraint fk_folder_archived_by
        foreign key (archived_by) references users (id),
    constraint fk_folder_created_by
        foreign key (created_by) references users (id),
    constraint fk_folder_deleted_by
        foreign key (deleted_by) references users (id),
    constraint fk_folder_workspace
        foreign key (workspace_id) references workspaces (id)
            on delete cascade
);

create table task_groups
(
    id           bigint unsigned auto_increment
        primary key,
    created_by   bigint                                 null,
    board_id     bigint unsigned                        not null,
    deleted_by   bigint                                 null,
    archived_by  bigint                                 null,
    title        varchar(255) default 'New Group'       not null,
    color        varchar(255)                           null,
    position     double       default 65535             not null,
    is_collapsed tinyint(1)   default 0                 null,
    is_deleted   tinyint(1)   default 0                 not null,
    is_archived  tinyint(1)   default 0                 not null,
    created_at   datetime     default CURRENT_TIMESTAMP null,
    updated_at   datetime     default (now())           null on update CURRENT_TIMESTAMP,
    archived_at  datetime                               null,
    deleted_at   datetime                               null,
    constraint fk_bg_archived_by
        foreign key (archived_by) references users (id)
            on delete set null,
    constraint fk_bg_board
        foreign key (board_id) references boards (id)
            on delete cascade,
    constraint fk_bg_created_by
        foreign key (created_by) references users (id),
    constraint fk_bg_deleted_by
        foreign key (deleted_by) references users (id)
            on delete set null
)
    collate = utf8mb4_unicode_ci;

create table items
(
    id          bigint unsigned auto_increment
        primary key,
    created_by  bigint                               null,
    group_id    bigint unsigned                      not null,
    board_id    bigint unsigned                      not null,
    deleted_by  bigint                               null,
    archived_by bigint                               null,
    name        varchar(255)                         not null,
    position    double                               not null,
    is_deleted  tinyint(1) default 0                 not null,
    is_archived tinyint(1) default 0                 not null,
    created_at  datetime   default CURRENT_TIMESTAMP null,
    updated_at  datetime   default (now())           null on update CURRENT_TIMESTAMP,
    deleted_at  datetime                             null,
    archived_at datetime                             null,
    constraint fk_item_archived_by
        foreign key (archived_by) references users (id)
            on delete set null,
    constraint fk_item_board
        foreign key (board_id) references boards (id)
            on delete cascade,
    constraint fk_item_creator
        foreign key (created_by) references users (id)
            on delete set null,
    constraint fk_item_deleted_by
        foreign key (deleted_by) references users (id)
            on delete set null,
    constraint fk_item_group
        foreign key (group_id) references task_groups (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create table column_values
(
    id         bigint unsigned auto_increment
        primary key,
    item_id    bigint unsigned                      not null,
    column_id  bigint unsigned                      not null,
    board_id   bigint unsigned                      not null,
    value      varchar(255)                         null,
    text_value varchar(255)                         null,
    color      varchar(255)                         null,
    type       varchar(255)                         null,
    is_deleted tinyint(1) default 0                 not null,
    created_at datetime   default CURRENT_TIMESTAMP null,
    updated_at datetime   default (now())           null on update CURRENT_TIMESTAMP,
    deleted_at datetime                             null,
    constraint unique_cell
        unique (item_id, column_id),
    constraint fk_cv_board
        foreign key (board_id) references boards (id)
            on delete cascade,
    constraint fk_cv_column
        foreign key (column_id) references board_columns (id)
            on delete cascade,
    constraint fk_cv_item
        foreign key (item_id) references items (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_cv_column
    on column_values (column_id);

create index idx_items_board
    on items (board_id);

create index idx_items_created_by
    on items (created_by);

create index idx_items_deleted_by
    on items (deleted_by);

create index idx_items_group
    on items (group_id);

create index idx_board_groups
    on task_groups (board_id);

create table workspace_members
(
    id           bigint unsigned auto_increment
        primary key,
    workspace_id bigint                             null,
    user_id      bigint                             null,
    role_id      bigint                             not null,
    joined_at    datetime default CURRENT_TIMESTAMP null,
    constraint fk_wsm_role
        foreign key (role_id) references roles (id),
    constraint fk_wsm_user
        foreign key (user_id) references users (id)
            on delete cascade,
    constraint fk_wsm_workspace
        foreign key (workspace_id) references workspaces (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

