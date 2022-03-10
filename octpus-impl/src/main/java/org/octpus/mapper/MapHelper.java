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
    public static void traverse(String absoluetPath,MapContext context, String parent, Object target, String path) throws Exception {
        Path ps = Path.split(parent, path);
        NodeAttribute currentNode = context.getSubjectNodeAttribute(absoluetPath,ps.getGlobal());

        switch (currentNode.getNodeType()) {
            case VECTOR: {
                Object collectionObject = PropertyUtils.getProperty(target, ps.getLeading());
                List<?> list = (List) collectionObject;
                for (int ix = 0; ix < list.size(); ix++) {
                    context.getIndexCounter().put(currentNode.getUuid(), ix);
                    traverse(absoluetPath,context, currentNode.getGolbalCode(), list.get(ix), ps.getSuccessive());
                }

                break;
            }

            case SCARLAR: {
                Object item = PropertyUtils.getProperty(target, ps.getLeading());
                traverse(absoluetPath,context, currentNode.getGolbalCode(), item, ps.getSuccessive());
                break;
            }

            case PREMITIVE: {
                Object object = PropertyUtils.getProperty(target, ps.getLeading());

                NodeAttribute dna = context.getObjectNodeAttribute(currentNode.getUuid());
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
    public static void retrieve(MapContext context, String destinationPath, Object object) throws Exception{
        String[] subnode = destinationPath.split("\\.");

        StringBuffer sb = new StringBuffer();

        for (int ix = 0; ix < subnode.length; ix++) {
            if (sb.length() != 0) {
                sb.append(".");
            }

            // TODO 重构掉丑陋的API
            NodeAttribute na = context.getMapManager().getToNodes().getNodeAttribute(sb + subnode[ix]).get(0);
            switch (na.getNodeType()) {
                case VECTOR: {
                    context.addNode(na.getUuid(),object);
                    log.info("path-1:{},{},{}",na.getGolbalCode(),context.getIndex(na.getUuid()),object);
                    break;
                }

                case SCARLAR: {
                    context.addNode(na.getUuid(),object);
                    log.info("path-2:{},{},{}",na.getGolbalCode(),context.getIndex(na.getUuid()),object);
                    break;
                }

                case PREMITIVE: {
                    context.addNode(na.getUuid(),object);
                    log.info("path-3:{},{},{}",na.getGolbalCode(),context.getIndex(na.getUuid()),object);
                    break;
                }
            }

            sb.append(subnode[ix]);
        }
    }
}
