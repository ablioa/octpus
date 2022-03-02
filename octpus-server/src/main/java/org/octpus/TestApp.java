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
    public static void main(String [] args) throws Exception{
        XlsTemplatePool xlsTemplatePool = new XlsTemplatePool();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        MapperPool mapperPool = new MapperPool();

        // 装载测试数据
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ss = resolver.getResources("com/octpus/mapping/import.mapping.*.xml");
        for (Resource rs : ss) {
            XlsTemplate template = JAXB.unmarshal(rs.getInputStream(), XlsTemplate.class);
            xlsTemplatePool.addTemplate(template);
        }

        // 装载映射模板数据
        Resource[] mapperResources = resolver.getResources("com/octpus/map/map-005.json");
        for (Resource rs : mapperResources) {
            Mapper mapper= objectMapper.readValue(rs.getInputStream(), Mapper.class);

            MapManager mm = new MapManager();
            mm.setMapper(mapper);
            mm.buildContext();

            mapperPool.addMapper(mm);
        }

        XlsTemplate testData = xlsTemplatePool.getTemplate("MID001");
        MapManager mapper = mapperPool.getMapper("M02");
        Assert.notNull(mapper,"映射模板未定义。");

        MapContext context = new MapContext(mapper);
        context.traverse(testData);

        System.out.println(objectMapper.writeValueAsString(context.getTarget()));
    }
}
