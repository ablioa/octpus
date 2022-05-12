package org.fnd.modulator.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@XmlRootElement(name = "entity")
public class  XlsEntity {
    @ApiModelProperty(name = "数据序列号")
    @XmlAttribute
    private int sheet;

    @ApiModelProperty(name = "起始序号")
    @XmlAttribute
    private int start;

    @ApiModelProperty(name = "数据子项名称")
    private String name;

    @ApiModelProperty(name = "数据sheet名称")
    private String sheetName;

    @ApiModelProperty(name = "业务对象类型")
    @XmlAttribute
    private String domainType;

    @ApiModelProperty(name = "候选键")
    @XmlElementWrapper(name="candidateKeys")
    private List <Integer> candidateKey;

    @ApiModelProperty(name = "数据列")
    @XmlElementWrapper(name="columns")
    private List<XlsColumn> column;

}
