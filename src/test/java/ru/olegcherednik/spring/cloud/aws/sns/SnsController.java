package ru.olegcherednik.spring.cloud.aws.sns;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
public class SnsController extends AmazonSnsEndpoint {

    @Override
    public void subscriptionConfirmation(MessageMetadata metadata, SubscriptionConfirmation subscriptionConfirmation) {
        super.subscriptionConfirmation(metadata, subscriptionConfirmation);
    }

    @Override
    public void unsubscribeConfirmation(MessageMetadata metadata, UnsubscribeConfirmation unsubscribeConfirmation) {
        super.unsubscribeConfirmation(metadata, unsubscribeConfirmation);
    }

    @Override
    public void onNotification(MessageMetadata metadata) {
        // stub for topics without @SnsSubscription
        super.onNotification(metadata);
    }
}
