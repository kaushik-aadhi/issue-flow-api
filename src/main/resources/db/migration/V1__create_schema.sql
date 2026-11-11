CREATE TABLE app_users (
 id BIGSERIAL PRIMARY KEY,
 full_name VARCHAR(120) NOT NULL,
 email VARCHAR(180) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE',
 created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT chk_user_role CHECK (role IN ('ADMIN','MANAGER','EMPLOYEE'))
);
CREATE TABLE tickets (
 id BIGSERIAL PRIMARY KEY,
 title VARCHAR(180) NOT NULL,
 description TEXT NOT NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
 priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
 created_by BIGINT NOT NULL REFERENCES app_users(id),
 assignee_id BIGINT REFERENCES app_users(id),
 created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
 updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CONSTRAINT chk_ticket_status CHECK (status IN ('OPEN','IN_PROGRESS','RESOLVED','CLOSED')),
 CONSTRAINT chk_ticket_priority CHECK (priority IN ('LOW','MEDIUM','HIGH','URGENT'))
);
CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_tickets_assignee ON tickets(assignee_id);
CREATE INDEX idx_tickets_created_by ON tickets(created_by);
CREATE TABLE ticket_comments (
 id BIGSERIAL PRIMARY KEY,
 ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
 author_id BIGINT NOT NULL REFERENCES app_users(id),
 body VARCHAR(3000) NOT NULL,
 created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_comments_ticket ON ticket_comments(ticket_id, created_at);
