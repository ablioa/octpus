package com.octpus.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XlsChangeLog {
    @ApiModelProperty(name = "数据更新时间")
    @XmlAttribute
    private String time;

    @ApiModelProperty(name = "数据类型")
    @XmlAttribute
    private String content;

    @ApiModelProperty(name = "数据内容")
    @XmlValue
    private String data;
}
