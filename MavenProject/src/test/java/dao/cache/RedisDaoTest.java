package dao.cache;

import dao.seckillDao;
import entity.seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    private long id = 1L;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private seckillDao s;

    @Test
    public void testGetSeckill() throws Exception{
        // get and put
        seckill ss = redisDao.getSeckill(id);
        if (ss == null) {
            ss = s.queryById(id);
            if (ss != null) {
                String result = redisDao.putSeckill(ss);
                System.out.println(result);
                ss = redisDao.getSeckill(id);
                System.out.println(ss);
            }
        }
    }

    @Test
    public void putSeckill() {
    }
}