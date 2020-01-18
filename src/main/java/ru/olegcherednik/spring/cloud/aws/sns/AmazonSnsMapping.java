package ru.olegcherednik.spring.cloud.aws.sns;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Getter
public class AmazonSnsMapping implements RequestCondition<AmazonSnsMapping> {

    protected final Set<String> endpoints;
    protected final AmazonSnsMessageType messageType;

    public static AmazonSnsMapping topicSubscription(Set<String> endpoints, Set<String> topicsArn) {
        return new TopicSubscription(endpoints, topicsArn);
    }

    public static AmazonSnsMapping subscriptionStub(Set<String> endpoints) {
        return new SubscriptionStub(endpoints);
    }

    public static AmazonSnsMapping subscriptionConfirmation(Set<String> endpoints) {
        return new SubscriptionConfirmation(endpoints);
    }

    public static AmazonSnsMapping unsubscribeConfirmation(Set<String> endpoints) {
        return new UnsubscribeConfirmation(endpoints);
    }

    protected AmazonSnsMapping(Set<String> endpoints, AmazonSnsMessageType messageType) {
        this.endpoints = normalize(endpoints);
        this.messageType = messageType;
    }

    private static Set<String> normalize(Set<String> endpoints) {
        endpoints = Optional.ofNullable(endpoints).orElse(Collections.emptySet()).stream()
                            .map(endpoint -> endpoint.charAt(0) == '/' ? endpoint : '/' + endpoint)
                            .map(String::toLowerCase)
                            .collect(Collectors.toSet());
        return endpoints.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(endpoints);
    }

    @Override
    public AmazonSnsMapping combine(AmazonSnsMapping mapping) {
        return mapping;
    }

    @Override
    public AmazonSnsMapping getMatchingCondition(HttpServletRequest request) {
        if (!HttpMethod.POST.matches(request.getMethod()))
            return null;
        if (!endpoints.contains(request.getRequestURI().toLowerCase()))
            return null;
        return getMatchingConditionInternal(request);
    }

    protected AmazonSnsMapping getMatchingConditionInternal(HttpServletRequest request) {
        String messageType = AmazonSnsHeader.MESSAGE_TYPE.header(request);
        return AmazonSnsMessageType.parseType(messageType) == this.messageType ? this : null;
    }

    @Override
    public final int compareTo(AmazonSnsMapping mapper, HttpServletRequest request) {
        int res = messageType.compareTo(mapper.messageType);

        if (res != 0)
            return res;
        if (mapper instanceof TopicSubscription)
            return 1;
        if (mapper instanceof SubscriptionConfirmation)
            return -1;
        if (mapper instanceof UnsubscribeConfirmation)
            return -1;
        if (mapper instanceof SubscriptionStub)
            return -1;

        return 1;
    }

    private static final class SubscriptionStub extends AmazonSnsMapping {

        private SubscriptionStub(Set<String> endpoints) {
            super(endpoints, AmazonSnsMessageType.NOTIFICATION);
        }

    }

    private static final class SubscriptionConfirmation extends AmazonSnsMapping {

        private SubscriptionConfirmation(Set<String> endpoints) {
            super(endpoints, AmazonSnsMessageType.SUBSCRIPTION_CONFIRMATION);
        }

    }

    private static final class UnsubscribeConfirmation extends AmazonSnsMapping {

        private UnsubscribeConfirmation(Set<String> endpoints) {
            super(endpoints, AmazonSnsMessageType.UNSUBSCRIBE_CONFIRMATION);
        }

    }

    private static final class TopicSubscription extends AmazonSnsMapping {

        private final Set<String> topicsArn;

        private TopicSubscription(Set<String> endpoints, Set<String> topicsArn) {
            super(endpoints, AmazonSnsMessageType.NOTIFICATION);
            //noinspection AssignmentOrReturnOfFieldWithMutableType
            this.topicsArn = topicsArn;
        }

        @Override
        protected AmazonSnsMapping getMatchingConditionInternal(HttpServletRequest request) {
            AmazonSnsMapping mapping = super.getMatchingConditionInternal(request);

            if (mapping != null)
                mapping = topicsArn.contains(AmazonSnsHeader.TOPIC_ARN.header(request)) ? this : null;

            return mapping;
        }
    }
}
