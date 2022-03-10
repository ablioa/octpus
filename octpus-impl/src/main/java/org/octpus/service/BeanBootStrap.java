package org.octpus.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octpus.facility.XlsTemplatePool;
import com.octpus.model.XlsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.octpus.map.node.MapModel;
import org.octpus.map.config.MapConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXB;


/**
 * 解析器构造
 * @author wangzh
 */
@Component
@Order(10)
@Slf4j
public class BeanBootStrap implements CommandLineRunner {

    public static final String PACKAGE_TO_SCAN = "com.octpus";

    @Autowired
    private XlsTemplatePool xlsTemplatePool;

    @Autowired
    private SystemMapping mapperPool;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... strings) throws Exception {
        // 装载测试数据
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ss = resolver.getResources("com/octpus/mapping/import.mapping.*.xml");
        for (Resource rs : ss) {
            XlsTemplate template = JAXB.unmarshal(rs.getInputStream(), XlsTemplate.class);
            log.info("装配导入模板:{}/{} <- {}", template.getName(), template.getDescription(), rs.getFilename());
            xlsTemplatePool.addTemplate(template);
        }

        // 装载映射模板数据
        Resource[] mapperResources = resolver.getResources("com/octpus/map/map-*.json");
        for (Resource rs : mapperResources) {
            MapConfiguration mapper= objectMapper.readValue(rs.getInputStream(), MapConfiguration.class);
            log.info("装载映射模板:{}", mapper);

//            Mapper mapper = mapperPool.getMapper(mid);
            MapModel mm = new MapModel();
            mm.setConfiguration(mapper);
            mm.init();

            mapperPool.addMapper(mm);
        }
    }
}
