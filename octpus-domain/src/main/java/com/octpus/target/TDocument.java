package com.octpus.target;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 导入模板定义
 * @author wangzh
 */
@Data
@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.FIELD)
public class TDocument {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String subSystem;

    @XmlAttribute
    private String description;

    @XmlElement(name="attribute")
    private TDescription attribute;

    @XmlElementWrapper(name="entities")
    private List<TSheet> entity;

    /**
     * 解析excel的扩展字段
     */
    private List<TSheet> extRows;
}
