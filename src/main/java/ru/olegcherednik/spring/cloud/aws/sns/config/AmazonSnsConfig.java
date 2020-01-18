package ru.olegcherednik.spring.cloud.aws.sns.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsEndpoint;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsHandlerMapping;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsProperties;
import ru.olegcherednik.spring.cloud.aws.sns.converters.CompositePayloadConverter;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(AmazonSnsProperties.class)
public class AmazonSnsConfig {

    @Bean
    @ConditionalOnMissingBean({ AWSCredentialsProvider.class, AmazonSNS.class })
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    @ConditionalOnMissingBean(AmazonSNS.class)
    public AmazonSNS sns(AWSCredentialsProvider credentialsProvider) {
        return AmazonSNSClientBuilder.standard().withCredentials(credentialsProvider).build();
    }

    @Bean
    @ConditionalOnMissingBean(AmazonSnsHandlerMapping.class)
    public AmazonSnsHandlerMapping amazonSnsHandlerMapping(AmazonSnsProperties properties) {
        return new AmazonSnsHandlerMapping(properties.getTopicById());
    }

    @Bean
    @ConditionalOnMissingBean(AmazonSnsEndpoint.class)
    public AmazonSnsEndpoint amazonSnsEndpoint() {
        return new AmazonSnsEndpoint();
    }

    @Bean
    @ConditionalOnMissingBean(PayloadConverter.class)
    public PayloadConverter payloadConverter(ObjectMapper objectMapper) {
        return new CompositePayloadConverter(objectMapper);
    }

}
