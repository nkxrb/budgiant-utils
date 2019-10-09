package org.budgiant.common.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 管理缓存</p>
 * 可扩展的功能：对每个缓存对象保存创建时间,设置过期机制
 *
 * @author nkxrb
 * @since 2019/5/14
 */
public class CacheManager {
    //缓存对象（单例）
    private static volatile CacheManager cacheManager = null;
    //声明一个map,用来作为缓存模型
    private static Map<String, CacheItem> map = new HashMap<>();

    private CacheManager() {
        super();
    }

    public static CacheManager getCacheManager(CacheConfig cacheConfig) {
        if (cacheManager == null) {
            synchronized (CacheManager.class) {
                if (cacheManager == null) {
                    cacheManager = new CacheManager();
                }
            }
        }
        return cacheManager;
    }

    /**
     * 获取/更新缓存数据
     *
     * @param key 健
     * @return 值
     */
    public static Object getOrUpdateValue(String key) {
        CacheItem cacheItem = map.get(key);
        if (cacheItem == null) {
            synchronized (CacheManager.class) {
                if (cacheItem == null) {
                    cacheItem = new CacheItem();
                    Object value = "abc";//这里是去数据库查询
                    cacheItem.setValue(value);
                    map.put(key, cacheItem);//将数据放到缓存模型中
                }
            }
        }
        return cacheItem.getValue();
    }

    /**
     * 获取缓存数据
     *
     * @param key 健
     * @return 值
     */
    public static Object getValue(String key) {
        CacheItem cacheItem = map.get(key);
        if (cacheItem == null) {
            return null;
        } else if (cacheItem.getValue() == null) {
            //对于无效数据或过期数据，将进行清空操作
            map.remove(key);
            return null;
        }
        return cacheItem.getValue();
    }

    /**
     * 存储缓存值（会覆盖旧数据）
     *
     * @param key   健
     * @param value 值
     */
    public static void setValue(String key, Object value) {
        CacheItem cacheItem = map.get(key);
        if (cacheItem == null) {
            cacheItem = new CacheItem();
        }
        cacheItem.setValue(value);
        map.put(key, cacheItem);
    }

    /**
     * 存储缓存值（会覆盖旧数据）
     *
     * @param key           健
     * @param value         值
     * @param effectiveTime 有效时间（ps:单位为毫秒）
     */
    public static void setValue(String key, Object value, Long effectiveTime) {
        CacheItem cacheItem = map.get(key);
        if (cacheItem == null) {
            cacheItem = new CacheItem();
        }
        cacheItem.setEffectiveTime(effectiveTime);
        cacheItem.setValue(value);
        map.put(key, cacheItem);
    }

    /**
     * 存储缓存值（不会覆盖旧数据）
     *
     * @param key   健
     * @param value 值
     */
    public static void setValueIfAbsent(String key, Object value) {
        CacheItem cacheItem = map.get(key);
        if (cacheItem == null) {
            cacheItem = new CacheItem();
            cacheItem.setValue(value);
        }
        map.putIfAbsent(key, cacheItem);
    }

    public static void updateValue() {

    }

}
