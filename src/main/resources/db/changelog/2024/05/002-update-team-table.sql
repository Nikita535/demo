ALTER TABLE IF EXISTS match_team
    ADD COLUMN leader_id INT;

ALTER TABLE IF EXISTS match_team
    ADD CONSTRAINT fk_leader
        FOREIGN KEY (leader_id)
            REFERENCES app_user(id)