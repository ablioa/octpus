package org.fnd.modulator.rules;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.fnd.modulator.rules.model.Rule;
import org.fnd.modulator.rules.service.RuleService;
import org.fnd.modulator.rules.model.dto.RuleAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@Api(tags = "转换规则管理服务")
@RequestMapping("/rule")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/")
    @ApiOperation(value = "添加映射规则", notes = "")
    public ResponseEntity<RuleAddDTO> add(@RequestBody Rule rule) throws Exception {
        String ruleId = ruleService.addRule(rule);
        RuleAddDTO dto = new RuleAddDTO();
        dto.setId(ruleId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询规则列表", notes = "")
    public ResponseEntity<List<String>> list() throws Exception {
        List <String> list = ruleService.getRuleIds();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/")
    @ApiOperation(value = "查询映射规则", notes = "")
    public ResponseEntity<Rule> get(@RequestParam String rid) throws Exception {
        Rule rule = ruleService.getRule(rid);
        return new ResponseEntity<>(rule, HttpStatus.OK);
    }
}
