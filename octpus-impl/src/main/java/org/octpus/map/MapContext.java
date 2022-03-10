package org.octpus.map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.octpus.core.BaseDynamicRoleData;
import org.octpus.map.node.MapModel;
import org.octpus.map.node.Node;

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

    public MapContext(MapModel model) {
        this.model = model;
        indexCounter = new HashMap<>();
    }

    public Object traverse(Object data) throws Exception {
        BaseDynamicRoleData target = new BaseDynamicRoleData();
        List<Node> attributes = model.getSubjectTree();

        for(Node na : attributes){
            traverse(na,target,data);
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
                            subObject = new LinkedList<>(); //
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
                            pObj.setProperty(currentNode.getCode(), object);
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
}