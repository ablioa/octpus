package org.octpus.controller;

import com.octpus.model.XlsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.octpus.inspect.inspect.ModelDefinitionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@Api(tags = "模型转换服务")
@RequestMapping("/model")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModelControler {

    public static final Class DEFAULT_MODEL = XlsTemplate.class;

    @GetMapping("/class")
    @ApiOperation(value = "检查业务模型是否存在", notes = "")
    public ResponseEntity<String> validateClass(@RequestParam String className){
        try {
            Class clz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return new ResponseEntity<>("0001", HttpStatus.OK);
        }
        return new ResponseEntity<>("0000", HttpStatus.OK);
    }

    @GetMapping("/catalog")
    @ApiOperation(value = "提取基础模型结构", notes = "")
    public ResponseEntity<Object> catalog(@RequestParam String className) throws Exception {
        log.info("领域模型对象: {}",className);

        Class clz = DEFAULT_MODEL;
        if(className != null){
            clz = Class.forName(className);
        }

        Object result = ModelDefinitionHelper.retrieve("","", clz,false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/meta")
    @ApiOperation(value = "取得指定模型的元数据信息", notes = "")
    public ResponseEntity<Object> meta(@RequestParam String className) throws Exception {
        Class clz = Class.forName(className);
        Object result = ModelDefinitionHelper.retrieve("","", clz,false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
