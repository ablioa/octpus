package org.fnd.modulator.map.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 转码映射定义
 * @author wangzh
 */
@Data
public class MapValueTable {
    private Map<String,String> table;
    private String defaultValue;

    public MapValueTable(){
        table = new HashMap<>();
    }
}