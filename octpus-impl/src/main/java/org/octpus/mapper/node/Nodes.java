package org.octpus.mapper.node;

import lombok.Data;
import org.octpus.mapper.model.MapItem;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Nodes {
    private Map<String,List<NodeAttribute>> attributes;

    private Map <String,NodeAttribute> cachedAttribute;

    public Nodes(){
        attributes = new LinkedHashMap<>();
        cachedAttribute = new LinkedHashMap<>();
    }

    // TODO 重构掉丑陋的API
    public List<NodeAttribute> getNodeAttribute(String code){
        return attributes.get(code);
    }

    public NodeAttribute getNodeAttributeByUUID(String code){
        return cachedAttribute.get(code);
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
        NodeAttribute root = new NodeAttribute();
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
            List<NodeAttribute> nl = attributes.get(attribute.getGolbalCode());
            if(nl == null){
                nl = new LinkedList<>();
            }
            nl.add(attribute);

            attributes.put(attribute.getGolbalCode(),nl);
//            attributes.put(attribute.getUuid(),attribute);

            cachedAttribute.put(attribute.getUuid(),attribute);

            attribute.setParent(parent);
            if(parent == null){
                root = attribute;
            }else{
                parent.setChild(attribute);
            }

            attribute.setRoot(root);
            parent = attribute;
        }
    }
}
