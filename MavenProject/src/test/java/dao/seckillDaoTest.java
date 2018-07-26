package dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import entity.seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class seckillDaoTest {

    // 注入Dao实现类依赖
    @Resource
    private seckillDao seckillDaos;

    @Test
    public void testQueryById() {
        long id = 1;
        seckill s = seckillDaos.queryById(id);
        System.out.println(s.getName());
        System.out.println(s);
    }

    @Test
    public void testReduceNumber() {
        Date date = new Date();
        int updateCount = seckillDaos.reduceNumber(1L, date);
        System.out.println(updateCount);
    }

    @Test
    public void testQueryAll() {
        List<seckill> seckillList = seckillDaos.queryAll(0, 100);
        for (seckill s : seckillList) {
            System.out.println(s);
        }
    }
}
