package org.octpus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.octpus.service.DataRetrieveService;
import org.octpus.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@Api(tags = "保单数据转换服务")
@RequestMapping("/mapping")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataController {

    @Autowired
    private DataRetrieveService dataRetrieveService;

    @GetMapping("/object")
    @ApiOperation(value = "业务映射测试-Object", notes = "")
    public ResponseEntity<Object> testObjectMapping(@RequestParam String ruleCode) throws Exception {
        Object xx = dataRetrieveService.mappingObject(ruleCode);
        return new ResponseEntity<>(xx, HttpStatus.OK);
    }

    @GetMapping("/bean")
    @ApiOperation(value = "业务映射测试-Bean", notes = "")
    public ResponseEntity<Object> testBeanMapping(@RequestParam String ruleCode) throws Exception {
        Object xx = dataRetrieveService.mappingBean(ruleCode);
        return new ResponseEntity<>(xx, HttpStatus.OK);
    }


    @GetMapping("/data")
    @ApiOperation(value = "查看测试业务数据", notes = "")
    public ResponseEntity<Object> getData() throws Exception {
        return new ResponseEntity<>(DataService.getTestData(), HttpStatus.OK);
    }

}
