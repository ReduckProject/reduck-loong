package net.reduck.loong;

import net.reduck.loong.configuration.EncryptionBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Gin
 * @since 2023/5/22 11:24
 */
@Configuration
@ConditionalOnProperty(prefix = "reduck.loong.crypto", name = "enabled", matchIfMissing = true)
public class PropertiesCryptoAutoConfiguration {

    @Bean
    public static EncryptionBeanFactoryPostProcessor encryptionBeanPostProcessor(final ConfigurableEnvironment environment) {
        return new EncryptionBeanFactoryPostProcessor(environment);
    }
}
