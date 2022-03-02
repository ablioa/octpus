package org.octpus.mapper;

import lombok.Data;

/**
 *  a.bb.XX.dd.ee
 *  
 *  overall: XX.dd.ee
 *  leading: XX
 *  successive: dd.ee
 *  parent: aa.bb
 *  global: aa.bb.XX
 * @author wangzh
 */
@Data
public class Path {
    private String leading;
    private String successive;
    private String overall;
    private String global;
    private String parent;

    private boolean tail;

    public Path(String parent,String subPath){
        this.overall = subPath;
        this.parent = parent;
        tail = false;
    }

    public static Path split(String parent,String subPath){
        Path defaultPath = new Path(parent,subPath);

        int section = subPath.indexOf(".");
        if(section == -1){
            defaultPath.leading = subPath;
            defaultPath.successive = "";
            defaultPath.tail = true;

            if(parent.length() == 0){
                defaultPath.global = defaultPath.getLeading();
            }else{
                defaultPath.global = parent + "." + defaultPath.getLeading();
            }
            return defaultPath;
        }

        defaultPath.leading = subPath.substring(0,section);
        defaultPath.successive = subPath.substring(section+1);

        if(parent.length() == 0){
            defaultPath.global = defaultPath.getLeading();
        }else{
            defaultPath.global = parent + "." + defaultPath.getLeading();
        }

        return defaultPath;
    }
}
