package org.octpus.rules.service.impl;

import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.octpus.rules.model.Rule;
import org.octpus.rules.repository.RuleRepository;
import org.octpus.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RuleServiceImpl implements RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String addRule(Rule rule) {
        log.info("----- rule:",rule);
        if(StringUtils.isEmpty(rule.getId())){
            rule.setId(UUID.randomUUID().toString());
        }
        ruleRepository.save(rule);
        return rule.getId();
    }

    @Override
    public Rule getRule(String rid) {
        return ruleRepository.findById(rid).orElse(null);
    }

    @Override
    public List<String> getRuleIds(){
        return jdbcTemplate.queryForList("select id from rule", String.class);
    }
}
