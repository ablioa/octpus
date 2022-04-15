package org.octpus.rules.service;

import org.octpus.rules.model.Rule;

/**
 * 规则管理器
 * @author wangzh
 */
public interface RuleService {

    /**
     * 新增规则
     * @param rule
     */
    String addRule(Rule rule);

    /**
     * 查询规则
     * @param rid
     * @return
     */
    Rule getRule(String rid);
}
