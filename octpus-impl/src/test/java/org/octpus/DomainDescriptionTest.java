package org.octpus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.model.XlsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.octpus.inspect.core.NodeDescriptor;
import org.octpus.inspect.inspect.ModelDefinitionHelper;
import org.octpus.inspect.inspect.domaini.PrimaryEntity;
import org.octpus.map.BeanMapContext;
import org.octpus.map.MapContext;
import org.octpus.map.config.MapConfiguration;
import org.octpus.map.node.MapModel;
import org.octpus.service.SystemMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.IOException;

@Slf4j
@DisplayName("业务对象数据采集测试")
public class DomainDescriptionTest {
    public XlsTemplate loadFromJson() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ss = resolver.getResources("com/octpus/mapping/demo.json");
        if(ss.length <= 0){
            throw new IOException("文件不存在.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        XlsTemplate data = objectMapper.readValue(ss[0].getInputStream(), XlsTemplate.class);

        return  data;
    }

    @DisplayName("动态对象映射测试")
    @Test
    public void mapDynamicObject() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        SystemMapping mapperPool = new SystemMapping();

        // 装载测试数据
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperResources = resolver.getResources("com/octpus/map/map-005.json");
        for (Resource rs : mapperResources) {
            MapConfiguration mapper= objectMapper.readValue(rs.getInputStream(), MapConfiguration.class);

            MapModel mm = new MapModel();
            mm.setConfiguration(mapper);
            mm.init();

            mapperPool.addMapper(mm);
        }

        MapModel mapper = mapperPool.getMapper("M05");
        Assert.notNull(mapper,"映射模板未定义。");

        MapContext context = new MapContext(mapper);

        log.info(">>> {}",objectMapper.writeValueAsString(context.traverse(loadFromJson())));
    }

    @DisplayName("Bean对象映射测试")
    @Test
    public void mapBeanObject() throws Exception {
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
        Object result = context.traverse(loadFromJson());

        log.info(">>> {}",objectMapper.writeValueAsString(result));
    }

    @Test
    @DisplayName("采集业务对象信息测试")
    public void getDomainObjectDescription() throws Exception {
        NodeDescriptor ss = ModelDefinitionHelper.retrieve("policy", XlsTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        log.info(">>> {}",objectMapper.writeValueAsString(ss));
    }

    @Test
    @DisplayName("取得列表元素类型")
    public void test2() throws Exception{
        Object nd = ModelDefinitionHelper.getListGenericType(PrimaryEntity.class,"address");
    }
}
