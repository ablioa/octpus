package org.octpus.rules.service;

import org.octpus.rules.model.Rule;

import java.util.List;

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

    /**
     * 查询id
     * @return
     */
    List<String> getRuleIds();
}
