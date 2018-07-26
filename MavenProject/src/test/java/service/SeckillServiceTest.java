package service;

import dto.Expose;
import dto.seckillExecution;
import entity.seckill;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})

public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() {
        List<seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}", seckillList);
    }

    @Test
    public void testGetById() {
        long id = 1;
        seckill s = seckillService.getById(id);
        logger.info("seckId={}", s);
    }

    @Test
    public void testExportSeckillUrl() {
        long id = 1;
        Expose expose = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", expose);
    }

    @Test
    public void executeSeckill() {
    }

    @Test
    public void testSeckillLogic() {
        long id = 1;
        Expose expose = seckillService.exportSeckillUrl(id);
        if (expose.isExposed()) {
            logger.info("expose={}", expose);
            String phoneNumber = "12345678901";
            String md5 = expose.getMd5();
            try {
                seckillExecution executions = seckillService.executeSeckill(id, phoneNumber, md5);
                logger.info("result={}", executions);
            } catch (RepeatKillException e) {
                logger.info(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.info(e.getMessage());
            }
        } else {
            logger.warn("expose={}", expose);
        }
    }

    @Test
    public void testExecuteSeckillProcedure() {
        long seckillId = 1L;
        String phone = "18894267473";
        Expose expose = seckillService.exportSeckillUrl(seckillId);
        if(expose.isExposed()) {
            String md5 = expose.getMd5();
            seckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }

    @Test
    public void testRandom() {
        System.out.println((int)((Math.random()*9+1)*100));
    }
}