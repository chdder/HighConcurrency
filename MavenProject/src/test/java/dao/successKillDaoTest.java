package dao;

import entity.successkill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class successKillDaoTest {

    // 注入Dao实现类依赖
    @Resource
    private successKillDao successKillDaos;

    @Test
    public void testInsertSuccessKilled() {
        long id = 2L;
        String phone = "15119547372";
        int insertCount = successKillDaos.insertSuccessKilled(id, phone);
        System.out.println(insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill() {
        long id = 2L;
        String phoneNumber = "15119547372";
        successkill successkills = successKillDaos.queryByIdWithSeckill(id, phoneNumber);
        System.out.println(successkills);
        System.out.println(successkills.getS());
    }
}