package org.fnd.modulator.components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter{
	public static final String BASE_API_PACKAGE = "org.octpus";
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");

		registry.addResourceHandler("index.html")
				.addResourceLocations("classpath:/static/");
	}
	
	public void addInterceptors(InterceptorRegistry registry){
	}

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(BASE_API_PACKAGE))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		String serviceDescription = "Resource Service";
		String serviceName = "Resource management service";
		String version = "0.0.1";
		
		return new ApiInfoBuilder()
				.description(serviceDescription)
				.title(serviceName)
				.version(version).build();
	}
}
