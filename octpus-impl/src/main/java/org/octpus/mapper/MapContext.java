package org.octpus.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.octpus.core.BaseDynamicRoleData;
import org.octpus.mapper.node.NodeAttribute;
import org.octpus.mapper.node.NodeType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class MapContext {
    private MapManager mapManager;

    private Map<String, Integer> indexCounter;

    private BaseDynamicRoleData target;

    private Map<String,Object> nodePath;

    private Map<String,Integer> nodeIndex;

    public MapContext(MapManager mapManager){
        this.mapManager = mapManager;

        indexCounter = new HashMap<>();
        target = new BaseDynamicRoleData();
        nodePath = new HashMap<>();

        nodeIndex = new HashMap<>();

        nodePath.put("",target);
    }

    public String getRealCode(String uuid){
        NodeAttribute na = mapManager.getToNodes().getNodeAttribute(uuid);
        String realCode = na.getCode();

        if(na.getNodeType() == NodeType.VECTOR){
            realCode = String.format("%s[%d]",na.getCode(),indexCounter.get(na.getMapUUID()));
        }

        return realCode;
    }

    public String getIndex(String uuid){
        NodeAttribute na = mapManager.getToNodes().getNodeAttribute(uuid);

        String rgcode = getRealCode(uuid);
        while(na.getParent() != null){
            NodeAttribute parent = na.getParent();
            rgcode = getRealCode(parent.getMapUUID())+"."+rgcode;
            na = na.getParent();
        }

        return rgcode;
    }

    public Object traverse(Object data) throws Exception{
        List<String> fromPath = mapManager.getFrom();
        for (int ix = 0; ix < fromPath.size(); ix++) {
            String path = fromPath.get(ix).replace("[]", "");
            MapHelper.traverse(this, "", data, path);
        }

        return this.getTarget();
    }

    /**
     * 查询路径对应节点的实时下标
     * @param path
     * @return
     */
    public Integer getIndexCounter(String path){
        String uid = getObjectUUIDByPath(path);
        return indexCounter.get(uid);
    }

    public String getObjectUUIDByPath(String path){
        return mapManager.getObjectUUIDByPath(path);
    }

    public NodeAttribute getSubjectNodeAttribute(String cpath){
        return mapManager.getSubjectNodeAttributeByPath(cpath);
    }

    public NodeAttribute getObjectNodeAttribute(String uuid){
        return mapManager.getObjectNodeAttributeByUUID(uuid);
    }

    public Object getNodeValue(String key1,int type){
        String key = getIndex(key1);
        if(StringUtils.isEmpty(key)){
            nodePath.put(key,target);
            return target;
        }

        Object mnode = nodePath.get(key);
        if(mnode == null){
            if(type==0) {
                mnode = new BaseDynamicRoleData();
            }else{
                mnode = new LinkedList<Object>();
                nodePath.put(key,mnode);
//                log.info("未命中: {}",key);
            }
        }
        return mnode;
    }
}
