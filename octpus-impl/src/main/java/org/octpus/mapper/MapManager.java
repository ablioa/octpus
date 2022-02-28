package org.octpus.mapper;

import lombok.Data;
import org.octpus.mapper.model.Mapper;
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

        fromNodes.parse(mapper.getSubject());
        toNodes.parse(mapper.getObject());

        // TODO 添加校验规则，校验映射数量和类型
        // ...
    }

    public List<String> getFrom(){
        return mapper.getSubject().stream().map(v->v.getPath()).collect(Collectors.toList());
    }

    public List<String> getTo(){
        return mapper.getObject().stream().map(v->v.getPath()).collect(Collectors.toList());
    }
}
