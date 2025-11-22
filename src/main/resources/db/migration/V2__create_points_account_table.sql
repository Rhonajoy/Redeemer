CREATE TABLE points_account (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                type VARCHAR(50),
                                amount_points INT,
                                points_balance INT,
                                created_at TIMESTAMP NOT NULL,
                                CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id)
);
