package com.octpus.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XlsColumn {

    /**
     * 列名称
     */
    @ApiModelProperty(name = "数据列名称")
    @XmlAttribute
    private String name;

    /**
     * 列索引
     */
    @ApiModelProperty(name = "数据列索引")
    @XmlAttribute
    private int column;

    /**
     * 字段值类型
     *
     * String Numberic,Date
     *
     */
    @ApiModelProperty(name = "数据字段之类型")
    @XmlAttribute
    private String type;

    /**
     * 字段领域对象
     */
    @XmlAttribute
    @ApiModelProperty(name = "领域模型类型")
    private String domainType;

    /**
     * 字段名
     */
    @ApiModelProperty(name = "数据操作日期")
    @XmlValue
    private String data;

    /**
     * 是否必填
     */
    @XmlAttribute
    @ApiModelProperty(name = "是否必填标识")
    private boolean mandatory;

    /**
     * 是否扩展列
     */
    @XmlAttribute
    @ApiModelProperty(name = "是否扩展序列标识")
    private boolean extRow = false;

    /**
     * 备注
     */
    @XmlAttribute
    @ApiModelProperty(name = "消息备注")
    private String desc;

    /**
     * 校验器
     */
    @XmlAttribute
    @ApiModelProperty(name = "数据校验器")
    private String validator;
}
