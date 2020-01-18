package ru.olegcherednik.spring.cloud.aws.sns.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
public class CompositePayloadConverter implements PayloadConverter {

    protected final List<BasePayloadConverter> converters;

    public CompositePayloadConverter(ObjectMapper objectMapper) {
        this(createMessageConverters(objectMapper));
    }

    public CompositePayloadConverter(List<BasePayloadConverter> converters) {
        this.converters = converters == null || converters.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(converters);
    }

    private static List<BasePayloadConverter> createMessageConverters(ObjectMapper objectMapper) {
        return Arrays.asList(
                new StringPayloadConverter(),
                new CharArrayPayloadConverter(),
                new ByteArrayPayloadConverter(),
                new JsonPayloadConverter(objectMapper));
    }

    @Override
    public <T> String writeBody(T payload, MessageAttributes attributes) {
        if (payload == null)
            return null;

        for (BasePayloadConverter converter : converters) {
            String body = converter.writeBody(payload, attributes);

            if (body != null)
                return body;
        }

        return null;
    }

    @Override
    public <T> T readBody(String body, Class<T> cls, MediaType mediaType) {
        if (body == null || body.isEmpty())
            return null;

        for (BasePayloadConverter converter : converters) {
            T payload = converter.readBody(body, cls, mediaType);

            if (payload != null)
                return payload;
        }

        throw new HttpMessageNotReadableException("Error converting notification message with payload:" + body);
    }

}
