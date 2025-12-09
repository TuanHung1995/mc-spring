-- Thiết lập Charset chuẩn để hỗ trợ Emoji và đa ngôn ngữ
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- Tạm tắt check khóa ngoại để tạo bảng dễ hơn

-- ==============================================================
-- PHẦN 1: QUẢN LÝ NGƯỜI DÙNG & TỔ CHỨC (IAM)
-- ==============================================================

-- 1. Users: Người dùng hệ thống
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `email`       VARCHAR(255)    NOT NULL,
    `password`    VARCHAR(255)                         DEFAULT NULL,
    `full_name`   VARCHAR(100)    NOT NULL,
    `avatar_url`  VARCHAR(512),
    `provider`    ENUM ('LOCAL', 'GOOGLE', 'GITHUB')   DEFAULT 'LOCAL',
    `provider_id` VARCHAR(255)                         DEFAULT NULL,
    `job_title`   VARCHAR(255)                         DEFAULT NULL,
    `phone`       VARCHAR(255),
    `address`     VARCHAR(255),
    `birthday`    DATE,

    `status`      ENUM ('ACTIVE', 'PENDING', 'LOCKED') DEFAULT 'ACTIVE',
    `created_at`  DATETIME                             DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 2. Teams: Nhóm người dùng (VD: Dev Team, Marketing)
DROP TABLE IF EXISTS `teams`;
CREATE TABLE `teams`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100)    NOT NULL,
    `description` TEXT,
    `avatar_url`  TEXT,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 3. Team Members: Liên kết User - Team (N-N)
DROP TABLE IF EXISTS `team_members`;
CREATE TABLE `team_members`
(
    `team_id`   BIGINT UNSIGNED NOT NULL,
    `user_id`   BIGINT UNSIGNED NOT NULL,
    `role`      ENUM ('LEAD', 'MEMBER') DEFAULT 'MEMBER',
    `joined_at` DATETIME                DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`team_id`, `user_id`),
    CONSTRAINT `fk_tm_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tm_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ==============================================================
-- PHẦN 2: CẤU TRÚC WORKSPACE & BOARD
-- ==============================================================

-- 4. Workspaces: Không gian làm việc (Container cao nhất)
DROP TABLE IF EXISTS `workspaces`;
CREATE TABLE `workspaces`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(100)    NOT NULL,
    `description` TEXT,
    `type`        ENUM ('OPEN', 'CLOSED') DEFAULT 'OPEN',
    `owner_id`    BIGINT UNSIGNED NOT NULL,
    `created_at`  DATETIME                DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_ws_owner` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 5. Workspace Members: Phân quyền truy cập Workspace
DROP TABLE IF EXISTS `workspace_members`;
CREATE TABLE `workspace_members`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT,
    `workspace_id` BIGINT  ,
    `user_id`      BIGINT  ,
    `role`         ENUM ('ADMIN', 'MEMBER', 'VIEWER') DEFAULT 'MEMBER',
    `joined_at`    DATETIME                           DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_wsm_workspace` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_wsm_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 6. Boards: Bảng quản lý công việc
DROP TABLE IF EXISTS `boards`;
CREATE TABLE `boards`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `workspace_id` BIGINT          NOT NULL,
    `created_by`   BIGINT          NOT NULL ,
    `deleted_by`   BIGINT,
    `name`         VARCHAR(255)    NOT NULL,
    `description`  TEXT,
    `type`         ENUM ('PUBLIC', 'PRIVATE', 'SHAREABLE') DEFAULT 'PUBLIC',
    `created_at`   DATETIME                                DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   DATETIME                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at`   DATETIME        NULL, -- Soft Delete
    PRIMARY KEY (`id`),
    KEY `idx_workspace` (`workspace_id`),
    CONSTRAINT `fk_board_ws` FOREIGN KEY (`workspace_id`) REFERENCES `workspaces` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_board_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_board_deleter` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ==============================================================
-- PHẦN 3: CẤU TRÚC DỮ LIỆU ĐỘNG (Dynamic Structure)
-- ==============================================================

-- 7. Board Groups: Nhóm các task (VD: To Do, Doing, Done)
DROP TABLE IF EXISTS `task_groups`;
CREATE TABLE `task_groups`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `board_id`     BIGINT UNSIGNED NOT NULL,
    `title`        VARCHAR(255)    NOT NULL DEFAULT 'New Group',
    `color`        VARCHAR(7)               DEFAULT '#579bfc',
    `position`     DOUBLE          NOT NULL DEFAULT 65535, -- Dùng số thực để hỗ trợ Drag & Drop chèn giữa
    `is_collapsed` BOOLEAN                  DEFAULT FALSE,
    `created_at`   DATETIME                 DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_board_groups` (`board_id`),
    CONSTRAINT `fk_bg_board` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 8. Board Columns: Định nghĩa các cột trong Board
