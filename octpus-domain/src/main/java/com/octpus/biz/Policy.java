package com.octpus.biz;

import lombok.Data;

import java.util.List;

@Data
public class Policy {
    private String productName;

    private String productCode;

    private List<Insured> insured;
}
