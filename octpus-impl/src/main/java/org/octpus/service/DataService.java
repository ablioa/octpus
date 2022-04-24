package org.octpus.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.model.XlsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Slf4j
public class DataService {
    /**
     * 提从测试数据使用
     * @return
     * @throws IOException
     */
    public static XlsTemplate getTestData() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("com/octpus/mapping/demo.json");
        if(resources.length <= 0){
            throw new IOException("文件不存在.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        XlsTemplate data = objectMapper.readValue(resources[0].getInputStream(), XlsTemplate.class);

        return  data;
    }
}
