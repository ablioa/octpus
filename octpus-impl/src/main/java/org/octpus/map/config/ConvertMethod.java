package org.octpus.map.config;

import java.util.Arrays;

public enum ConvertMethod {
    TABLE("C0001","枚举映射"),
    SCRIPT("C0002","脚本映射"),
    BEAN("C0002","SpringBean");

    private String code;
    private String desc;

    ConvertMethod(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
