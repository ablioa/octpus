package org.octpus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.model.XlsTemplate;
import org.octpus.map.BeanMapContext;
import org.octpus.map.config.MapConfiguration;
import org.octpus.map.node.MapModel;
import org.octpus.service.SystemMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

public class BeanMapperApp {

    public static XlsTemplate loadFromJson() throws Exception{
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ss = resolver.getResources("com/octpus/mapping/demo.json");
        if(ss.length <= 0){
            throw new Exception("文件不存在.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        XlsTemplate data = objectMapper.readValue(ss[0].getInputStream(), XlsTemplate.class);

        return  data;
    }

    public static void testTransform() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        SystemMapping mapperPool = new SystemMapping();

        // 装载测试数据
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperResources = resolver.getResources("com/octpus/map/map-007.json");
        for (Resource rs : mapperResources) {
            MapConfiguration mapper= objectMapper.readValue(rs.getInputStream(), MapConfiguration.class);

            MapModel mm = new MapModel();
            mm.setConfiguration(mapper);
            mm.init();

            mapperPool.addMapper(mm);
        }

        MapModel mapper = mapperPool.getMapper("M07");
        Assert.notNull(mapper,"映射模板未定义。");

        BeanMapContext context = new BeanMapContext(mapper);
        System.out.println(objectMapper.writeValueAsString(context.traverse(loadFromJson())));
    }

    public static void main(String [] args) throws Exception{
        testTransform();
    }
}
