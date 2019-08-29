package com.md1k.services.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vvk
 * @since 2018/11/23
 */
public class SimpleMapBuilder<K,V>{

    private Map<K,V> map = new HashMap<>();

    public Map<K,V> build(){
        return this.map;
    }

    public SimpleMapBuilder<K,V> put(K key, V val){
        this.map.put(key,val);
        return this;
    }
}
