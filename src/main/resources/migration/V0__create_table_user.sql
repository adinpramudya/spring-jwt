CREATE TABLE IF NOT EXISTS hr_user (
    id BIGINT PRIMARY KEY  NOT NULL,
    login VARCHAR(50),
    email VARCHAR(50),
    password_hash TEXT,
    is_active BOOLEAN NOT NULL ,
    created_date TIMESTAMP WITHOUT TIME ZONE ,
    created_by VARCHAR(50),
    last_modified_date TIMESTAMP WITHOUT TIME ZONE ,
    last_modified_by VARCHAR(50)
);