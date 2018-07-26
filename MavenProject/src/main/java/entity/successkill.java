package entity;

import java.util.Date;

public class successkill {
    private long seckillId;
    private String userPhone;
    private short state;
    private Date createTime;
    private seckill s;

    public seckill getS() {
        return s;
    }

    public void setS(seckill s) {
        this.s = s;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "successkill{" +
                "seckillId=" + seckillId +
                ", userPhone='" + userPhone + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", s=" + s +
                '}';
    }
}
