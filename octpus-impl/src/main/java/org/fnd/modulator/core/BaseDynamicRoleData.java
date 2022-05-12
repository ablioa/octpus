package org.fnd.modulator.core;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import groovy.lang.MetaProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@EqualsAndHashCode(callSuper = false)
@Data
@JsonIgnoreProperties({"class", "metaClass", "declaringClass"})
public class BaseDynamicRoleData extends BasePoGo {

    public static final String BIG_DECIMAL = "BigDecimal";

    @JsonIgnore
    private Map<String, Object> extPropertiesMap;

    public BaseDynamicRoleData() {
        super();
        extPropertiesMap = new LinkedHashMap<>(10);
    }

    @Override
    public Object getProperty(String property) {
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        MetaProperty mp = DefaultGroovyMethods.hasProperty(this, property);
        Object value;
        if (mp == null) {
            value = this.extPropertiesMap.get(property);
        } else {
            value = super.getProperty(property);
        }
        return value;
    }

    @Override
    public void setProperty(String property, Object newValue) {
        MetaProperty mp = DefaultGroovyMethods.hasProperty(this, property);
        if (mp == null) {
            this.extPropertiesMap.put(property, newValue);
        } else {
            super.setProperty(property, newValue);
        }

    }

    @ToString.Include(name = "extPropertiesMap")
    @JsonIgnore
    @JsonAnyGetter
    public Map<String, Object> getExtPropertiesMap() {
        return this.extPropertiesMap;
    }

    @JsonAnySetter
    public void anySetter(String property, Object value) {
        if (StringUtils.hasText(property) && value != null) {
            if (value instanceof Double) {
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setGroupingUsed(false);
                value = decimalFormat.format(value);
            }
            this.extPropertiesMap.put(property, value);
        }
    }
}
