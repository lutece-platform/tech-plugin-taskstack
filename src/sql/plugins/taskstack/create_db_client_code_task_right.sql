DROP TABLE IF EXISTS identitystore_client_code_task_right;
CREATE TABLE identitystore_client_code_task_right
(
    authorized_client_code      VARCHAR(255) NOT NULL,
    grantee_client_code   VARCHAR(255) NOT NULL,
    task_type            VARCHAR(50)  NOT NULL,
    CONSTRAINT identitystore_client_code_task_right_pkey PRIMARY KEY (authorized_client_code, grantee_client_code, task_type)
);
