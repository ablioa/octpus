package org.fnd.modulator.target;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class TField {

    /**
     * 列名称
     */
    @XmlAttribute
    private String name;

    /**
     * 列索引
     */
    @XmlAttribute
    private int yy;

    /**
     * 字段值类型
     *
     * String Numberic,Date
     *
     */
    @XmlAttribute
    private String type;

    /**
     * 字段领域对象
     */
    @XmlAttribute
    private String domainType;

    /**
     * 字段名
     */
    @XmlValue
    private String xxx;

    /**
     * 是否必填
     */
    @XmlAttribute
    private boolean mandatory;

    /**
     * 是否扩展列
     */
    @XmlAttribute
    private boolean extRow = false;

    /**
     * 备注
     */
    @XmlAttribute
    private String desc;

    /**
     * 校验器
     */
    @XmlAttribute
    private String validator;
}
