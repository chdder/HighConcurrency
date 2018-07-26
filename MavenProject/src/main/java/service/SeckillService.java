package service;

import dto.Expose;
import dto.seckillExecution;
import entity.seckill;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;

import java.util.List;

public interface SeckillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    seckill getById(long seckillId);

    /**
     * 秒杀开始时输出秒杀地址
     * 否则输出系统时间和秒杀开始时间
     * @param seckillId
     */
    Expose exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    seckillExecution executeSeckill(long seckillId, String userPhone, String md5)
            throws SeckillException, SeckillCloseException, RepeatKillException;

    /**
     * 执行秒杀操作 by MySQL存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws SeckillCloseException
     * @throws RepeatKillException
     */
    seckillExecution executeSeckillProcedure(long seckillId, String userPhone, String md5);
}
