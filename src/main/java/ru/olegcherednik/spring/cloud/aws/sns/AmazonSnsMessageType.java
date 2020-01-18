package ru.olegcherednik.spring.cloud.aws.sns;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum AmazonSnsMessageType {

    NULL(null),
    NOTIFICATION("Notification"),
    SUBSCRIPTION_CONFIRMATION("SubscriptionConfirmation"),
    UNSUBSCRIBE_CONFIRMATION("UnsubscribeConfirmation");

    private final String id;

    public static AmazonSnsMessageType parseType(JsonNode content) {
        return parseType(content.get("Type").asText());
    }

    public static AmazonSnsMessageType parseType(String id) {
        for (AmazonSnsMessageType type : values())
            if (type != NULL && type.id.equalsIgnoreCase(id))
                return type;

        return NULL;
    }

}
