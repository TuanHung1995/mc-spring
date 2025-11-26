-- Tạo bảng Users
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255),
                                     full_name VARCHAR(255),
                                     phone VARCHAR(20),
                                     status ENUM('ACTIVE', 'LOCKED', 'PENDING') DEFAULT 'ACTIVE',
                                     reset_token VARCHAR(255),
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tạo bảng Roles
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert dữ liệu Role mặc định (Nên làm ngay khi init)
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_MEMBER'), ('ROLE_VIEWER'), ('ROLE_GUEST');

-- Tạo bảng Teams
CREATE TABLE IF NOT EXISTS teams (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     description TEXT,
                                     slug VARCHAR(255),
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     created_by BIGINT,
                                     CONSTRAINT fk_teams_created_by FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Tạo bảng User Roles (Bảng trung gian)
CREATE TABLE IF NOT EXISTS user_roles (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          user_id BIGINT NOT NULL,
                                          role_id BIGINT NOT NULL,
                                          team_id BIGINT NOT NULL,
                                          CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                                          CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id),
                                          CONSTRAINT fk_user_roles_team FOREIGN KEY (team_id) REFERENCES teams(id)
);

-- Tạo bảng Workspaces
CREATE TABLE IF NOT EXISTS workspaces (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
                                          status VARCHAR(50) DEFAULT 'ACTIVE',
                                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          updated_at DATETIME,
                                          deleted_at DATETIME,
                                          created_by BIGINT,
                                          deleted_by BIGINT,
                                          team_id BIGINT,
                                          CONSTRAINT fk_workspaces_created_by FOREIGN KEY (created_by) REFERENCES users(id),
                                          CONSTRAINT fk_workspaces_deleted_by FOREIGN KEY (deleted_by) REFERENCES users(id),
                                          CONSTRAINT fk_workspaces_team FOREIGN KEY (team_id) REFERENCES teams(id)
);