package com.octpus.target;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@XmlRootElement(name = "attribute")
public class TDescription {
    // 版本
    @XmlAttribute
    private String version;

    // 提供商N
    @XmlAttribute
    private String vendor;

    // 文件大小
    private String fileSize;

    // 字符集
    private String encoding;

    // 校验和摘要
    private String checksum;

    @XmlElementWrapper(name="logs")
    private List<TVersion> log;
}
