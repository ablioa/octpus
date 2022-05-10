package org.octpus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.octpus.map.BeanMapContext;
import org.octpus.map.MapContext;
import org.octpus.map.config.MapConfiguration;
import org.octpus.map.node.MapModel;
import org.octpus.service.DataService;
import org.octpus.service.SystemMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

@Slf4j
@DisplayName("业务对象数据采集测试")
public class AnotherTest {
    @DisplayName("Beany映射扩展信息")
    @Test
    public void mapExtensionObject() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        SystemMapping mapperPool = new SystemMapping();

        // 装载测试数据
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperResources = resolver.getResources("com/octpus/map/map-008.json");
        for (Resource rs : mapperResources) {
            MapConfiguration mapper= objectMapper.readValue(rs.getInputStream(), MapConfiguration.class);

            MapModel mm = new MapModel();
            mm.setConfiguration(mapper);
            mm.init();

            mapperPool.addMapper(mm);
        }

        MapModel mapper = mapperPool.getMapper("M078");
        Assert.notNull(mapper,"映射模板未定义。");

        MapContext context = new MapContext(mapper);

        Object testData = DataService.getTestData();
        Object result = context.traverse(testData);

        log.info(">>> {}",objectMapper.writeValueAsString(result));
    }
}
