package dto;

import entity.successkill;
import enums.SeckillStateEnum;

/**
 * 秒杀执行后的结果
 */
public class seckillExecution {

    private long seckillId;
    /**
     * 秒杀执行结果状态
     */
    private int state;
    /**
     * 状态表示
     */
    private String stateInfo;
    /**
     * 秒杀成功对象
     */
    private successkill successKilled;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public successkill getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(successkill successKilled) {
        this.successKilled = successKilled;
    }

    public seckillExecution(long seckillId, SeckillStateEnum stateEnum, successkill successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public seckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    @Override
    public String toString() {
        return "seckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
