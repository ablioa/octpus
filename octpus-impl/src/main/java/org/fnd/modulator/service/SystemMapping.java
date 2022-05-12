package org.fnd.modulator.service;

import lombok.Data;
import org.fnd.modulator.map.node.MapModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 系统映射
 * @author wangzh
 */
@Data
public class SystemMapping {
    private Map<String, MapModel> maps;

    public SystemMapping(){
        maps = new LinkedHashMap<>();
    }

    public void addMapper(MapModel mapper){
        maps.put(mapper.getConfiguration().getMid(),mapper);
    }

    public MapModel getMapper(String mid){
        return maps.get(mid);
    }
}
