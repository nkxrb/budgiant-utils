package org.budgiant.common.cache;

/**
 * 自建本地缓存机制，功能尚未完善
 * 1.有效时间--每个缓存对象都有创建时间，每次去去对象时，与当前时间进行比对，若超出设置的有效时间，则返回空对象，并清空该缓存对象
 * 2.永久对象--不受有效时间影响，仅在每次重启系统或系统主动修改的情况下发生变动
 *
 * @author nkxrb
 * @since 2019/5/14
 */
public class CacheConfig {
    /**
     * 有效时间
     **/
    private Long effectiveTime;
    /**
     * eternal：true表示对象永不过期，此时会忽略timeToIdleSeconds和timeToLiveSeconds属性，默认为false
     **/
    private boolean eternal;

    public Long getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Long effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public boolean isEternal() {
        return eternal;
    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }
}
