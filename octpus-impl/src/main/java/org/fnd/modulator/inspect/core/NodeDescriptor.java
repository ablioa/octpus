package org.fnd.modulator.inspect.core;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 业务模型节点描述
 * @author wangzh
 */
@Data
public class NodeDescriptor {
    private String uuid;

    private String domainType;

    private String name;

    private boolean isCollection;

    private String annotation;

    private List<NodeDescriptor> fields;

    public NodeDescriptor(){
        fields = new LinkedList<>();
        uuid = UUID.randomUUID().toString();
    }
}
