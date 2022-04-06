package org.octpus.inspect.inspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.octpus.inspect.core.NodeDescriptor;
import org.octpus.inspect.inspect.domaini.PrimaryEntity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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

        return root;
    }

    /**
     * 根据列表字段的泛型类型，构造列表元素
     *
     * TODO 对无泛型的数据和基础类型数据进行处理
     *
     * @param clz
     * @param fieldName
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object getListGenericType(Class clz,String fieldName) throws InstantiationException, IllegalAccessException {
        Field field = Arrays.stream(clz.getDeclaredFields())
                .filter(v->v.getName().equals(fieldName))
                .findFirst()
                .orElse(null);

        if(field.getType().getCanonicalName().equals("java.util.List")){
            Type genericType = field.getGenericType();

            if (null == genericType) {
                return new Object();
            }

            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                Class<?> actualTypeArgument = (Class<?>)pt.getActualTypeArguments()[0];
                if(!EXCEPTIONS.contains(actualTypeArgument.getCanonicalName())) {
                    return actualTypeArgument.newInstance();
                }else{
                    return new Object();
                }
            }
        }

        return new Object();
    }

    public static void main(String [] args) throws Exception{
        test2();
    }

    @Test
    public static void test2() throws Exception{
        Object nd = getListGenericType(PrimaryEntity.class,"address");
        System.out.println("...");
    }

    @Test
    public static void test1() throws Exception{
        NodeDescriptor nd = retrieve("policy", PrimaryEntity.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println(objectMapper.writeValueAsString(nd));
    }
}
