package org.octpus.mapper;

import lombok.Data;

@Data
public class Path {
    private String leading;
    private String successive;
    private String overall;
    private boolean tail;

    public Path(String overall){
        this.overall = overall;
        tail = false;
    }

    public static Path split(String path){
        Path defaultPath = new Path(path);

        int location = path.indexOf(".");
        if(location == -1){
            defaultPath.leading = path;
            defaultPath.successive = "";
            defaultPath.tail = true;
            return defaultPath;
        }

        defaultPath.leading = path.substring(0,location);
        defaultPath.successive = path.substring(location+1);

        return defaultPath;
    }
}
