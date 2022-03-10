package org.octpus.map.node;

import lombok.Data;
import org.octpus.map.config.MapConfiguration;
import org.octpus.map.config.MapItem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class MapModel {
    private MapConfiguration configuration;

    private Map<String, Node> subjectNodes;

    private Map<String, Node> objectNodes;

    private List<Node> subjectTree;

    public void init(){
        // TODO 添加校验规则，校验映射数量和类型
        // ...

        subjectNodes = parse(configuration.getSubjects().getItems());
        objectNodes = parse(configuration.getObjects().getItems());

        subjectTree = new LinkedList<>();
        subjectNodes.forEach((k,v)->{
            if(v.getParent() == null){
                subjectTree.add(v);
            }
        });
    }

    public Map<String, Node> parse(List<MapItem> from){
        Map<String, Node>  cachedAttribute = new HashMap<>();
        for (MapItem item : from){
            parseNodes(cachedAttribute,item);
        }

        return cachedAttribute;
    }

    public void parseNodes(Map<String, Node>  cachedAttribute, MapItem item){

        String [] pathNodes = item.getPath().split("\\.");

        StringBuffer glbCode = new StringBuffer();
        Node parent = null;
        Node root = new Node();
        for(int ix = 0; ix < pathNodes.length; ix ++){
            Node attribute = new Node();
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
//            List<NodeAttribute> nl = attributes.get(attribute.getGolbalCode());
//            if(nl == null){
//                nl = new LinkedList<>();
//            }
//            nl.add(attribute);

//            attributes.put(attribute.getGolbalCode(),nl);
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
