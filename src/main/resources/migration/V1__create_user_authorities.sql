CREATE TABLE IF NOT EXISTS hr_user_authority (
    id BIGINT NOT NULL  PRIMARY KEY ,
    name VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_hr_user FOREIGN KEY (user_id) REFERENCES hr_user(id)
)