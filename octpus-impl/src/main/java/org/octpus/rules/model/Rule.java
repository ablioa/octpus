package org.octpus.rules.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="rule")
public class Rule {

    @Id
    @Column
    private String id;

    @Column
    private String rule;
}
