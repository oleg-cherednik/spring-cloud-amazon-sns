package ru.olegcherednik.spring.cloud.aws.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

import java.util.Collection;

/**
 * @author Oleg Cherednik
 * @since 18.01.2020
 */
@RequiredArgsConstructor
public abstract class AmazonSnsPublish {

    protected final AmazonSnsTopic topic;
    protected final AmazonSNS sns;
    protected final PayloadConverter converter;
    protected final ObjectMapper objectMapper;
    protected final MessageAttributes attributes = new MessageAttributes();

    protected String subject;
    protected Object payload;

    protected <T> void send() {
        sns.publish(publishRequest(new PublishRequest()));
    }

    protected PublishRequest publishRequest(PublishRequest publishRequest) {
        publishRequest.setTopicArn(topic.getTopicArn());
        publishRequest.setMessage(converter.writeBody(payload, attributes));
        publishRequest.setSubject(subject);
        publishRequest.withMessageAttributes(attributes.getMessageAttributes(objectMapper));
        return publishRequest;
    }

    public void subject(String subject) {
        this.subject = subject;
    }

    public void payload(Object payload) {
        this.payload = payload;
    }

    public void addStringAttribute(String name, String value) {
        attributes.addString(name, value);
    }

    public void addStringArrayAttribute(String name, Collection<String> value) {
        attributes.addStringArray(name, value);
    }

    public void addNumberAttribute(String name, Number value) {
        attributes.addNumber(name, value);
    }

    @SuppressWarnings("MethodCanBeVariableArityMethod")
    public void addBinaryAttribute(String name, byte[] value) {
        attributes.addBinary(name, value);
    }

}
