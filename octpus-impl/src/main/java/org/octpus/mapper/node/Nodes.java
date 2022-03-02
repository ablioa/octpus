package org.octpus.mapper.node;

import lombok.Data;
import org.octpus.mapper.model.MapItem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class Nodes {
    private Map<String,NodeAttribute> attributes;

    public Nodes(){
        attributes = new LinkedHashMap<>();
    }

    public NodeAttribute getNodeAttribute(String code){
        return attributes.get(code);
    }

    public void parse(List<MapItem> from){
        for (MapItem item : from){
            parseNodes(item);
        }
    }

    public void parseNodes(MapItem item){
        String [] pathNodes = item.getPath().split("\\.");

        StringBuffer glbCode = new StringBuffer();
        NodeAttribute parent = null;
        for(int ix = 0; ix < pathNodes.length; ix ++){
            NodeAttribute attribute = new NodeAttribute();
            String fields = pathNodes[ix];
            String code = fields;
            if(fields.endsWith("[]")){
                code = code.substring(0,code.length()-2);
                attribute.setNodeType(NodeType.VECTOR);
                attribute.setCode(code);
            }else{
                attribute.setCode(code);
                attribute.setNodeType(NodeType.SCARLAR);
            }

            if(ix == pathNodes.length -1){
                attribute.setNodeType(NodeType.PREMITIVE);
            }

            if(glbCode.length() != 0){
                glbCode.append(".");
            }

            attribute.setMindex(ix);
            attribute.setMid(item.getMid());
            glbCode.append(code);
            attribute.setGolbalCode(glbCode.toString());

            // TODO 做校验，处理类型冲突
            attributes.put(attribute.getGolbalCode(),attribute);
            attributes.put(attribute.getMapUUID(),attribute);

            attribute.setParent(parent);
            parent = attribute;
        }
    }
}
