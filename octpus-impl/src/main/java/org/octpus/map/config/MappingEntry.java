package org.octpus.map.config;

import lombok.Data;

import java.util.List;

/**
 * 映射对象
 * @author wangzh
 */
@Data
public class MappingEntry {
    private String domainType;

    private List<MapItem> items;
}
