package org.octpus.mapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.octpus.core.BaseDynamicRoleData;
import org.octpus.mapper.node.NodeAttribute;
import org.octpus.mapper.node.NodeType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

        nodePath.put("",target);
        target.setProperty("########-parent############","xxx");
    }

    public Object traverse(Object data) throws Exception{
        List<String> fromPath = mapManager.getFrom();
        for (int ix = 0; ix < fromPath.size(); ix++) {
            String path = fromPath.get(ix).replace("[]", "");
            MapHelper.traverse(path,this, "", data, path);
        }

        return this.getTarget();
    }

    // TODO 重构掉丑陋的API
    public NodeAttribute getSubjectNodeAttribute(String absoluetPath,String cpath){
        List<NodeAttribute> tlist = mapManager.getFromNodes().getAttributes().get(absoluetPath);
        List<NodeAttribute> list = mapManager.getSubjectNodeAttributeByPath(cpath);

        List<NodeAttribute> ls = tlist.stream()
                .filter(v->v.getGolbalCode().equals(absoluetPath))
                .collect(Collectors.toList());
        String mid = ls.get(0).getMid();

        List<NodeAttribute> res = list.stream()
                .filter(v->v.getMid().equals(mid) && v.getGolbalCode().equals(cpath))
                .collect(Collectors.toList());

        return res.get(0);
    }

    public NodeAttribute getObjectNodeAttribute(String uuid){
        return mapManager.getObjectNodeAttributeByUUID(uuid);
    }

    public String getRealCode(String uuid){
        NodeAttribute na = mapManager.getToNodes().getNodeAttributeByUUID(uuid);
        String realCode = na.getCode();

        if(na.getNodeType() == NodeType.VECTOR){
            realCode = String.format("%s[%d]",na.getCode(),indexCounter.get(na.getUuid()));
        }

        return realCode;
    }

    public String getIndex(String uuid){
        NodeAttribute na = mapManager.getToNodes().getNodeAttributeByUUID(uuid);

        String rgcode = getRealCode(uuid);
        while(na.getParent() != null){
            NodeAttribute parent = na.getParent();
            rgcode = getRealCode(parent.getUuid())+"."+rgcode;
            na = na.getParent();
        }

        return rgcode;
    }

    public Object addNode(String uuid,Object object) throws Exception{
        NodeAttribute na = mapManager.getToNodes().getNodeAttributeByUUID(uuid);
        NodeAttribute currentNode = na.getRoot();

        Object parentObject = target;
        do{

            switch (currentNode.getNodeType()){
                case VECTOR:{
                    if(parentObject instanceof BaseDynamicRoleData){
                        BaseDynamicRoleData pObj = (BaseDynamicRoleData) parentObject;
                        Object subObject = pObj.getProperty(currentNode.getCode());
                        if(subObject == null){
                            subObject = new LinkedList<>(); //
                            pObj.setProperty(currentNode.getCode(),subObject);
                        }

                        List<Object> listObject = (List<Object>)subObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index +1 > listObject.size()){
                            listObject.add(new BaseDynamicRoleData());
                        }
                        parentObject = listObject.get(index);

                    }else if(parentObject instanceof List){
                        List<Object> listObject = (List<Object>)parentObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index +1 >= listObject.size()){
                            listObject.add(new BaseDynamicRoleData());
                        }

                        parentObject = listObject.get(index);
                    }
                    break;
                }

                case SCARLAR:{
                    if(parentObject instanceof BaseDynamicRoleData){
                        BaseDynamicRoleData pObj = (BaseDynamicRoleData) parentObject;
                        Object subObject = pObj.getProperty(currentNode.getCode());
                        if(subObject == null){
                            pObj.setProperty(currentNode.getCode(),new BaseDynamicRoleData());
                        }
                        parentObject = pObj.getProperty(currentNode.getCode());
                    }else if(parentObject instanceof List){
                        List<Object> listObject = (List<Object>)parentObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index +1 > listObject.size()){
                            listObject.add(new BaseDynamicRoleData());
                        }

                        parentObject = listObject.get(index);
                    }

                    break;
                }

                case PREMITIVE:{
                    if(parentObject instanceof BaseDynamicRoleData){
                        BaseDynamicRoleData pObj = (BaseDynamicRoleData) parentObject;
                        Object subObject = pObj.getProperty(currentNode.getCode());
                        if(subObject == null){
                            pObj.setProperty(currentNode.getCode(),object);
                        }
                        parentObject = pObj.getProperty(currentNode.getCode());
                    }else if(parentObject instanceof List){
                        List<Object> listObject = (List<Object>)parentObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index +1 > listObject.size()){
                            listObject.add(object);
                        }

                        parentObject = listObject.get(index);
                    }
                    break;
                }
            }

            currentNode = currentNode.getChild();
        }while (currentNode!= null && !currentNode.getUuid().equals(uuid));

        return parentObject;
    }
}
