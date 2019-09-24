package com.withergate.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

/**
 * Thymeleaf config.
 */
@Configuration
public class ThymeleafConfig {

    /**
     * Thymeleaf dialect used for Spring security features.
     *
     * @return dialect
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
