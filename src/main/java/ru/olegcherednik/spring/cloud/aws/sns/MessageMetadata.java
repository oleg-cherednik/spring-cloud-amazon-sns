package ru.olegcherednik.spring.cloud.aws.sns;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author Oleg Cherednik
 * @since 13.01.2020
 */
@Getter
@Builder
@SuppressWarnings("unused")
public final class MessageMetadata {

    private final AmazonSnsMessageType type;
    private final UUID messageId;
    private final AmazonResourceName topicArn;
    private final AmazonResourceName subscriptionArn;
    private final String subscribeUrl;
    private final ZonedDateTime timestamp;
    private final Integer signatureVersion;
    private final String signature;
    private final String signingCertUrl;

    public String getTopicName() {
        return topicArn.getResourceType();
    }

    public String getTopicArn() {
        return topicArn.toString();
    }

    public String getSubscriptionName() {
        return subscriptionArn.getResourceType();
    }

    public String getSubscriptionArn() {
        return subscriptionArn.toString();
    }

}
