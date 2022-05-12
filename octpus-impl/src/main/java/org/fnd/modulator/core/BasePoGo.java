package org.fnd.modulator.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.springframework.beans.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"class", "metaClass", "declaringClass"})
public class BasePoGo extends GroovyObjectSupport {

    /**
     * 判断当前对象是否拥有非动态属性
     * @param property
     * @return
     */
    public boolean isNonDynamicProperty(String property) {
        if(StringUtils.isEmpty(property)){
            return false;
        }
        return DefaultGroovyMethods.hasProperty(this, property) != null;
    }

    @Override
    public Object getProperty(String property) {
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        MetaProperty mp = DefaultGroovyMethods.hasProperty(this, property);
        Object value = null;
        if (mp != null) {
            //存在的属性才返回
            value = super.getProperty(property);
        }
        return value;
    }

    @Override
    public void setProperty(String property, Object newValue) {
        MetaProperty mp = DefaultGroovyMethods.hasProperty(this, property);
        if (mp != null) {
            //只设置存在的属性
            super.setProperty(property, newValue);
        }
    }

    /**
     * 复制属性到目标对象中，复制时忽略属性class、metaClass、declaringClass、expandoMetaClass
     *
     * @param targetObj 目标对象
     * @param <T>       目标对象类型
     * @return
     */
    public <T> T copyPropertiesTo(T targetObj) {
        if (targetObj == null) {
            return null;
        }
        BeanUtils.copyProperties(this, targetObj, "class", "metaClass", "declaringClass", "expandoMetaClass");
        return targetObj;
    }
}
