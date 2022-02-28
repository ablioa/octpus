package com.octpus.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@XmlRootElement(name = "entity")
public class  XlsEntity {
    @XmlAttribute
    private int sheet;

    @XmlAttribute
    private int start;

    private String name;

    private String sheetName;

    @XmlAttribute
    private String domainType;

    @XmlElementWrapper(name="candidateKeys")
    private List <Integer> candidateKey;

    @XmlElementWrapper(name="columns")
    private List<XlsColumn> column;

}
