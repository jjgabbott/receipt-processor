package com.fetch_rewards.receipt_processor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Simple configuration class for api docs
 */
@Configuration
public class OpenAPIConfiguration {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Receipt Processor").description("A simple receipt processor").version("1.0.0"));
	}

}
