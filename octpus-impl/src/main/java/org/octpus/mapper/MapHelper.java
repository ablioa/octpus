package org.octpus.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.octpus.core.BaseDynamicRoleData;
import org.octpus.mapper.node.NodeAttribute;

import java.util.List;

/**
 * 遍历数据节点，构造映射对象
 *
 * @author wangzh
 */
@Slf4j
public class MapHelper {

    /**
     * 路径结构： [parent][leading][successive]
     *
     * @param context
     * @param parent
     * @param target
     * @param path    需要遍历的子树
     * @throws Exception
     */
    public static void traverse(MapContext context, String parent, Object target, String path) throws Exception {
        Path ps = Path.split(parent, path);
        NodeAttribute currentNode = context.getSubjectNodeAttribute(ps.getGlobal());

        switch (currentNode.getNodeType()) {
            case VECTOR: {
                Object collectionObject = PropertyUtils.getProperty(target, ps.getLeading());
                List<?> list = (List) collectionObject;
                for (int ix = 0; ix < list.size(); ix++) {
                    context.getIndexCounter().put(currentNode.getMapUUID(), ix);
                    traverse(context, currentNode.getGolbalCode(), list.get(ix), ps.getSuccessive());
                }

                break;
            }

            case SCARLAR: {
                Object item = PropertyUtils.getProperty(target, ps.getLeading());
                traverse(context, currentNode.getGolbalCode(), item, ps.getSuccessive());
                break;
            }

            case PREMITIVE: {
                Object object = PropertyUtils.getProperty(target, ps.getLeading());

                NodeAttribute dna = context.getObjectNodeAttribute(currentNode.getMapUUID());
                retrieve(context,dna.getGolbalCode(), object);
            }
        }
    }

    /**
     * 提取源数据，设置到目标数据
     *
     * @param context
     * @param destinationPath
     * @param object
     */
    public static void retrieve(MapContext context, String destinationPath, Object object) {
        String[] subnode = destinationPath.split("\\.");

        StringBuffer sb = new StringBuffer();

        for (int ix = 0; ix < subnode.length; ix++) {
            if (sb.length() != 0) {
                sb.append(".");
            }

            NodeAttribute na = context.getMapManager().getToNodes().getNodeAttribute(sb + subnode[ix]);
            Object parentObject = context.getTarget();
            if(na.getParent() != null){
                parentObject = context.getNodeValue(na.getParent().getMapUUID(), 0);
            }

            switch (na.getNodeType()) {
                case VECTOR: {
                    Integer index = context.getIndexCounter().get(na.getMapUUID());

                    List<Object> ss = (List<Object>) context.getNodeValue(na.getMapUUID(), 1);
                    while (ss.size() < index + 1) {
                        ss.add(new BaseDynamicRoleData());
                    }

                    if (parentObject instanceof BaseDynamicRoleData) {
                        BaseDynamicRoleData ssp = (BaseDynamicRoleData) parentObject;
                        ssp.setProperty(subnode[ix], ss);
                    } else {
                        Integer pindex = context.getIndexCounter().get(na.getParent().getMapUUID());

                        List<BaseDynamicRoleData> ssp = (List<BaseDynamicRoleData>) parentObject;
                        while (ssp.size() < pindex + 1) {
                            ssp.add(new BaseDynamicRoleData());
                        }

                        BaseDynamicRoleData itemData = ssp.get(pindex);
                        itemData.setProperty(subnode[ix], ss);
                    }

                    log.info("path-1:{},{},{}",na.getGolbalCode(),context.getIndex(na.getMapUUID()),object);
                    break;
                }

                case SCARLAR: {
                    Object mnode = context.getNodeValue(na.getMapUUID(), 0);
                    ((BaseDynamicRoleData)parentObject).setProperty(subnode[ix], mnode);

                    log.info("path-2:{},{},{}",na.getGolbalCode(),context.getIndex(na.getMapUUID()),object);
                    break;
                }

                case PREMITIVE: {
                    if (parentObject instanceof BaseDynamicRoleData) {
                        ((BaseDynamicRoleData) parentObject).setProperty(subnode[ix], object);
                    }

                    if (parentObject instanceof List) {
                        String uid = na.getParent().getMapUUID();
                        Integer parentIndex = context.getIndexCounter().get(uid);

                        BaseDynamicRoleData tt = (BaseDynamicRoleData) ((List<?>) parentObject).get(parentIndex);
                        tt.setProperty(subnode[ix], object);
                    }

                    log.info("path-3:{},{},{}",na.getGolbalCode(),context.getIndex(na.getMapUUID()),object);
                    break;
                }
            }

            sb.append(subnode[ix]);
        }
    }
}
