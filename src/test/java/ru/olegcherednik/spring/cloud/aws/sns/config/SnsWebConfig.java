package ru.olegcherednik.spring.cloud.aws.sns.config;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsProperties;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(AmazonSnsProperties.class)
public class SnsWebConfig extends AmazonSnsWebConfig {

    public SnsWebConfig(AmazonSNS sns, PayloadConverter converter, ObjectMapper objectMapper) {
        super(sns, converter, objectMapper);
    }

    @Override
    protected HandlerMethodArgumentResolverComposite addResolvers(HandlerMethodArgumentResolverComposite composite) {
        return super.addResolvers(composite);
    }
}
