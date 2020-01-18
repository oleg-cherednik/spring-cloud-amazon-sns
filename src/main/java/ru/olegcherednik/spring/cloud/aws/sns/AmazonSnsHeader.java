package ru.olegcherednik.spring.cloud.aws.sns;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum AmazonSnsHeader {

    MESSAGE_TYPE("x-amz-sns-message-type"),
    MESSAGE_ID("x-amz-sns-message-id"),
    TOPIC_ARN("x-amz-sns-topic-arn"),
    SUBSCRIPTION_ARN("x-amz-sns-subscription-arn");

    private final String id;

    public String header(HttpServletRequest request) {
        return request.getHeader(id);
    }

    public String header(HttpMessage request) {
        List<String> values = request.getHeaders().getValuesAsList(id);
        return values.isEmpty() ? null : values.iterator().next();
    }

}
