package org.octpus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.facility.XlsTemplatePool;
import com.octpus.model.XlsTemplate;
import org.octpus.mapper.MapContext;
import org.octpus.mapper.MapManager;
import org.octpus.mapper.model.Mapper;
import org.octpus.mapper.model.MapperPool;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import javax.xml.bind.JAXB;

public class TestApp {

    public static XlsTemplate loadFromJson() throws Exception{
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ss = resolver.getResources("com/octpus/mapping/demo.json");
        if(ss.length <= 0){
            throw new Exception("文件不存在.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        XlsTemplate data = objectMapper.readValue(ss[0].getInputStream(), XlsTemplate.class);

//        System.out.println("data:" + objectMapper.writeValueAsString(data));

        return  data;
    }

    public static void testTransform() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        MapperPool mapperPool = new MapperPool();

        // 装载测试数据
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperResources = resolver.getResources("com/octpus/map/map-005.json");
        for (Resource rs : mapperResources) {
            Mapper mapper= objectMapper.readValue(rs.getInputStream(), Mapper.class);

            MapManager mm = new MapManager();
            mm.setMapper(mapper);
            mm.buildContext();

            mapperPool.addMapper(mm);
        }

        MapManager mapper = mapperPool.getMapper("M02");
        Assert.notNull(mapper,"映射模板未定义。");

        MapContext context = new MapContext(mapper);
        context.traverse(loadFromJson());

        System.out.println(objectMapper.writeValueAsString(context.getTarget()));
    }

    public static void main(String [] args) throws Exception{
        testTransform();
    }
}
