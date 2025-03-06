package ohmygod.project.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
    /**
     * 生成一个密钥对（KeyPair）。
     * 该方法使用提供的JwtProperties对象中的信息从KeyStore中加载密钥对。
     *
     * @param properties 包含KeyStore位置、密码和别名的JwtProperties对象
     * @return 返回生成的密钥对
     */
    @Bean
    public KeyPair keyPair(JwtProperties properties){
        // 创建KeyStoreKeyFactory对象，使用KeyStore的位置和密码
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(
                properties.getLocation(),
                properties.getPassword().toCharArray()
        );

        // 从KeyStore中加载密钥对，使用提供的别名和密码
        return keyStoreKeyFactory.getKeyPair(
                properties.getAlias(),
                properties.getPassword().toCharArray());
    }


}
