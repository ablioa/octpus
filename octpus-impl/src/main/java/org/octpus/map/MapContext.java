package org.octpus.map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.octpus.core.BaseDynamicRoleData;
import org.octpus.map.config.MapConverter;
import org.octpus.map.config.MapItem;
import org.octpus.map.node.MapModel;
import org.octpus.map.node.Node;
import org.octpus.map.script.ConversionContext;
import org.octpus.map.script.MappingRuleExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class MapContext {
    private MapModel model;

    private Map<String, Integer> indexCounter;

    private Object source;

    private Object target;

    public MapContext(MapModel model) {
        this.model = model;
        indexCounter = new HashMap<>();
    }

    public Object traverse(Object source) throws Exception {
        BaseDynamicRoleData target = new BaseDynamicRoleData();
        List<Node> attributes = model.getSubjectTree();

        this.target = target;
        this.source = source;

        for(Node node : attributes){
            traverse(node,target,source);
        }

//        log.info("##### 处理目标扩展节点: {}");
        List<MapItem> extItems = model.getExeNodes();
        for(MapItem v : extItems){
//            log.info("## %%% ## {} : {}: {}",v.getMid(),v.getPath(),v.getConverter());
            String script = v.getConverter().getGroovy();
            Object result = MappingRuleExecutor.execute(getConversionContext("你好"),script,"你好");
            target.setProperty(v.getPath(),result);
        }

        return target;
    }

    public void traverse(Node sourceNode,Object target, Object data) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
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

    public Object addNode(Node na, Object target, Object object)  {
        Node currentNode = na.getRoot();

        Object parentObject = target;
        do {
            switch (currentNode.getNodeType()) {
                case VECTOR: {
                    if (parentObject instanceof BaseDynamicRoleData) {
                        BaseDynamicRoleData pObj = (BaseDynamicRoleData) parentObject;
                        Object subObject = pObj.getProperty(currentNode.getCode());
                        if (subObject == null) {
                            subObject = new LinkedList<>();
                            pObj.setProperty(currentNode.getCode(), subObject);
                        }

                        List<Object> listObject = (List<Object>) subObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index + 1 > listObject.size()) {
                            listObject.add(new BaseDynamicRoleData());
                        }
                        parentObject = listObject.get(index);

                    } else if (parentObject instanceof List) {
                        List<Object> listObject = (List<Object>) parentObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index + 1 >= listObject.size()) {
                            listObject.add(new BaseDynamicRoleData());
                        }

                        parentObject = listObject.get(index);
                    }
                    break;
                }

                case SCARLAR: {
                    if (parentObject instanceof BaseDynamicRoleData) {
                        BaseDynamicRoleData pObj = (BaseDynamicRoleData) parentObject;
                        Object subObject = pObj.getProperty(currentNode.getCode());
                        if (subObject == null) {
                            pObj.setProperty(currentNode.getCode(), new BaseDynamicRoleData());
                        }
                        parentObject = pObj.getProperty(currentNode.getCode());
                    } else if (parentObject instanceof List) {
                        List<Object> listObject = (List<Object>) parentObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index + 1 > listObject.size()) {
                            listObject.add(new BaseDynamicRoleData());
                        }

                        parentObject = listObject.get(index);
                    }

                    break;
                }

                case PREMITIVE: {
                    if (parentObject instanceof BaseDynamicRoleData) {
                        BaseDynamicRoleData pObj = (BaseDynamicRoleData) parentObject;
                        Object subObject = pObj.getProperty(currentNode.getCode());
                        if (subObject == null) {
                            Object nvalue = object;
                            if(currentNode.getConverter() != null){
                                nvalue = transformValue(currentNode.getConverter(),object);
                            }
                            pObj.setProperty(currentNode.getCode(), nvalue);
                        }
                        parentObject = pObj.getProperty(currentNode.getCode());
                    } else if (parentObject instanceof List) {
                        List<Object> listObject = (List<Object>) parentObject;
                        Integer index = getIndexCounter().get(currentNode.getUuid());
                        while (index + 1 > listObject.size()) {
                            listObject.add(object);
                        }

                        parentObject = listObject.get(index);
                    }

                    break;
                }
            }

            currentNode = currentNode.getChild();
        } while (currentNode != null);

        return parentObject;
    }

    public ConversionContext getConversionContext(Object input){
        return new ConversionContext( this.source,this.target,input);
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
    public Object transformValue(MapConverter converter,Object input){
        Object result = input;

        ConversionContext context = new ConversionContext(this.source,this.target,input);

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
                result = MappingRuleExecutor.execute(context,converter.getGroovy(),input);
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
