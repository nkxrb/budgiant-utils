package org.budgiant.common.cache;

import java.util.Collection;
import java.util.Date;

/**
 * 缓存存储时的基础对象
 *
 * @author nkxrb
 * @since 2019/5/15
 */
public class CacheItem {
    /**
     * 创建时间
     **/
    private Date sysCreated = new Date();
    /**
     * 修改时间
     **/
    private Date sysModified = new Date();
    /**
     * 有效时间（ps:单位为毫秒）
     **/
    private long effectiveTime = 0L;
    /**
     * 缓存对象的值
     **/
    private Object value;
    /**
     * 访问次数
     **/
    private long hitCount = 0L;
    /**
     * 最后一次命中时间
     **/
    private Date lastHitTime = new Date();
    /**
     * 状态：1.默认常态，2.闲置
     **/
    private int status = 1;
    /**
     * 对象值类型是否为集合
     **/
    private boolean isCollection;

    public Date getSysCreated() {
        return sysCreated;
    }

    public void setSysCreated(Date sysCreated) {
        this.sysCreated = sysCreated;
    }

    public Date getSysModified() {
        return sysModified;
    }

    public void setSysModified(Date sysModified) {
        this.sysModified = sysModified;
    }

    public long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * 每次取值时都更新命中状态
     */
    public Object getValue() {
        Date curDate = new Date();
        //判断缓存值是否过期
        if (this.effectiveTime > 0 && (curDate.getTime() - this.sysModified.getTime()) > this.effectiveTime) {
            return null;
        }
        this.setLastHitTime(curDate);
        this.setHitCount(this.getHitCount() + 1);
        this.setSysModified(curDate);
        return value;
    }

    public void setValue(Object value) {
        Date curDate = new Date();
        this.setSysModified(curDate);
        this.setLastHitTime(curDate);
        this.setHitCount(this.getHitCount() + 1);
        this.value = value;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }

    public Date getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(Date lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isCollection() {
        return this.value instanceof Collection;
    }

}
