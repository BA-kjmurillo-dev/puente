package com.puente.web.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources(
        @PropertySource("classpath:service-crea-bp.properties")
)
@Getter
@Setter
@NoArgsConstructor
public class CreaBpProperties {
    @Value("${wsBp.username}")
    private String username;

    @Value("${wsBp.password}")
    private String password;

    @Value("${wsBp.url}")
    private String url;

    @Value("${wsBp.actionR}")
    private String actionR;

    @Value("${wsBp.actionI}")
    private String actionI;

    @Value("${wsBp.SenderBusinessSystemID}")
    private String SenderBusinessSystemID;
}
