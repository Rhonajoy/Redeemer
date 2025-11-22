CREATE TABLE wallets (
                         id BIGSERIAL PRIMARY KEY,
                         wallet_balance BIGINT NOT NULL DEFAULT 0,
                         total_redeemed_money BIGINT NOT NULL DEFAULT 0,
                         user_id BIGINT NOT NULL UNIQUE,
                         CONSTRAINT fk_wallet_user FOREIGN KEY(user_id)
                             REFERENCES users(id)
                             ON DELETE CASCADE
);
