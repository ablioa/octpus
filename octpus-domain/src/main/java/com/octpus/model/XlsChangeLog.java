package com.octpus.model;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XlsChangeLog {
    @XmlAttribute
    private String time;

    @XmlAttribute
    private String content;

    @XmlValue
    private String data;
}
