//package com.octpus.facility;
//
//import com.octpus.model.XlsTemplate;
//import lombok.Data;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//@Data
//public class XlsTemplatePool {
//    private List<XlsTemplate> mapperHolder;
//    private Map<String,XlsTemplate> dataMapper;
//
//    public XlsTemplatePool(){
//        mapperHolder = new LinkedList<>();
//        dataMapper = new HashMap<>();
//    }
//
//    public void addTemplate(XlsTemplate template){
//        mapperHolder.add(template);
//        dataMapper.put(template.getName(),template);
//    }
//
//    public List<XlsTemplate> getTemplates(){
//        return mapperHolder;
//    }
//
//    public XlsTemplate getTemplate(String code){
//        return dataMapper.get(code);
//    }
//}
