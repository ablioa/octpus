package org.octpus.map.config;

import lombok.Data;

/**
 * 映射字段定义
 * @author wangzh
 */
@Data
public class MapItem {
    private String path;

    private String mid;

    private MapConverter converter;
    // TODO 添加转换规则，映射脚本，聚合函数等。
}
