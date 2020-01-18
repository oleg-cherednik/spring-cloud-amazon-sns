package ru.olegcherednik.spring.cloud.aws.sns.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsHandlerMapping;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsProperties;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsTopic;
import ru.olegcherednik.spring.cloud.aws.sns.SnsController;
import ru.olegcherednik.spring.cloud.aws.sns.SnsLetterTopic;
import ru.olegcherednik.spring.cloud.aws.sns.converters.CompositePayloadConverter;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Configuration(proxyBeanMethods = false)
public class SnsConfig {

    //    @Bean
    public AWSCredentialsProvider credentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    public AmazonSNS sns(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonSNSClientBuilder.standard().withCredentials(awsCredentialsProvider).build();
    }

    //    @Bean
    public PayloadConverter payloadConverter(ObjectMapper objectMapper) {
        return new CompositePayloadConverter(objectMapper);
    }

    //    @Bean
    public AmazonSnsHandlerMapping amazonSNSHandlerMapping(AmazonSnsProperties properties) {
        return new AmazonSnsHandlerMapping(properties.getTopicById());
    }

    //    @Bean
    public SnsController amazonSnsEndpoint() {
        return new SnsController();
    }

    @Bean("snsLetterPublish")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SnsLetterTopic snsLetterPublish(AmazonSNS sns, AmazonSnsProperties properties, PayloadConverter converter, ObjectMapper objectMapper) {
        AmazonSnsTopic topic = properties.getTopic("letterId");
        return new SnsLetterTopic(topic, sns, converter, objectMapper);
    }

}
