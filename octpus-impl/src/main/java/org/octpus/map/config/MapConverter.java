package org.octpus.map.config;

import lombok.Data;

/**
 * 字段映射配置
 * @author wangzh
 */
@Data
public class MapConverter {
    private String method;

    private String groovy;

    private MapValueTable table;

    public MapConverter(String data){
    }
}
