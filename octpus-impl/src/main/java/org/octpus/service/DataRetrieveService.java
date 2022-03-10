package org.octpus.service;

import com.octpus.facility.XlsTemplatePool;
import com.octpus.model.XlsTemplate;
import lombok.extern.java.Log;
import org.octpus.map.MapContext;
import org.octpus.map.node.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Log
@Component
public class DataRetrieveService {
    @Autowired
    private XlsTemplatePool xlsTemplatePool;

    @Autowired
    private SystemMapping mapperPool;

    public Object getData(){
        return xlsTemplatePool.getTemplate("MID001");
    }

    public Object retrive(String mid) throws Exception{
        XlsTemplate testData = xlsTemplatePool.getTemplate("MID001");

        MapModel mapper = mapperPool.getMapper(mid);
        Assert.notNull(mapper,"映射模板未定义。");

        MapContext context = new MapContext(mapper);
        return context.traverse(testData);
    }
}
