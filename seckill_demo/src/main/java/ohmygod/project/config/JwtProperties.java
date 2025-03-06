package ohmygod.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;


import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String password;
    private Resource location;
    private String alias;
    private Duration tokenTTL = Duration.ofMinutes(10);
}
