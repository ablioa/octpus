package org.octpus.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.octpus.map.BeanMapContext;
import org.octpus.map.MapContext;
import org.octpus.map.config.MapConfiguration;
import org.octpus.map.node.MapModel;
import org.octpus.rules.model.Rule;
import org.octpus.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataRetrieveService {
    @Autowired
    private SystemMapping mapperPool;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Object 映射
     * @param ruleCode
     * @return
     * @throws Exception
     */
    public Object mappingObject(String ruleCode) throws Exception{
        Rule rule = ruleService.getRule(ruleCode);

        MapConfiguration mc= objectMapper.readValue(rule.getRule(), MapConfiguration.class);
        log.info("装载映射模板:{}", mc);

        MapModel mapper = new MapModel();
        mapper.setConfiguration(mc);
        mapper.init();

        MapContext context = new MapContext(mapper);

        Object result = context.traverse(DataService.getTestData());
        log.info(">>> {}",objectMapper.writeValueAsString(result));

        return result;
    }

    /**
     * Object 映射
     * @param ruleCode
     * @return
     * @throws Exception
     */
    public Object mappingBean(String ruleCode) throws Exception{
        Rule rule = ruleService.getRule(ruleCode);

        MapConfiguration mc= objectMapper.readValue(rule.getRule(), MapConfiguration.class);
        log.info("装载映射模板:{}", mc);

        MapModel mapper = new MapModel();
        mapper.setConfiguration(mc);
        mapper.init();

        BeanMapContext context = new BeanMapContext(mapper);

        Object result = context.traverse(DataService.getTestData());
        log.info(">>> {}",objectMapper.writeValueAsString(result));

        return result;
    }
}
