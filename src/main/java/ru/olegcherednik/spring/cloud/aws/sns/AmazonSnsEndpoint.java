package ru.olegcherednik.spring.cloud.aws.sns;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@SuppressWarnings("unused")
public class AmazonSnsEndpoint {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void subscriptionConfirmation(MessageMetadata metadata, SubscriptionConfirmation subscriptionConfirmation) {
        subscriptionConfirmation.apply();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribeConfirmation(MessageMetadata metadata, UnsubscribeConfirmation unsubscribeConfirmation) {
        unsubscribeConfirmation.apply();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void onNotification(MessageMetadata metadata) {
    }

}
