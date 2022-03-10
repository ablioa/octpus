package org.octpus.map.config;

import lombok.Data;

@Data
public class MapItem {
    private String path;

    private String mid;

    // TODO 添加转换规则，映射脚本，聚合函数等。
}
