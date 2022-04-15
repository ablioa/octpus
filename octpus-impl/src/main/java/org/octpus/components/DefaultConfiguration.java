package org.octpus.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.facility.XlsTemplatePool;
import org.octpus.service.SystemMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.octpus.book.model.ModelManager;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class DefaultConfiguration {
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return new ObjectMapper();
    }

    @Bean
    public SystemMapping getModelManager(){
        return new SystemMapping();
    }

    @Bean
    public XlsTemplatePool getXlsTemplatePool(){
        return new XlsTemplatePool();
    }

    @Bean
    public MappingJackson2HttpMessageConverter setupObjectMapper() {
        List<MediaType> types = new LinkedList<MediaType>();

        types.add(MediaType.APPLICATION_JSON_UTF8);
        types.add(MediaType.TEXT_HTML);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(types);
        converter.setObjectMapper(new ObjectMapper());

        return converter;
    }
}
