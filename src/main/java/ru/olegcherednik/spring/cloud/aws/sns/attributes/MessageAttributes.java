package ru.olegcherednik.spring.cloud.aws.sns.attributes;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
public final class MessageAttributes implements Iterable<Attribute> {

    private final Map<String, Attribute> attributes = new LinkedHashMap<>();

    public void addString(String name, String value) {
        attributes.put(name, StringAttribute.of(name, value));
    }

    public void addStringArray(String name, Collection<String> value) {
        attributes.put(name, StringArrayAttribute.of(name, value));
    }

    public void addNumber(String name, Number value) {
        attributes.put(name, NumberAttribute.of(name, value));
    }

    @SuppressWarnings("MethodCanBeVariableArityMethod")
    public void addBinary(String name, byte[] value) {
        attributes.put(name, BinaryAttribute.of(name, value));
    }

    public MediaType getContentType() {
        StringAttribute attribute = (StringAttribute)attributes.get(HttpHeaders.CONTENT_TYPE);
        return attribute == null ? null : MediaType.parseMediaType(attribute.getValue());
    }

    public Map<String, MessageAttributeValue> getMessageAttributes(ObjectMapper objectMapper) {
        Map<String, MessageAttributeValue> map = new HashMap<>();
        attributes.values().forEach(attribute -> map.put(attribute.getName(), attribute.getMessageAttribute(objectMapper)));
        return map;
    }

    @Override
    public Iterator<Attribute> iterator() {
        return attributes.values().iterator();
    }

    @Override
    public String toString() {
        return "total: " + attributes.size();
    }

    public static Attribute getAttribute(String type, String name, String value, ObjectMapper objectMapper) {
        if (StringAttribute.TYPE.equalsIgnoreCase(type))
            return StringAttribute.of(name, value);
        if (StringArrayAttribute.TYPE.equalsIgnoreCase(type))
            return StringArrayAttribute.of(name, value, objectMapper);
        if (NumberAttribute.TYPE.equalsIgnoreCase(type))
            return NumberAttribute.of(name, value);
        if (BinaryAttribute.TYPE.equalsIgnoreCase(type))
            return BinaryAttribute.of(name, value);
        return null;
    }
}
