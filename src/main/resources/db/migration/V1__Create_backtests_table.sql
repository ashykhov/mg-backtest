CREATE TABLE IF NOT EXISTS backtests (
    id VARCHAR(255) PRIMARY KEY,
    bqet_bs JSONB NOT NULL,
    bt_type VARCHAR(50) NOT NULL,
    bt_params JSONB NOT NULL,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP,
    config JSONB NOT NULL,
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL
);

-- Индексы для быстрого поиска
CREATE INDEX idx_backtests_status ON backtests(status);
CREATE INDEX idx_backtests_started_at ON backtests(started_at);
CREATE INDEX idx_backtests_bt_type ON backtests(bt_type);