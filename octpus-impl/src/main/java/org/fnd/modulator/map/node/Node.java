package org.fnd.modulator.map.node;

import lombok.Data;
import org.fnd.modulator.map.config.MapConverter;

@Data
public class Node {
    private NodeType nodeType;

    private String code;

    private String golbalCode;

    private String mid;

    private Integer mindex;

    private MapConverter converter;

    // TODO 可选
    private String nodeDomainType;

    private Node parent;

    private Node root;

    private Node child;

    public  String getUuid(){
        return String.format("%s.%d",mid,mindex);
    }
}
