--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'TASKSTACK_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('TASKSTACK_MANAGEMENT',1);