CREATE TABLE points_balance (
                                id BIGSERIAL PRIMARY KEY,
                                available_points BIGINT,
                                points_account_id BIGINT,
                                CONSTRAINT fk_points_balance_account FOREIGN KEY(points_account_id)
                                    REFERENCES points_account(id)
                                    ON DELETE CASCADE
);
