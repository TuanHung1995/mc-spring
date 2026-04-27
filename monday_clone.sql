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

create table verification_codes
(
    id         binary(16)                                            not null
        primary key,
    user_id    binary(16)                                            not null,
    code       varchar(20)                                           not null,
    status     enum ('USED', 'EXPIRED', 'PENDING') default 'PENDING' not null,
    is_deleted tinyint(1)                          default 0         null,
    created_at datetime                                              null,
    updated_at datetime                                              null on update CURRENT_TIMESTAMP,
    expired_at datetime                                              null
);

create index verification_codes_code_index
    on verification_codes (code);

create index verification_codes_user_index
    on verification_codes (user_id);

create table password_reset_tokens
(
    id          binary(16)           not null,
    token       varchar(255)         not null,
    user_id     binary(16)           not null,
    created_by  binary(16)           null,
    updated_by  binary(16)           null,
    expiry_Date datetime             not null,
    used        tinyint(1)           null,
    created_at  datetime             not null,
    updated_at  datetime             null on update CURRENT_TIMESTAMP,
    is_deleted  tinyint(1) default 0 not null,
    constraint idx_password_reset_token
        unique (token),
    constraint prt_token_uq
        unique (token)
);

create index idx_password_reset_user
    on password_reset_tokens (user_id);

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

create table org_teams
(
    id          binary(16)                           not null
        primary key,
    created_by  binary(16)                           null,
    updated_by  binary(16)                           null,
    deleted_by  binary(16)                           null,
    name        varchar(255)                         null,
    slug        varchar(255)                         null,
    description varchar(255)                         null,
    created_at  datetime   default CURRENT_TIMESTAMP null,
    updated_at  datetime                             null on update CURRENT_TIMESTAMP,
    deleted_at  datetime                             null,
    is_deleted  tinyint(1) default 0                 not null,
    constraint slug
        unique (slug)
);

create table org_team_members
(
    id        binary(16)
        primary key,
    team_id   binary(16)                         not null,
    user_id   binary(16)                         not null,
    role_id   bigint                         not null,
    joined_at datetime default CURRENT_TIMESTAMP null,
    constraint uq_org_team_user
        unique (team_id, user_id),
    constraint fk_org_tm_team
        foreign key (team_id) references org_teams (id)
            on delete cascade
);

create table org_workspaces
(
    id         binary(16) primary key,
    created_by binary(16)                             not null,
    deleted_by binary(16)                             null,
    updated_by binary(16)                             null,
    team_id    binary(16)                             not null,
    name       varchar(255)                       null,
    status     varchar(255)                       null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    deleted_at datetime                           null,
    is_deleted  tinyint(1) default 0                 not null,
    constraint fk_org_workspace_team
        foreign key (team_id) references org_teams (id)
            on delete cascade
);

create table org_apartments
(
    id             binary(16) primary key,
    name           varchar(255)                       null,
    created_by     binary(16)                             not null,
    updated_by     binary(16)                             null,
    deleted_by     binary(16)                             null,
    workspace_id   binary(16)                             not null,
    owner_id       binary(16)                             not null,
    team_id        binary(16)                             not null,
    description    varchar(255)                       null,
    background_url varchar(255)                       null,
    created_at     datetime default CURRENT_TIMESTAMP null,
    updated_at     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    deleted_at     datetime                           null,
    is_deleted  tinyint(1) default 0                 not null,
    constraint fk_org_ap_team
        foreign key (team_id) references org_teams (id)
            on delete cascade,
    constraint fk_org_ap_workspace
        foreign key (workspace_id) references org_workspaces (id)
);

create table org_apartment_members
(
    id           binary(16)                             not null
        primary key,
    apartment_id binary(16)                             not null,
    user_id      binary(16)                             not null,
    role_id      bigint                                 null,
    is_owner     tinyint(1)                             null,
    status       enum ('ACTIVE', 'PENDING', 'REJECTED') null,
    joined_at    datetime   default CURRENT_TIMESTAMP   null,
    created_at   datetime   default (now())             null,
    updated_at   datetime                               null,
    is_deleted   tinyint(1) default 0                   not null,
    constraint fk_org_apm_apartment
        foreign key (apartment_id) references org_apartments (id)
            on delete cascade
);

