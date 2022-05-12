package org.fnd.modulator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.fnd.modulator.rules.model.Rule;
import org.fnd.modulator.map.BeanMapContext;
import org.fnd.modulator.map.MapContext;
import org.fnd.modulator.map.config.MapConfiguration;
import org.fnd.modulator.map.node.MapModel;
import org.fnd.modulator.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据转换服务
 * @author wangzh
 */
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

        log.info(rule.getRule());

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
