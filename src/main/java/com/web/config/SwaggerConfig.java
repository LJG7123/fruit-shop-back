package com.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().
						addList("Bearer Authentication"))
				.path("/login", new PathItem().post(new Operation().description("실제 로그인 테스트는 Postman으로 가능").parameters(
						List.of(new Parameter().in("query").name("username").required(true).schema(new StringSchema()),
								new Parameter().in("query").name("password").required(true).schema(new StringSchema()))
				)))
				.components(new Components().addSecuritySchemes
						("Bearer Authentication", createAPIKeyScheme()));
	}

	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}
}
