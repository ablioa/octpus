package org.octpus.mapper.node;

import lombok.Data;

@Data
public class NodeAttribute {
    private NodeType nodeType;

    private String code;

    private String golbalCode;

    private String mid;

    private Integer mindex;

    // TODO 可选
    private String nodeDomainType;

    public  String getMapUUID(){
        return String.format("%s-%d",mid,mindex);
    }
}
