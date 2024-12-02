-- INTERVAL BASED ON THE OLD PARAMETER taskstack.task.retention.days.number=182
ALTER TABLE stack_task
ADD COLUMN expiration_date TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP + INTERVAL '182' DAY;

UPDATE stack_task
SET expiration_date = creation_date + INTERVAL '182' DAY;