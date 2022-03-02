package org.octpus.mapper.model;

import lombok.Data;

import java.util.List;

@Data
public class MappingEntry {
    private String domainType;

    private List<MapItem> items;
}
