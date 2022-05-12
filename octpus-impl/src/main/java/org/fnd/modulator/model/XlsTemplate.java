package org.fnd.modulator.model;

import io.swagger.annotations.ApiModelProperty;
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
public class XlsTemplate {
    @ApiModelProperty(name = "模板代码")
    @XmlAttribute
    private String name;

    @ApiModelProperty(name = "模块名称名称")
    @XmlAttribute
    private String subSystem;

    @ApiModelProperty(name = "业务描述")
    @XmlAttribute
    private String description;

    @ApiModelProperty(name = "附加属性")
    @XmlElement(name="attribute")
    private XlsAttribute attribute;

    @ApiModelProperty(name = "实体清单")
    @XmlElementWrapper(name="entities")
    private List<XlsEntity> entity;

    /**
     * 解析excel的扩展字段
     */
    @ApiModelProperty(name = "解析excel的扩展字段")
    private List<XlsEntity> extRows;
}
