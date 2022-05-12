package org.fnd.modulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@ComponentScan(basePackageClasses = MainApp.class)
@EnableScheduling
public class MainApp extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(MainApp.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}
