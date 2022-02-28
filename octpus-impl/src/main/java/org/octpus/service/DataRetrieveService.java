package org.octpus.service;

import com.octpus.facility.XlsTemplatePool;
import com.octpus.model.XlsTemplate;
import lombok.extern.java.Log;
import org.octpus.mapper.MapContext;
import org.octpus.mapper.MapHelper;
import org.octpus.mapper.MapManager;
import org.octpus.mapper.model.MapperPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

@Log
@Component
public class DataRetrieveService {
    @Autowired
    private XlsTemplatePool xlsTemplatePool;

    @Autowired
    private MapperPool mapperPool;

    public Object getData(){
        return xlsTemplatePool.getTemplate("MID001");
    }

    public Object retrive(String mid) throws Exception{
        XlsTemplate template = xlsTemplatePool.getTemplate("MID001");

        MapManager mapper = mapperPool.getMapper(mid);
        Assert.notNull(mapper,"映射模板未定义。");

        MapContext mapContext = new MapContext(mapper);
        List<String> fromPath = mapper.getFrom();
        for (int ix = 0; ix < fromPath.size(); ix++) {
            String path = fromPath.get(ix).replace("[]", "");
            MapHelper.traverse(mapContext, "", "", template, path);
        }

        return mapContext.getTarget();
    }
}
