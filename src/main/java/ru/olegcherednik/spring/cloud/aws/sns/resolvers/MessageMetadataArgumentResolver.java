package ru.olegcherednik.spring.cloud.aws.sns.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonResourceName;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsHeader;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsMessageType;
import ru.olegcherednik.spring.cloud.aws.sns.MessageMetadata;
import ru.olegcherednik.spring.cloud.aws.sns.SubscriptionConfirmation;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
public class MessageMetadataArgumentResolver extends BaseArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return MessageMetadata.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    protected Object resolveArgument(JsonNode content, HttpInputMessage request, Class<?> cls) {
        requireMessageType(content, SubscriptionConfirmation.class, AmazonSnsMessageType.SUBSCRIPTION_CONFIRMATION,
                AmazonSnsMessageType.UNSUBSCRIBE_CONFIRMATION, AmazonSnsMessageType.NOTIFICATION);

        String subscriptionArn = AmazonSnsHeader.SUBSCRIPTION_ARN.header(request);

        return MessageMetadata.builder()
                              .type(AmazonSnsMessageType.parseType(content))
                              .messageId(UUID.fromString(content.get("MessageId").asText()))
                              .topicArn(AmazonResourceName.fromString(content.get("TopicArn").asText()))
                              .subscriptionArn(subscriptionArn == null ? null : AmazonResourceName.fromString(subscriptionArn))
                              .subscribeUrl(asText(content, "SubscribeURL"))
                              .timestamp(ZonedDateTime.parse(content.get("Timestamp").asText()))
                              .signatureVersion(asInt(content, "SignatureVersion"))
                              .signature(asText(content, "Signature"))
                              .signingCertUrl(asText(content, "SigningCertURL")).build();
    }

    private static String asText(JsonNode content, String fieldName) {
        return content.has(fieldName) ? content.get(fieldName).asText() : null;
    }

    private static Integer asInt(JsonNode content, String fieldName) {
        return content.has(fieldName) ? content.get(fieldName).asInt() : null;
    }

}

