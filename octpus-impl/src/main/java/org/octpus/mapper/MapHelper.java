package org.octpus.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.octpus.core.BaseDynamicRoleData;
import org.octpus.mapper.node.NodeAttribute;

import java.util.List;

/**
 * 遍历数据节点，构造映射对象
 * @author wangzh
 */
public class MapHelper {

    public static void traverse(MapContext context, String locateId, String parent, Object target, String key) throws Exception {
        String gk = parent;
        if (parent.length() != 0) {
            gk = gk + ".";
        }

        Path ps = Path.split(key);
        NodeAttribute na = context.getMapManager().getFromNodes().getNodeAttribute(gk + ps.getLeading());
        switch (na.getNodeType()) {
            case VECTOR: {
                Object collecionObject = PropertyUtils.getProperty(target, ps.getLeading());
                List<?> yy = (List) collecionObject;
                for (int ix = 0; ix < yy.size(); ix++) {
                    Object uu = yy.get(ix);

                    String sp = parent;
                    if (parent.length() != 0) {
                        sp = parent + ".";
                    }

                    String tp = locateId;
                    if (tp.length() != 0) {
                        tp = locateId + ".";
                    }

                    String cpath = sp + ps.getLeading();
                    String lpath = tp + ps.getLeading() + "[" + ix + "]";

                    String uid = context.getMapManager().getFromNodes().getNodeAttribute(cpath).getMapUUID();
                    context.getIndexCounter().put(uid,ix);

                    traverse(context, lpath, cpath, uu, ps.getSuccessive());
                }
                break;
            }

            case SCARLAR: {
                Object xx = PropertyUtils.getProperty(target, ps.getLeading());
                String cpath = ps.getLeading();
                if (parent.length() != 0) {
                    cpath = parent + "." + ps.getLeading();
                }
                traverse(context, cpath, cpath, xx, ps.getSuccessive());
                break;
            }

            case PREMITIVE: {
                Object xx = PropertyUtils.getProperty(target, ps.getLeading());
                String cpath = ps.getLeading();
                if (parent.length() != 0) {
                    cpath = parent + "." + ps.getLeading();
                }

                NodeAttribute sna = context.getMapManager().getFromNodes().getNodeAttribute(cpath);
                NodeAttribute dna = context.getMapManager().getToNodes().getNodeAttribute(sna.getMapUUID());

                retrieve(context,context.getTarget(),dna.getGolbalCode(),xx);
            }
        }
    }

    public static void retrieve(MapContext context, BaseDynamicRoleData target, String cpath, Object object){
        String [] subnode = cpath.split("\\.");

        StringBuffer sb = new StringBuffer();
        // 最终遍历路径，调试用。
        StringBuffer logpath = new StringBuffer();

        for (int ix = 0; ix < subnode.length; ix ++){
            String parent = sb.toString();
            if(sb.length() != 0){ sb.append("."); }
            if(logpath.length() != 0){ logpath.append("."); }

            String gpath = sb + subnode[ix];
            NodeAttribute na = context.getMapManager().getToNodes().getNodeAttribute(gpath);

            logpath.append(subnode[ix]);
            sb.append(subnode[ix]);

            switch (na.getNodeType()){
                case VECTOR:{
                    String uid = context.getMapManager().getToNodes().getNodeAttribute(gpath).getMapUUID();
                    Integer index = context.getIndexCounter().get(uid);

                    logpath.append("[" + index+"]");

                    List<Object> ss = (List<Object>)context.getNodeValue(sb.toString(),1);
                    while(ss.size() < index+1){
                        ss.add(new BaseDynamicRoleData());
                    }

                    BaseDynamicRoleData ssp = (BaseDynamicRoleData)context.getNodeValue(parent,0);
                    ssp.setProperty(subnode[ix],ss);

                    break;
                }
                case SCARLAR:{
                    Object mnode = context.getNodeValue(sb.toString(),0);
                    context.getTarget().setProperty(subnode[ix],mnode);
                    context.getNodePath().put(sb.toString(),mnode);
                    break;
                }

                case PREMITIVE:{
                    if(parent.equals("")){
                        target.setProperty(subnode[ix],object);
                        break;
                    }

                    Object ss = context.getNodeValue(parent,0);
                    if(ss instanceof  BaseDynamicRoleData){
                        ((BaseDynamicRoleData) ss).setProperty(subnode[ix],object);
                    }

                    if(ss instanceof List){
                        String uid = context.getMapManager().getToNodes().getNodeAttribute(parent).getMapUUID();
                        Integer index = context.getIndexCounter().get(uid);
                        BaseDynamicRoleData tt = (BaseDynamicRoleData)((List<?>) ss).get(index);
                        tt.setProperty(subnode[ix],object);
                    }

                    break;
                }
            }
        }
    }
}
