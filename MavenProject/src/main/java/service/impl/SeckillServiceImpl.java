package service.impl;

import dao.cache.RedisDao;
import dao.seckillDao;
import dao.successKillDao;
import dto.Expose;
import dto.seckillExecution;
import entity.seckill;
import entity.successkill;
import enums.SeckillStateEnum;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import service.SeckillService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private seckillDao seckillDaos;
    @Autowired
    private successKillDao successKillDaos;
    @Autowired
    private RedisDao redisDao;
    /**
     * 任意字符串，越复杂越好，用以混淆
     * md5原值字符串
     */
    private final String slat = "9n38q45yr(Y(*skdfhskdfbk&*&AFDSAYSBAsv67t624";

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    public List<seckill> getSeckillList() {
        return seckillDaos.queryAll(0, 5);
    }

    @Override
    public seckill getById(long seckillId) {
        return seckillDaos.queryById(seckillId);
    }

    @Override
    public Expose exportSeckillUrl(long seckillId) {
        /**
         * modify：高并发优化 超时的基础上维护一致性
         * redis缓存优化
         */
        // 先通过redis获取对象
        seckill s = redisDao.getSeckill(seckillId);
        // 如果redis缓存中没有该对象
        if (s == null) {
            // 访问数据库获取
            s = seckillDaos.queryById(seckillId);
            if (s == null) {
                return new Expose(false, seckillId);
            } else {
                // 放入redis
                redisDao.putSeckill(s);
            }
        } else {
            System.out.println("从redis中获取对象。");
        }

        Date startTime = s.getStartTime();
        Date endTime = s.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Expose(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // 转换字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Expose(true, md5, seckillId);
    }

    @Override
    @Transactional
    public seckillExecution executeSeckill(long seckillId, String userPhone, String md5) throws SeckillException, SeckillCloseException, RepeatKillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite.");
        }
        // 执行秒杀逻辑，减库存+记录购买行为
        Date nowDate = new Date();
        // 减库存
        int updateCount = seckillDaos.reduceNumber(seckillId, nowDate);
        try {
            if (updateCount <= 0) {
                // 没有购买行为
                throw new SeckillCloseException("seckill is closed.");
            } else {
                // 记录购买行为
                int insertCount = successKillDaos.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated.");
                } else {
                    // 秒杀成功
                    successkill ss = successKillDaos.queryByIdWithSeckill(seckillId, userPhone);
                    return new seckillExecution(seckillId, SeckillStateEnum.SUCCESS, ss);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译器异常转化为运行期异常
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    @Override
    public seckillExecution executeSeckillProcedure(long seckillId, String userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new seckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> stringObjectMap = new HashMap<String, Object>();
        stringObjectMap.put("seckillId", seckillId);
        stringObjectMap.put("phone", userPhone);
        stringObjectMap.put("killTime", killTime);
        stringObjectMap.put("result", null);
        try {
            seckillDaos.killByProcedure(stringObjectMap);
            // 获取result
            int result = MapUtils.getInteger(stringObjectMap, "result", -2);
            if (result == 1) {
                successkill sk = successKillDaos.queryByIdWithSeckill(seckillId, userPhone);
                return new seckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
            } else {
                return new seckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new seckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}
