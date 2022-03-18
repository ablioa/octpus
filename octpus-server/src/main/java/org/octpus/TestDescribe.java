package org.octpus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.octpus.model.XlsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.octpus.inspect.core.NodeDescriptor;
import org.octpus.inspect.inspect.ModelDefinitionHelper;

@Slf4j
public class TestDescribe {

    public static void main(String [] args) throws Exception{
        NodeDescriptor ss = ModelDefinitionHelper.retrieve("policy", XlsTemplate.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println("data:" + objectMapper.writeValueAsString(ss));
    }
}
