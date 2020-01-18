package ru.olegcherednik.spring.cloud.aws.sns.attributes;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.util.Assert;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@Getter
public final class NumberAttribute extends Attribute {

    public static final String TYPE = "Number";

    public static NumberAttribute of(String name, Number value) {
        Assert.notNull(value, "'value' should not be null");
        return new NumberAttribute(name, value);
    }

    public static NumberAttribute of(String name, String value) {
        try {
            Assert.notNull(value, "'value' should not be null");
            return of(name, NumberFormat.getInstance(Locale.US).parse(value));
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private NumberAttribute(String name, Number value) {
        super(TYPE, name, value);
    }

    @Override
    public Number getValue() {
        return (Number)super.getValue();
    }

    @Override
    protected MessageAttributeValue withData(MessageAttributeValue messageAttributeValue, ObjectMapper objectMapper) {
        return messageAttributeValue.withStringValue(NumberFormat.getInstance(Locale.US).format(getValue()));
    }
}
