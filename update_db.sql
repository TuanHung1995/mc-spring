/* Update iam_users */
/* Add created_by, updated_by column */
ALTER TABLE iam_users
ADD COLUMN created_by VARCHAR(255) DEFAULT NULL;
ALTER TABLE iam_users
    ADD COLUMN updated_by VARCHAR(255) DEFAULT NULL;

/*  18/11/2026 */
/* Done 12:41 PM (same day) */
/* Add created_at, updated_at, deleted_at column */
ALTER TABLE org_team_members
    ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE org_team_members
    ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE org_team_members
    ADD COLUMN deleted_at DATETIME DEFAULT NULL;
/*  18/11/2026 (done 12:55 PM) */
ALTER TABLE org_team_members
    ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;


