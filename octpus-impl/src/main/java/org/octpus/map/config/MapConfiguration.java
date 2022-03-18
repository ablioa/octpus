package org.octpus.map.config;

import lombok.Data;

/**
 * 模型映射配置
 * @author wangzh
 */
@Data
public class MapConfiguration {
    private String mid;

    private String version;

    private String description;

    private MappingEntry subjects;

    private MappingEntry objects;
}
