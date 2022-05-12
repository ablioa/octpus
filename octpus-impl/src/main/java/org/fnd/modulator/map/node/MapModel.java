package org.fnd.modulator.map.node;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fnd.modulator.map.config.MapConfiguration;
import org.fnd.modulator.map.config.MapItem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class MapModel {
    private MapConfiguration configuration;

    private Map<String, Node> subjectNodes;

    private Map<String, Node> objectNodes;

//    private Map<String,Node> extNodes;

    private List<Node> subjectTree;

    private List<MapItem> exeNodes;

    public void init(){
        // TODO 添加校验规则，校验映射数量和类型
        // ...


        subjectNodes = parse(configuration.getSubjects().getItems());
        objectNodes = parse(configuration.getObjects().getItems());
        exeNodes = buildExtNodes();

        subjectTree = new LinkedList<>();
        subjectNodes.forEach((k,v)->{
            if(v.getParent() == null){
                subjectTree.add(v);
            }
        });
    }

    public List <MapItem> buildExtNodes(){
        List <MapItem> output = new LinkedList<>();

        /////
        List <MapItem> xx = configuration.getObjects().getExt();
        xx.forEach(v->{
//            extNodes.put(v.getMid(),v);
            output.add(v);
        });
        ////

        return output;
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
                attribute.setConverter(item.getConverter());
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
