package org.octpus.map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.octpus.inspect.inspect.ModelDefinitionHelper;
import org.octpus.map.config.MapConverter;
import org.octpus.map.node.MapModel;
import org.octpus.map.node.Node;
import org.octpus.map.script.MappingRuleExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanMapContext {
    private MapModel model;

    private Map<String, Integer> indexCounter;

    public BeanMapContext(MapModel model) {
        this.model = model;
        indexCounter = new HashMap<>();
    }

    public Object buildTargetObject(String clzName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class clz = Class.forName(clzName);
        return clz.newInstance();
    }

    public Object traverse(Object data) throws Exception {
        String clazzName = model.getConfiguration().getObjects().getDomainType();
        Object target = buildTargetObject(clazzName);
        List<Node> attributes = model.getSubjectTree();

        for(Node na : attributes){
            traverse(na,target,data);
        }

        return target;
    }

    /**
     * 往目标的Bean对象上送数据
     *
     * @param sourceNode
     * @param target
     * @param data
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public void traverse(Node sourceNode,Object target, Object data) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        switch (sourceNode.getNodeType()) {
            case VECTOR: {
                Object collectionObject = PropertyUtils.getProperty(data, sourceNode.getCode());
                List<?> list = (List) collectionObject;
                for (int ix = 0; ix < list.size(); ix++) {
                    indexCounter.put(sourceNode.getUuid(), ix);
                    traverse(sourceNode.getChild(),target, list.get(ix));
                }

                break;
            }

            case SCARLAR: {
                Object item = PropertyUtils.getProperty(data, sourceNode.getCode());
                traverse(sourceNode.getChild(),target, item);
                break;
            }

            case PREMITIVE: {
                Object object = PropertyUtils.getProperty(data, sourceNode.getCode());
                Node handle = model.getObjectNodes().get(sourceNode.getUuid());
                addNode(handle,target, object);
                break;
            }
        }
    }

    /**
     * 添加业务数据节点
     * @param na
     * @param target
     * @param object
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    public Object addNode(Node na, Object target, Object object) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Node currentNode = na.getRoot();

        Object parentObject = target;
        do {
            switch (currentNode.getNodeType()) {
                case VECTOR: {
                    if(parentObject instanceof List){
                        List<Object> listObject = (List<Object>) parentObject;
                        Integer index = indexCounter.get(currentNode.getUuid());
                        while (index + 1 >= listObject.size()) {
//                            listObject.add(new BaseDynamicRoleData());
                        }

                        parentObject = listObject.get(index);
                    }else{
                        Object subObject = PropertyUtils.getProperty(parentObject,currentNode.getCode());//pObj.getProperty(currentNode.getCode());
                        if (subObject == null) {
                            subObject = new LinkedList<>();
                            PropertyUtils.setProperty(parentObject,currentNode.getCode(),subObject);
                        }

                        List<Object> listObject = (List<Object>) subObject;
                        Integer index = indexCounter.get(currentNode.getUuid());
                        while (index + 1 > listObject.size()) {
                            Object obct = ModelDefinitionHelper.getListGenericType(parentObject.getClass(), currentNode.getCode());
                            listObject.add(obct);
                        }

                        parentObject = listObject.get(index);
                    }
                    break;
                }

                case SCARLAR: {
                    if(parentObject instanceof List){
                        List<Object> listObject = (List<Object>) parentObject;
                        Integer index = indexCounter.get(currentNode.getUuid());
                        while (index + 1 > listObject.size()) {
//                            listObject.add(new BaseDynamicRoleData());
                        }

                        parentObject = listObject.get(index);
                    }else{
                        Object subObject = PropertyUtils.getProperty(parentObject,currentNode.getCode());
                        if (subObject == null ) {
                            Class clz = PropertyUtils.getPropertyType(parentObject,currentNode.getCode());
                            subObject = clz.newInstance();
                            PropertyUtils.setProperty(parentObject,currentNode.getCode(),subObject);
                        }

                        parentObject = PropertyUtils.getProperty(parentObject,currentNode.getCode());
                    }

                    break;
                }

                case PREMITIVE: {
                    if(parentObject instanceof List){
                        List<Object> listObject = (List<Object>) parentObject;
                        Integer index = indexCounter.get(currentNode.getUuid());
                        while (index + 1 > listObject.size()) {
                            listObject.add(object);
                        }

                        parentObject = listObject.get(index);
                    }else{
                        Object subObject = PropertyUtils.getProperty(parentObject,currentNode.getCode());
                        if (subObject == null) {
                            Object nvalue = object;

                            if(currentNode.getConverter() != null){
                                nvalue = transformValue(currentNode.getConverter(),object);
                            }

                            PropertyUtils.setProperty(parentObject,currentNode.getCode(), nvalue);
                        }
                    }
                    break;
                }
            }

            currentNode = currentNode.getChild();
        } while (currentNode != null);

        return parentObject;
    }

    /**
     * 映射值转换
     *
     * TODO 代码风格重构
     *
     * @param converter
     * @param input
     * @return
     */
    public Object transformValue(MapConverter converter, Object input){
        Object result = input;
        switch (converter.getMethod()){
            case "C0001":{
                result = converter.getTable().getTable().get(input);
                if(result == null){
                    result = converter.getTable().getDefaultValue();
                }
                log.info("查表转换:{},{}",input,result);
                break;
            }

            case "C0002":{
                log.info("执行脚本:{}",input);
                result = "";//MappingRuleExecutor.execute(converter.getGroovy(),input);
                break;
            }

            case "C0003":{
                log.info("执行Beean:{}",input);
                break;
            }
        }

        return result;
    }
}
