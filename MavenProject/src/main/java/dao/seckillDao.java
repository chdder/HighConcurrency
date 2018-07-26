package dao;

import entity.seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface seckillDao {
    /**
     * 减库存
     */
    int reduceNumber(@Param("id") long id, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     */
    seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * 加上 @Param 是因为Java没有保存形参的记录
     */
    List<seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);
}
