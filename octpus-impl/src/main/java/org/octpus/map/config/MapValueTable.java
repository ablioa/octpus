package org.octpus.map.config;

import lombok.Data;

import java.util.Map;

@Data
public class MapValueTable {
    private Map<String,String> table;
    private String defaultValue;
}
