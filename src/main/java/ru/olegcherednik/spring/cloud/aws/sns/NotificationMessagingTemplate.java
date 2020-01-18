package ru.olegcherednik.spring.cloud.aws.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

import java.util.Map;

@RequiredArgsConstructor
public class NotificationMessagingTemplate {

    private final AmazonSNS sns;
    private final Map<String, AmazonSnsTopic> topicsById;
    private final PayloadConverter converter;
    private final ObjectMapper objectMapper;

    public <T> void send(String topicId, T payload, String subject, MessageAttributes attributes) {
        String topicArn = getTopicArn(topicId);
        convertAndSend(topicArn, payload, subject, attributes);
    }

    private String getTopicArn(String topicId) {
        if (topicsById.containsKey(topicId))
            return topicsById.get(topicId).getTopicArn();

        throw new RuntimeException(String.format("topicId '%s' was not found", topicId));
    }

    private <T> void convertAndSend(String topicArn, T payload, String subject, MessageAttributes attributes) {
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setTopicArn(topicArn);
        publishRequest.setMessage(converter.writeBody(payload, attributes));
        publishRequest.setSubject(subject);
        publishRequest.withMessageAttributes(attributes.getMessageAttributes(objectMapper));
        sns.publish(publishRequest);
    }

}
