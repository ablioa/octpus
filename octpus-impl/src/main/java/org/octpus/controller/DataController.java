package org.octpus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.octpus.service.DataRetrieveService;
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
@RequestMapping("/data")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataController {
    @Autowired
    private DataRetrieveService dataRetrieveService;

    @GetMapping("/")
    @ApiOperation(value = "业务数据映射", notes = "")
    public ResponseEntity<Object> retrieve(@RequestParam String mapperCode) throws Exception {
        Object xx = dataRetrieveService.retrive(mapperCode);
        return new ResponseEntity<>(xx, HttpStatus.OK);
    }

    @GetMapping("/data")
    @ApiOperation(value = "业务数据查看", notes = "")
    public ResponseEntity<Object> getData() throws Exception {
        Object xx = dataRetrieveService.getData();
        return new ResponseEntity<>(xx, HttpStatus.OK);
    }

}
