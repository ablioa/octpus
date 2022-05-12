package org.fnd.modulator.rules.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.fnd.modulator.rules.model.Rule;
import org.fnd.modulator.rules.repository.RuleRepository;
import org.fnd.modulator.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Date;
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

    @Override
    public String uuid(){
        int num = (int)(Math.random() * 100);
        long ts = new Date().getTime();
        String digest = String.format("%04d%s",Long.toHexString(num),Long.toHexString(ts));
        return new String(Base64.encodeBase64(digest.getBytes(StandardCharsets.UTF_8)));
    }
}
