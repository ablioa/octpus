package com.octpus.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@XmlRootElement(name = "attribute")
public class XlsAttribute {
    // 版本
    @ApiModelProperty(name = "扩展数据版本")
    @XmlAttribute
    private String version;

    // 提供商N
    @ApiModelProperty(name = "数据提供商")
    @XmlAttribute
    private String vendor;

    // 文件大小
    @ApiModelProperty(name = "数据长度")
    private String fileSize;

    // 字符集
    @ApiModelProperty(name = "字符集")
    private String encoding;

    // 校验和摘要
    @ApiModelProperty(name = "校验和")
    private String checksum;

    @ApiModelProperty(name = "变更日志")
    @XmlElementWrapper(name="logs")
    private List<XlsChangeLog> log;
}
