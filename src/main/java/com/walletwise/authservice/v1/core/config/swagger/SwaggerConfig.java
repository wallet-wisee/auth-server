package com.walletwise.authservice.v1.core.config.swagger;

import com.walletwise.authservice.v1.core.config.annotations.Generated;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Generated
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WALLET-WISE")
                        .version("v1")
                        .description("The Personal Budgeting App (WALLET-WISE) is your comprehensive financial companion,\n" +
                                "        designed to empower users in managing their finances effectively.\n" +
                                "        With intuitive features, it allows you to track expenses,\n" +
                                "        set realistic budgets, and achieve financial goals. Gain insights through visual reports,\n" +
                                "        receive personalized spending suggestions, and take control of your financial well-being.\n" +
                                "        Elevate your financial literacy with educational resources integrated within the app.\n" +
                                "        Start your journey to financial wellness with the Personal Budgeting App today."))
                .tags(
                        Arrays.asList(
                                new Tag().name("Authentication").description("Endpoints for authentication and registration.")
                        )
                );
    }
}
