CREATE TABLE outbox_events (
   id             UUID         PRIMARY KEY,
   aggregate_type VARCHAR(64)  NOT NULL,
   aggregate_id   UUID         NOT NULL,
   event_type     VARCHAR(128) NOT NULL,
   topic          VARCHAR(255) NOT NULL,
   payload        BYTEA        NOT NULL,
   status         VARCHAR(16)  NOT NULL DEFAULT 'NEW',
   attempts       INT          NOT NULL DEFAULT 0,
   last_error     TEXT,
   created_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
   published_at   TIMESTAMPTZ,
   CONSTRAINT outbox_events_status_check CHECK (status IN ('NEW', 'PUBLISHED', 'FAILED'))
);

-- частичный индекс: публикатор ищет только неотправленные события
CREATE INDEX idx_outbox_events_new ON outbox_events (created_at) WHERE status = 'NEW';
