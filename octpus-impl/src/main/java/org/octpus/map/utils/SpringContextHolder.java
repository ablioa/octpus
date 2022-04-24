package org.octpus.map.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(SpringContextHolder.BEAN_NAME)
public class SpringContextHolder implements ApplicationContextAware {

    public static final String BEAN_NAME = "springContextHolder";

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    public static <O> O getBean(String name,Class <O> requiredType){
        return getApplicationContext().getBean(name,requiredType);
    }

    public static <O> O getBean(Class <O> requireType){
        return getApplicationContext().getBean(requireType);
    }

    public static <O> String [] getBeanNamesForType(Class <O> requireType){
        return getApplicationContext().getBeanNamesForType(requireType);
    }

    public static <O> Map<String,O> getBeansForType(Class <O> requireType){
        return getApplicationContext().getBeansOfType(requireType);
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if(SpringContextHolder.applicationContext == null){
            SpringContextHolder.applicationContext = context;
        }
    }

    public static boolean hasApplicationContext(){
        return getApplicationContext() != null;
    }

    public static String getActiveProfile(){
        return getEnivronment().getActiveProfiles()[0];
    }

    public static Environment getEnivronment(){
        return SpringContextHolder.getApplicationContext().getEnvironment();
    }
}