package org.octpus.map.node;

/**
 * 映射节点数据类型
 * @author wangzh
 */
public enum NodeType {

    SCARLAR(0,"标量数据","."),
    VECTOR(1,"矢量数据","[]"),
    PREMITIVE(2,"基本数据","");

    private int type;
    private String desc;
    private String token;

    NodeType(int type,String desc,String token){
        this.type = type;
        this.desc = desc;
        this.token = token;
    }
}