create table work_boards
(
    id           binary(16)
        primary key,
    workspace_id binary(16)                                                        not null,
    created_by   binary(16)                                                        null,
    updated_by   binary(16)                                                        null,
    deleted_by   binary(16)                                                        null,
    name         varchar(255)                                                      not null,
    type         enum ('PUBLIC', 'PRIVATE', 'SHAREABLE') default 'PUBLIC'          null,
    purpose      varchar(255)                                                      null,
    description  varchar(255)                                                      null,
    is_deleted   tinyint(1)                              default 0                 not null,
    created_at   datetime                                default CURRENT_TIMESTAMP null,
    updated_at   datetime                                default (now())           null on update CURRENT_TIMESTAMP,
    deleted_at   datetime                                                          null,
    team_id      binary(16)                                                        not null
)
    collate = utf8mb4_unicode_ci;

create index idx_board_team
    on work_boards (team_id);

create index idx_board_workspace
    on work_boards (workspace_id);

create table work_board_columns
(
    id          bigint unsigned auto_increment
        primary key,
    board_id    binary(16)                                                                                not null,
    workspace_id    binary(16)                                                                              not null,
    team_id    binary(16)                                                                             not null,
    created_by  binary(16)                                                                                           null,
    deleted_by  binary(16)                                                                                           null,
    archived_by binary(16)                                                                                           null,
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
    constraint fk_work_bc_board
        foreign key (board_id) references work_boards (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_bc_board
    on work_board_columns (board_id);

create table work_board_members
(
    id         bigint auto_increment
        primary key,
    board_id   binary(16)                         not null,
    user_id    binary(16)                         not null,
    role_id    bigint                             not null,
    created_by binary(16)                         not null,
    updated_by binary(16)                         null,
    deleted_by binary(16)                         null,
    joined_at  datetime default CURRENT_TIMESTAMP null,
    created_at datetime default CURRENT_TIMESTAMP null,
    updated_at datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    deleted_at datetime default CURRENT_TIMESTAMP null,
    constraint fk_work_bm_board
        foreign key (board_id) references work_boards (id)
            on delete cascade
);

# create table folders
# (
#     id           bigint auto_increment
#         primary key,
#     created_by   bigint                             not null,
#     deleted_by   bigint                             not null,
#     archived_by  bigint                             not null,
#     workspace_id bigint                             not null,
#     name         varchar(255)                       null,
#     slug         varchar(255)                       null,
#     created_at   datetime default CURRENT_TIMESTAMP null,
#     updated_at   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
#     deleted_at   datetime default CURRENT_TIMESTAMP null,
#     archived_at  datetime default CURRENT_TIMESTAMP null,
#     constraint slug
#         unique (slug),
#     constraint fk_folder_archived_by
#         foreign key (archived_by) references users (id),
#     constraint fk_folder_created_by
#         foreign key (created_by) references users (id),
#     constraint fk_folder_deleted_by
#         foreign key (deleted_by) references users (id),
#     constraint fk_folder_workspace
#         foreign key (workspace_id) references workspaces (id)
#             on delete cascade
# );

create table work_task_groups
(
    id           binary(16)
        primary key,
    board_id     binary(16)                             not null,
    created_by   binary(16)                             null,
    updated_by   binary(16)                             null,
    deleted_by   binary(16)                             null,
    archived_by  binary(16)                             null,
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
    workspace_id binary(16)                             not null,
    team_id      binary(16)                             not null,
    constraint fk_work_bg_board
        foreign key (board_id) references work_boards (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_btg_board
    on work_task_groups (board_id);

create table work_items
(
    id           binary(16)
        primary key,
    task_group_id     binary(16)                      not null,
    board_id     binary(16)                    not null,
    created_by   binary(16)                           null,
    updated_by   binary(16)                           null,
    deleted_by   binary(16)                           null,
    archived_by  binary(16)                           null,
    name         varchar(255)                         not null,
    position     double                               not null,
    is_deleted   tinyint(1) default 0                 not null,
    is_archived  tinyint(1) default 0                 not null,
    created_at   datetime   default CURRENT_TIMESTAMP null,
    updated_at   datetime   default (now())           null on update CURRENT_TIMESTAMP,
    deleted_at   datetime                             null,
    archived_at  datetime                             null,
    workspace_id binary(16)                           not null,
    team_id      binary(16)                           not null,
    constraint fk_work_item_board
        foreign key (board_id) references work_boards (id)
            on delete cascade,
    constraint fk_work_item_group
        foreign key (task_group_id) references work_task_groups (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_items_board
    on work_items (board_id);

create index idx_items_task_group
    on work_items (task_group_id);

create table work_column_values
(
    id           binary(16)
        primary key,
    item_id      binary(16)                      not null,
    column_id    bigint unsigned                      not null,
    board_id     binary(16)                           not null,
    created_by   binary(16)                           null,
    updated_by   binary(16)                           null,
    deleted_by   binary(16)                           null,
    value        varchar(255)                         null,
    text_value   varchar(255)                         null,
    color        varchar(255)                         null,
    type         varchar(255)                         null,
    is_deleted   tinyint(1) default 0                 not null,
    created_at   datetime   default CURRENT_TIMESTAMP null,
    updated_at   datetime   default (now())           null on update CURRENT_TIMESTAMP,
    deleted_at   datetime                             null,
    workspace_id binary(16)                           not null,
    team_id      binary(16)                           not null,
    task_group_id     binary(16)                      not null,
    constraint unique_cell_work
        unique (item_id, column_id),
    constraint fk_work_cv_board
        foreign key (board_id) references work_boards (id)
            on delete cascade,
    constraint fk_work_cv_column
        foreign key (column_id) references work_board_columns (id)
            on delete cascade,
    constraint fk_work_cv_item
        foreign key (item_id) references work_items (id)
            on delete cascade,
    constraint fk_work_cv_task_group
        foreign key (task_group_id) references work_task_groups (id)
            on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index idx_column_values_task_group
    on work_column_values (task_group_id);

create index idx_column_values_board
    on work_column_values (board_id);

create index idx_column_values_column
    on work_column_values (column_id);

create index idx_column_values_item
    on work_column_values (item_id);

# create table user_medias
# (
#     user_id  bigint not null,
#     media_id bigint not null,
#     constraint fk_user_media_media
#         foreign key (media_id) references media (id),
#     constraint fk_user_media_user
#         foreign key (user_id) references users (id)
# );

DELIMITER $$
CREATE PROCEDURE SeedOneThousandBoards()
BEGIN
    DECLARE i INT DEFAULT 1;
    -- Dùng VARCHAR(36) để khai báo biến, ta sẽ convert nó ở lệnh INSERT
    DECLARE target_org_workspace_id VARCHAR(36) DEFAULT '76253820-ed31-4c40-bef9-d623a8606901';

    -- Tắt tự động commit để chạy cực nhanh
    SET autocommit = 0;

    WHILE i <= 1000000 DO
            INSERT INTO work_boards (id, workspace_id, name, created_at)
            VALUES (
                       i + 10,
                       UUID_TO_BIN(target_org_workspace_id),
                       CONCAT('Test Board ', i + 10),
                       NOW()
                   );

            -- Commit mỗi 10,000 dòng để không tràn bộ nhớ DB
            IF i % 10000 = 0 THEN
                COMMIT;
            END IF;

            SET i = i + 1;
        END WHILE;

    COMMIT;
    SET autocommit = 1;
END$$
DELIMITER ;

ALTER TABLE work_boards ADD COLUMN team_id BINARY(16) not null;
CREATE INDEX idx_board_team ON work_boards (team_id);