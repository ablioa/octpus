package org.octpus.rules;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.octpus.rules.model.Rule;
import org.octpus.rules.model.dto.RuleAddDTO;
import org.octpus.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@Api(tags = "转换规则管理服务")
@RequestMapping("/rule")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/")
    @ApiOperation(value = "添加映射规则", notes = "")
    public ResponseEntity<RuleAddDTO> retrieve(@RequestBody Rule rule) throws Exception {
        String ruleId = ruleService.addRule(rule);
        RuleAddDTO dto = new RuleAddDTO();
        dto.setId(ruleId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
