--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'TASKSTACK_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('TASKSTACK_MANAGEMENT',1);

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'TASKSTACK_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES
    ('TASKSTACK_MANAGEMENT','taskstack.title',1,'jsp/admin/plugins/taskstack/TaskSearch.jsp','taskstack.plugin.description',0,'taskstack',NULL,NULL,NULL,1);