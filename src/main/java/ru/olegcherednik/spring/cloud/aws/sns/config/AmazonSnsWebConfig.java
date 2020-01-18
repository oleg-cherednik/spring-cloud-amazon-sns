package ru.olegcherednik.spring.cloud.aws.sns.config;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsProperties;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;
import ru.olegcherednik.spring.cloud.aws.sns.resolvers.AttributesArgumentResolver;
import ru.olegcherednik.spring.cloud.aws.sns.resolvers.MessageArgumentResolver;
import ru.olegcherednik.spring.cloud.aws.sns.resolvers.MessageMetadataArgumentResolver;
import ru.olegcherednik.spring.cloud.aws.sns.resolvers.SubjectArgumentResolver;
import ru.olegcherednik.spring.cloud.aws.sns.resolvers.SubscriptionConfirmationArgumentResolver;
import ru.olegcherednik.spring.cloud.aws.sns.resolvers.UnsubscribeConfirmationArgumentResolver;

import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(AmazonSnsProperties.class)
@ConditionalOnClass(WebMvcConfigurer.class)
public class AmazonSnsWebConfig implements WebMvcConfigurer {

    protected final AmazonSNS sns;
    protected final PayloadConverter converter;
    protected final ObjectMapper objectMapper;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(addResolvers(new HandlerMethodArgumentResolverComposite()));
    }

    protected HandlerMethodArgumentResolverComposite addResolvers(HandlerMethodArgumentResolverComposite composite) {
        composite.addResolver(new MessageArgumentResolver(converter));
        composite.addResolver(new SubscriptionConfirmationArgumentResolver(sns));
        composite.addResolver(new UnsubscribeConfirmationArgumentResolver(sns));
        composite.addResolver(new MessageMetadataArgumentResolver());
        composite.addResolver(new SubjectArgumentResolver());
        composite.addResolver(new AttributesArgumentResolver(objectMapper));
        return composite;
    }

}
