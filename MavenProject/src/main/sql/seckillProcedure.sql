-- 秒杀执行存储过程
-- 将";"号换为"$$"
DELIMITER $$
-- 定义存储过程
CREATE PROCEDURE `seckill`.`execute_seckill`
(in v_seckill_id bigint,in v_phone VARCHAR(120) ,in v_kill_time TIMESTAMP,out r_result int)
BEGIN
  DECLARE insert_count int DEFAULT 0;
  start TRANSACTION;
  INSERT ignore INTO success_killed(seckill_id,user_phone,create_time)
  VALUES (v_seckill_id,v_phone,v_kill_time);
  SELECT ROW_COUNT() into insert_count;
  IF (insert_count = 0) THEN
    ROLLBACK;
    set r_result = -1;
  ELSEIF (insert_count < 0) THEN
    ROLLBACK;
    set r_result = -2;
  ELSE
    UPDATE seckill_table
    set number = number - 1
    WHERE id = v_seckill_id
      and end_time > v_kill_time
      and start_time < v_kill_time
      and number > 0;
    SELECT row_count() into insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK;
      set r_result = 0;
    ELSEIF (insert_count < 0) THEN
      ROLLBACK;
      set r_result = -2;
    ELSE
      COMMIT;
      set r_result = 1;
    END IF;
  END IF;
END;
$$
-- 存储过程定义结束

DELIMITER ;
-- 定义变量
set @r_result = -3;
-- 执行存储过程
call execute_seckill(2,'18814267473',now(),@r_result);
-- 获取结果
SELECT @r_result;

-- 使用存储过程优化的是：事务行级锁持有的时间
-- 不能过度依赖存储过程
-- 简单的逻辑可以应用存储过程
-- 使用存储过程的qps：6000

