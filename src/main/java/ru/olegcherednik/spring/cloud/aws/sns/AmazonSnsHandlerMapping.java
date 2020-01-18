package ru.olegcherednik.spring.cloud.aws.sns;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.AmazonSnsSubscription;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
public class AmazonSnsHandlerMapping extends AbstractHandlerMethodMapping<AmazonSnsMapping> {

    private final Set<String> endpoints;
    private final Map<String, AmazonSnsTopic> topicsById;

    public AmazonSnsHandlerMapping(Map<String, AmazonSnsTopic> topics) {
        endpoints = getEndpoints(topics.values());
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        topicsById = topics;
        setOrder(0);
    }

    private static Set<String> getEndpoints(Collection<AmazonSnsTopic> topics) {
        Set<String> endpoints = topics.stream()
                                      .map(AmazonSnsTopic::getEndpoint)
                                      .collect(Collectors.toSet());
        return endpoints.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(endpoints);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AmazonSnsEndpoint.class.isAssignableFrom(beanType) || AnnotatedElementUtils.hasAnnotation(beanType, Component.class);
    }

    @Override
    protected AmazonSnsMapping getMappingForMethod(Method method, Class<?> handlerType) {
        AmazonSnsMapping mapping = createSubscriptionMapper(method);
        mapping = mapping == null ? createSubscriptionStubMapper(method) : mapping;
        mapping = mapping == null ? createSubscriptionConfirmationMapper(method) : mapping;
        return mapping == null ? createUnsubscribeConfirmationMapper(method) : mapping;
    }

    private AmazonSnsMapping createSubscriptionMapper(Method method) {
        AmazonSnsSubscription subscription = AnnotatedElementUtils.findMergedAnnotation(method, AmazonSnsSubscription.class);
        return subscription == null ? null : AmazonSnsMapping.topicSubscription(endpoints, getTopicsArn(subscription));
    }

    private Set<String> getTopicsArn(AmazonSnsSubscription subscription) {
        return requireRegisteredTopicNames(subscription.value()).stream()
                                                                .map(topicsById::get)
                                                                .map(AmazonSnsTopic::getTopicArn)
                                                                .collect(Collectors.toSet());
    }

    private AmazonSnsMapping createSubscriptionStubMapper(Method method) {
        if (!AmazonSnsEndpoint.class.isAssignableFrom(method.getDeclaringClass()))
            return null;
        if (!"onNotification".equalsIgnoreCase(method.getName()))
            return null;
        return AmazonSnsMapping.subscriptionStub(endpoints);
    }

    private AmazonSnsMapping createSubscriptionConfirmationMapper(Method method) {
        if (!AmazonSnsEndpoint.class.isAssignableFrom(method.getDeclaringClass()))
            return null;
        if (!"subscriptionConfirmation".equalsIgnoreCase(method.getName()))
            return null;
        return AmazonSnsMapping.subscriptionConfirmation(endpoints);
    }

    private AmazonSnsMapping createUnsubscribeConfirmationMapper(Method method) {
        if (!AmazonSnsEndpoint.class.isAssignableFrom(method.getDeclaringClass()))
            return null;
        if (!"unsubscribeConfirmation".equalsIgnoreCase(method.getName()))
            return null;
        return AmazonSnsMapping.unsubscribeConfirmation(endpoints);
    }

    private Set<String> requireRegisteredTopicNames(String... topicNames) {
        if (topicNames == null || topicNames.length == 0)
            return Collections.emptySet();

        for (String topicName : topicNames)
            if (!topicsById.containsKey(topicName))
                throw new RuntimeException("Amazon SNS topic '" + topicName + "' is not registered in property file");

        return Arrays.stream(topicNames).collect(Collectors.toSet());
    }

    @Override
    protected Set<String> getMappingPathPatterns(AmazonSnsMapping mapping) {
        return mapping.getEndpoints();
    }

    @Override
    protected AmazonSnsMapping getMatchingMapping(AmazonSnsMapping mapping, HttpServletRequest request) {
        return mapping.getMatchingCondition(request);
    }

    @Override
    protected Comparator<AmazonSnsMapping> getMappingComparator(HttpServletRequest request) {
        return (one, two) -> one.compareTo(two, request);
    }

}
