package com.mywallet.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    
	/*
	 * Defining class That need to be
	 * Documented
	 */
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select().apis(RequestHandlerSelectors.
						withClassAnnotation(RestController.class))
				.build();
	}
	
	/*
	 * Preparing Api Information 
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("My Wallet REST API DOCUMENTATION")
				.description("GUIDE TO KYC USAGE")
				.termsOfServiceUrl("http://localhost:8080/terms")
				.contact("My Wallet pvt ltd")
				.license("Apache License Version 20")
				.licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
				.version("1.0")
				.build();
	}
	
}
