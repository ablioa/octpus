package com.octpus.target;

import lombok.Data;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class TVersion {
    @XmlAttribute
    private String time;

    @XmlAttribute
    private String target;

    @XmlValue
    private String text;
}
