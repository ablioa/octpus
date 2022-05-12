package org.fnd.modulator.map.script;

import lombok.Data;

/**
 * 转换上下文
 * @author wangzh
 */
@Data
public class ConversionContext {
    /**
     * 源节点
     */
    private Object source;

    /**
     * 目标节点
     */
    private Object target;

    /**
     * 输入参数
     */
    private Object input;

    public ConversionContext(Object source,Object target,Object input){
        this.source = source;
        this.target = target;
        this.input = input;
    }
}
