DROP TABLE IF EXISTS stack_task;
CREATE TABLE stack_task
(
    id                      INT AUTO_INCREMENT NOT NULL,
    code                    VARCHAR(255) NOT NULL,
    resource_id             VARCHAR(255) NOT NULL,
    resource_type           VARCHAR(255) NOT NULL,
    type                    VARCHAR(255) NOT NULL,
    creation_date           TIMESTAMP(3) NOT NULL,
    last_update_date        TIMESTAMP(3) NOT NULL,
    last_update_client_code VARCHAR(255) NOT NULL,
    status                  VARCHAR(50)  NOT NULL,
    metadata                JSON DEFAULT NULL,
    CONSTRAINT stack_task_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS stack_task_change;
CREATE TABLE stack_task_change
(
    id          INT AUTO_INCREMENT NOT NULL,
    id_task     INT          NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    author_type VARCHAR(255) NOT NULL,
    client_code VARCHAR(255) NOT NULL,
    status      VARCHAR(50)  NOT NULL,
    change_type VARCHAR(50)  NOT NULL,
    change_date TIMESTAMP(3) NOT NULL,
    CONSTRAINT stack_task_change_pkey PRIMARY KEY (id)
);