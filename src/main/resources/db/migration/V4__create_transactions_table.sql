CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              transaction_reference VARCHAR(255) NOT NULL UNIQUE,
                              amount BIGINT,
                              points_earned INT,
                              points_redeemed INT,
                              type VARCHAR(50),
                              created_at TIMESTAMP,
                              user_id BIGINT NOT NULL,
                              CONSTRAINT fk_transaction_user FOREIGN KEY(user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);
