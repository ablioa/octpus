package org.octpus.map.node;

import lombok.Data;

@Data
public class Node {
    private NodeType nodeType;

    private String code;

    private String golbalCode;

    private String mid;

    private Integer mindex;

    // TODO 可选
    private String nodeDomainType;

    private Node parent;

    private Node root;

    private Node child;

    public  String getUuid(){
        return String.format("%s.%d",mid,mindex);
    }
}
