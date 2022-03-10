package org.octpus.map.config;

import lombok.Data;

@Data
public class MapConverter {
    private String method;

    private String groovy;

    private MapValueTable table;
}
