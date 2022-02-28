package org.octpus.mapper.model;

import lombok.Data;

import java.util.List;

@Data
public class Mapper {
    private String mid;

    private String version;

    private String description;

    private List<MapItem> subject;

    private List<MapItem> object;
}
