package org.octpus.service;

import lombok.extern.slf4j.Slf4j;
import org.octpus.inspect.core.NodeDescriptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ModelDefinitionHelper {
    public static Set<String> EXCEPTIONS = new HashSet<>();
    static {
        EXCEPTIONS.add("java.lang.String");
        EXCEPTIONS.add("java.lang.Integer");
        EXCEPTIONS.add("java.lang.Double");
        EXCEPTIONS.add("java.lang.Long");
    }

    public static NodeDescriptor retrieve(String name, Class clazz) throws Exception{
        NodeDescriptor root = new NodeDescriptor();

        root.setDomainType(clazz.getCanonicalName());
        root.setName(name);
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(field.getType().getCanonicalName().equals("java.util.List")){
                Type genericType = field.getGenericType();

                if (null == genericType) {
                    continue;
                }

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    Class<?> actualTypeArgument = (Class<?>)pt.getActualTypeArguments()[0];
                    if(!EXCEPTIONS.contains(actualTypeArgument.getCanonicalName())) {
                        Object actualType = actualTypeArgument.newInstance();
                        root.getFields().add(retrieve(field.getName(), actualType.getClass()));
                    }
                }

                continue;
            }

            if(!EXCEPTIONS.contains(field.getDeclaringClass().getCanonicalName())) {
                root.getFields().add(retrieve(field.getName(), field.getType()));
            }
        }

        log.info("running ...");

        return root;
    }
}
