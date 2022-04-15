package org.octpus.rules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.model.XlsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.octpus.map.MapContext;
import org.octpus.map.config.MapConfiguration;
import org.octpus.map.node.MapModel;
import org.octpus.rules.model.Rule;
import org.octpus.rules.model.dto.RuleAddDTO;
import org.octpus.rules.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    public XlsTemplate loadFromJson() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] ss = resolver.getResources("com/octpus/mapping/demo.json");
        if(ss.length <= 0){
            throw new IOException("文件不存在.");
        }

//        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        XlsTemplate data = objectMapper.readValue(ss[0].getInputStream(), XlsTemplate.class);

        return  data;
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

        MapConfiguration mc= objectMapper.readValue(rule.getRule(), MapConfiguration.class);
        log.info("装载映射模板:{}", mc);

        MapModel mapper = new MapModel();
        mapper.setConfiguration(mc);
        mapper.init();

        List<String> xx = ruleService.getRuleIds();
//
////            Mapper mapper = mapperPool.getMapper(mid);
//        MapModel mm = new MapModel();
//        mm.setConfiguration(mapper);
//        mm.init();
        MapContext context = new MapContext(mapper);

        log.info(">>> {}",objectMapper.writeValueAsString(context.traverse(loadFromJson())));


//        log.info("result:{}",rule.getRule());

        return new ResponseEntity<>(rule, HttpStatus.OK);
    }
}
