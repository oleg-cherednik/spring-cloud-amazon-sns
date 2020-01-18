package ru.olegcherednik.spring.cloud.aws.sns.attributes;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oleg Cherednik
 * @since 18.01.2020
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Attribute {

    protected final String type;
    protected final String name;
    protected final Object value;

    public MessageAttributeValue getMessageAttribute(ObjectMapper objectMapper) {
        return withData(new MessageAttributeValue().withDataType(type), objectMapper);
    }

    protected abstract MessageAttributeValue withData(MessageAttributeValue messageAttributeValue, ObjectMapper objectMapper);

}
