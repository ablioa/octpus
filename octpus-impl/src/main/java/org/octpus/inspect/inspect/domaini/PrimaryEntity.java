package org.octpus.inspect.inspect.domaini;

import lombok.Data;

import java.util.List;

@Data
public class PrimaryEntity {
    private String name;

    private Integer age;

    private List<AddressEntity> address;
}
