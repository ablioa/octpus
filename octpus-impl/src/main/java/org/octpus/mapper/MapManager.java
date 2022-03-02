package org.octpus.mapper;

import lombok.Data;
import org.octpus.mapper.model.Mapper;
import org.octpus.mapper.node.NodeAttribute;
import org.octpus.mapper.node.Nodes;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MapManager {
    private Mapper mapper;

    private Nodes fromNodes;

    private Nodes toNodes;

    public void buildContext(){
        fromNodes = new Nodes();
        toNodes = new Nodes();

        fromNodes.parse(mapper.getSubject().getItems());
        toNodes.parse(mapper.getObject().getItems());

        // TODO 添加校验规则，校验映射数量和类型
        // ...
    }

    public List<String> getFrom(){
        return mapper.getSubject().getItems()
                .stream()
                .map(v->v.getPath())
                .collect(Collectors.toList());
    }

    public List<String> getTo(){
        return mapper.getObject().getItems()
                .stream()
                .map(v->v.getPath())
                .collect(Collectors.toList());
    }

    public NodeAttribute getSubjectNodeAttributeByPath(String path){
        return fromNodes.getNodeAttribute(path);
    }

    public NodeAttribute getSubjectNodeAttributeByUUID(String path){
        return fromNodes.getNodeAttribute(path);
    }

    public NodeAttribute getObjectNodeAttributeByPath(String uuid){
        return toNodes.getNodeAttribute(uuid);
    }

    public NodeAttribute getObjectNodeAttributeByUUID(String uuid){
        return toNodes.getNodeAttribute(uuid);
    }

    public String getObjectUUIDByPath(String path){
        return toNodes.getNodeAttribute(path).getMapUUID();
    }
}
