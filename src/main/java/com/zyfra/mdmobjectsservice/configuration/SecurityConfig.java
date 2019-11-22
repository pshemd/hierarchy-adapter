package com.zyfra.mdmobjectsservice.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Configuration
@EnableResourceServer
@ConditionalOnProperty(value = "security.disable", havingValue = "false", matchIfMissing = true)
public class SecurityConfig {
}
