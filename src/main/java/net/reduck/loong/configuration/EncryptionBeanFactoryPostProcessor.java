package net.reduck.loong.configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * @author Gin
 * @since 2023/5/22 11:07
 */
@RequiredArgsConstructor
public class EncryptionBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {
    private final ConfigurableEnvironment environment;

    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MutablePropertySources mutablePropertySources = environment.getPropertySources();

        String secretKey = environment.getProperty("reduck.loong.crypto.secret-key");

        if (secretKey == null) {
            return;
        }

        String prefix = environment.getProperty("reduck.loong.crypto.wrapper-prefix");
        String suffix = environment.getProperty("reduck.loong.crypto.wrapper-suffix");
        String encryptorName = environment.getProperty("reduck.loong.crypto.encryptor");

        prefix = StringUtils.hasText(prefix) ? prefix : "$ENC{";
        suffix = StringUtils.hasText(suffix) ? suffix : "}";
        encryptorName = StringUtils.hasText(encryptorName) ? encryptorName : AesEncryptor.class.getName();

        Encryptor encryptor = (Encryptor) Class.forName(encryptorName).newInstance();
        encryptor.initKey(PrivateKeyFinder.getSecretKey(secretKey));

        for (PropertySource<?> propertySource : mutablePropertySources) {
            if (propertySource instanceof OriginTrackedMapPropertySource) {
                mutablePropertySources.replace(propertySource.getName(),
                        new PropertySourceWrapper(propertySource
                                , encryptor
                                , new EncryptionWrapperDetector(prefix, suffix))
                );
            }
        }
    }

    @Override
    public int getOrder() {
        // 在jasypt之前处理
        return Ordered.LOWEST_PRECEDENCE - 200;
    }
}
