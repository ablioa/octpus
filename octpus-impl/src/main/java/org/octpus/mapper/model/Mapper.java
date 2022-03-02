package org.octpus.mapper.model;

import lombok.Data;

@Data
public class Mapper {
    private String mid;

    private String version;

    private String description;

//    private List<MapItem> subject;
//
//    private List<MapItem> object;

    private MappingEntry subject;

    private MappingEntry object;
}
