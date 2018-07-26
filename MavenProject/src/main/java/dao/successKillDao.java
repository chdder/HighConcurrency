package dao;

import entity.successkill;
import org.apache.ibatis.annotations.Param;

public interface successKillDao {
    /**
     * 插入购买明细，可过滤重复记录
     * @param secKillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("secKillId") long secKillId, @Param("userPhone") String userPhone);

    /**
     *
     * @param seckillId
     * @return
     */
    successkill queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") String userPhone);
}
