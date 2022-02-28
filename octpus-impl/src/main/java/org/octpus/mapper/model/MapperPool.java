package org.octpus.mapper.model;

import lombok.Data;
import org.octpus.mapper.MapManager;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class MapperPool {
    private Map<String,MapManager> maps;

    public MapperPool(){
        maps = new LinkedHashMap<>();
    }

    public void addMapper(MapManager mapper){
        maps.put(mapper.getMapper().getMid(),mapper);
    }

    public MapManager getMapper(String mid){
        return maps.get(mid);
    }
}
