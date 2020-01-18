package ru.olegcherednik.spring.cloud.aws.sns.attributes;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.util.Assert;

import java.nio.ByteBuffer;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@Getter
@SuppressWarnings("MethodCanBeVariableArityMethod")
public final class BinaryAttribute extends Attribute {

    public static final String TYPE = "Binary";

    public static BinaryAttribute of(String name, byte[] value) {
        Assert.notNull(value, "'value' should not be null");
        return new BinaryAttribute(name, value);
    }

    public static BinaryAttribute of(String name, String value) {
        Assert.notNull(value, "'value' should not be null");
        return of(name, Base64.decode(value));
    }

    private BinaryAttribute(String name, byte[] value) {
        super(TYPE, name, value);
    }

    @Override
    public byte[] getValue() {
        return (byte[])super.getValue();
    }

    @Override
    protected MessageAttributeValue withData(MessageAttributeValue messageAttributeValue, ObjectMapper objectMapper) {
        return messageAttributeValue.withBinaryValue(ByteBuffer.wrap(getValue()));
    }
}
