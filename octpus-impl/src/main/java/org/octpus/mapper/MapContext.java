package org.octpus.mapper;

import lombok.Data;
import org.octpus.core.BaseDynamicRoleData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Data
public class MapContext {
    private MapManager mapManager;

    private Map<String, Integer> indexCounter;

    private BaseDynamicRoleData target;

    private Map<String,Object> nodePath;

    public MapContext(MapManager mapManager){
        this.mapManager = mapManager;
        indexCounter = new HashMap<>();
        target = new BaseDynamicRoleData();
        nodePath = new HashMap<>();
    }

    public Object getNodeValue(String key,int type){
        Object mnode = nodePath.get(key);
        if(mnode == null){
            if(type==0) {
                mnode = new BaseDynamicRoleData();
            }else{
                mnode = new LinkedList<Object>();
                nodePath.put(key,mnode);
            }
        }
        return mnode;
    }
}
