package ru.olegcherednik.spring.cloud.aws.sns.attributes;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@Getter
public final class StringArrayAttribute extends Attribute {

    public static final String TYPE = "String.Array";
    private static final TypeReference<List<String>> VALUE_TYPE_REF = new TypeReference<List<String>>() {
    };

    public static StringArrayAttribute of(String name, Collection<String> value) {
        Assert.notNull(value, "'value' should not be null");
        return new StringArrayAttribute(name, value);
    }

    public static StringArrayAttribute of(String name, String value, ObjectMapper objectMapper) {
        try {
            Assert.notNull(value, "'value' should not be null");
            return of(name, objectMapper.readValue(value, VALUE_TYPE_REF));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StringArrayAttribute(String name, Collection<String> value) {
        super(TYPE, name, value);
    }

    @Override
    public Collection<String> getValue() {
        return (Collection<String>)super.getValue();
    }

    @Override
    protected MessageAttributeValue withData(MessageAttributeValue messageAttributeValue, ObjectMapper objectMapper) {
        try {
            return messageAttributeValue.withStringValue(objectMapper.writeValueAsString(value));
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
