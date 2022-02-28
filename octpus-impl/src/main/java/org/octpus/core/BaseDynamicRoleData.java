package org.octpus.core;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import groovy.lang.MetaProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.octpus.core.BasePoGo;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

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
            //先从动态属性中获取
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
            //动态属性中设置
            this.extPropertiesMap.put(property, newValue);
        } else {
            super.setProperty(property, newValue);
        }

    }

    /**
     * 获取动态属性集合，使用JsonAnyGetter以保证jackson转换时忽略
     *
     * @return
     */
    @SuppressWarnings("unused")
    @ToString.Include(name = "extPropertiesMap")
    @JsonIgnore
    @JsonAnyGetter
    public Map<String, Object> getExtPropertiesMap() {
        return this.extPropertiesMap;
    }

    /**
     * json转对象时处理找不到对应属性时的处理方法
     */
    @SuppressWarnings("unused")
    @JsonAnySetter
//    @Override
    public void anySetter(String property, Object value) {
        if (StringUtils.hasText(property) && value != null) {
            //前端double类型会导致精度丢失 故转为String
            if (value instanceof Double) {
                DecimalFormat decimalFormat = new DecimalFormat();
                //不使用千分位
                decimalFormat.setGroupingUsed(false);
                //BigDecimal bigDecimalValue=new BigDecimal((Double) value);
                value = decimalFormat.format(value);
            }
            //动态属性中设置
            this.extPropertiesMap.put(property, value);
        }
    }
}
