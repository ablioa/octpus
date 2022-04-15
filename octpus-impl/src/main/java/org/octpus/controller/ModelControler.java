package org.octpus.controller;

import com.cpic.PolicyDto;
import com.octpus.model.XlsTemplate;
import com.octpus.target.TDocument;
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

    @GetMapping("/catalog")
    @ApiOperation(value = "基础模型", notes = "")
    public ResponseEntity<Object> catalog() throws Exception {
        Object result = ModelDefinitionHelper.retrieve("", XlsTemplate.class,false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/meta")
    @ApiOperation(value = "取得指定模型的元数据信息", notes = "")
    public ResponseEntity<Object> meta(@RequestParam String className) throws Exception {
        Class clz = Class.forName(className);
        Object result = ModelDefinitionHelper.retrieve("", clz,false);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
