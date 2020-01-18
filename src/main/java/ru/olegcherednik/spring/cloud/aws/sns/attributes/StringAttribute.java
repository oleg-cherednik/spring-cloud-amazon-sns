package ru.olegcherednik.spring.cloud.aws.sns.attributes;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@Getter
public final class StringAttribute extends Attribute {

    public static final String TYPE = "String";

    public static StringAttribute of(String name, String value) {
        Assert.notNull(value, "'value' should not be null");
        return new StringAttribute(name, value);
    }

    private StringAttribute(String name, String value) {
        super(TYPE, name, value);
    }

    @Override
    public String getValue() {
        return (String)super.getValue();
    }

    @Override
    protected MessageAttributeValue withData(MessageAttributeValue messageAttributeValue, ObjectMapper objectMapper) {
        return messageAttributeValue.withStringValue(getValue());
    }
}