DROP TABLE IF EXISTS `board_columns`;
CREATE TABLE `board_columns`
(
    `id`          BIGINT UNSIGNED                                                                                 NOT NULL AUTO_INCREMENT,
    `board_id`    BIGINT UNSIGNED                                                                                 NOT NULL,
    `created_by`  BIGINT ,
    `title`       VARCHAR(255)                                                                                    NOT NULL,
    -- Các loại cột được hỗ trợ
    `type`        ENUM ('TEXT', 'STATUS', 'DATE', 'PERSON', 'NUMBER', 'TIMELINE', 'CHECKBOX', 'LINK', 'DROPDOWN') NOT NULL,
    -- Settings lưu JSON: VD cột Status lưu {"labels": {1: "Done", 2: "Stuck"}}
    `settings`    JSON     DEFAULT NULL,
    `description` VARCHAR(255),
    `position`    DOUBLE                                                                                          NOT NULL,
    `width`       INT      DEFAULT 150,
    `is_hidden`   BOOLEAN  DEFAULT FALSE,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_board_columns` (`board_id`),
    CONSTRAINT `fk_bc_board` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_bc_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 9. Items (Tasks/Rows): Các dòng dữ liệu
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `group_id`   BIGINT UNSIGNED NOT NULL,
    `board_id`   BIGINT UNSIGNED NOT NULL, -- Denormalization để query nhanh
    `name`       VARCHAR(255)    NOT NULL, -- Tên Task (Cột Name mặc định)
    `created_by` BIGINT,
    `deleted_by` BIGINT  DEFAULT NULL,
    `position`   DOUBLE          NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted_at` DATETIME,
    PRIMARY KEY (`id`),
    KEY `idx_items_group` (`group_id`),
    KEY `idx_items_board` (`board_id`),
    KEY `idx_items_created_by` (`created_by`),
    KEY `idx_items_deleted_by` (`deleted_by`),
    CONSTRAINT `fk_item_creator` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_item_deleted_by` FOREIGN KEY (`deleted_by`) REFERENCES `users` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_item_group` FOREIGN KEY (`group_id`) REFERENCES `task_groups` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_item_board` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 10. Column Values: Giá trị của từng ô (Cell)
-- Đây là bảng quan trọng nhất để lưu dữ liệu động
DROP TABLE IF EXISTS `column_values`;
CREATE TABLE `column_values`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `item_id`    BIGINT UNSIGNED NOT NULL,
    `column_id`  BIGINT UNSIGNED NOT NULL,
    `board_id`   BIGINT UNSIGNED NOT NULL,             -- Denormalization để hỗ trợ Partitioning sau này nếu cần

    -- Giá trị JSON linh hoạt. VD:
    -- Text: "Hello"
    -- Status: {"index": 1}
    -- Person: {"personsAndTeams": [{"id": 1, "kind": "person"}]}
    `value`      JSON     DEFAULT NULL,

    -- Giá trị text thuần túy để phục vụ Search/Sort/Filter nhanh
    `text_value` TEXT,

    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_cell` (`item_id`, `column_id`), -- Một ô chỉ có 1 giá trị
    KEY `idx_cv_column` (`column_id`),
    CONSTRAINT `fk_cv_board` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cv_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cv_column` FOREIGN KEY (`column_id`) REFERENCES `board_columns` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ==============================================================
-- PHẦN 4: LOG & GIAO TIẾP
-- ==============================================================

-- 11. Activity Logs: Lịch sử hoạt động
DROP TABLE IF EXISTS `activity_logs`;
CREATE TABLE `activity_logs`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `workspace_id`   BIGINT UNSIGNED NOT NULL,
    `board_id`       BIGINT UNSIGNED,
    `item_id`        BIGINT UNSIGNED,
    `user_id`        BIGINT UNSIGNED NOT NULL,
    `action_type`    VARCHAR(50)     NOT NULL, -- CREATE_ITEM, UPDATE_STATUS, DELETE_COLUMN
    `entity_type`    VARCHAR(50)     NOT NULL, -- ITEM, COLUMN, BOARD
    `entity_id`      BIGINT UNSIGNED NOT NULL,
    `previous_value` JSON,                     -- Giá trị trước khi sửa
    `new_value`      JSON,                     -- Giá trị sau khi sửa
    `created_at`     DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_log_board` (`board_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE refresh_tokens
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     BIGINT       NOT NULL,
    expiry_date DATETIME     NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);