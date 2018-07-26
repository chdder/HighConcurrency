package web;

import dto.Expose;
import dto.seckillExecution;
import dto.seckillResult;
import entity.seckill;
import enums.SeckillStateEnum;
import exception.RepeatKillException;
import exception.SeckillCloseException;
import exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.SeckillService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") // url:/模块/资源/{id}/细分
public class seckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    /**
     * 输出详情
     *
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        seckill s = seckillService.getById(seckillId);
        if (s == null) {
            // 可以写成 redirect
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", s);
        return "detail";
    }

    /**
     * 用来输出秒杀地址
     *
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public seckillResult<Expose> exposer(@PathVariable Long seckillId) {

        seckillResult<Expose> result;
        try {
            Expose ex = seckillService.exportSeckillUrl(seckillId);
            result = new seckillResult<Expose>(true, ex);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new seckillResult<Expose>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 执行秒杀并检查异常
     *
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public seckillResult<seckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) String userPhone) {
        /**
         * 验证是否登录注册
         */
        if (userPhone == null) {
            return new seckillResult<seckillExecution>(false, "未注册");
        }

        /**
         * 执行秒杀
         */
        try {

            // 改为调用存储过程
//            seckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            seckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            return new seckillResult<seckillExecution>(true, execution);

        } catch (RepeatKillException e) {

            seckillExecution execution = new seckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new seckillResult<seckillExecution>(true, execution);

        } catch (SeckillCloseException e) {

            seckillExecution execution = new seckillExecution(seckillId, SeckillStateEnum.END);
            return new seckillResult<seckillExecution>(true, execution);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            seckillExecution execution = new seckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new seckillResult<seckillExecution>(true, execution);
        }
    }

    /**
     * 执行秒杀并检查异常
     *
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/{phone}/execution",
            method = RequestMethod.POST, produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public seckillResult<seckillExecution> executeTest(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @PathVariable("phone") String userPhone) {
        /**
         * 验证是否登录注册
         */
        if (userPhone == null) {
            return new seckillResult<seckillExecution>(false, "未注册");
        }
        userPhone = (int) ((Math.random() * 9 + 1) * 100) + "" + (int) ((Math.random() * 9 + 1) * 1000) + "" + (int) ((Math.random() * 9 + 1) * 1000);

        /**
         * 执行秒杀
         */
        try {

            // 改为调用存储过程
//            seckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            seckillExecution execution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            return new seckillResult<seckillExecution>(true, execution);

        } catch (RepeatKillException e) {

            seckillExecution execution = new seckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new seckillResult<seckillExecution>(true, execution);

        } catch (SeckillCloseException e) {

            seckillExecution execution = new seckillExecution(seckillId, SeckillStateEnum.END);
            return new seckillResult<seckillExecution>(true, execution);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            seckillExecution execution = new seckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new seckillResult<seckillExecution>(true, execution);
        }
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public seckillResult<Long> time() {
        Date date = new Date();
        return new seckillResult<Long>(true, date.getTime());
    }
}
