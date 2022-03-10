package org.octpus.map.config;

import lombok.Data;

import java.util.List;

@Data
public class MappingEntry {
    private String domainType;

    private List<MapItem> items;
}
