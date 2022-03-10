package org.octpus.controller;

import com.octpus.model.XlsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.octpus.service.ModelDefinitionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@Api(tags = "模型转换服务")
@RequestMapping("/model")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModelControler {

    @GetMapping("/catalog")
    @ApiOperation(value = "基础模型", notes = "")
    public ResponseEntity<Object> catalog() throws Exception {
        Object result = ModelDefinitionHelper.retrieve("policy", XlsTemplate.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}