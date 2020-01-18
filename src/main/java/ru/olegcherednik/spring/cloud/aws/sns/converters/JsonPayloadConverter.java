package ru.olegcherednik.spring.cloud.aws.sns.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@RequiredArgsConstructor
public class JsonPayloadConverter extends BasePayloadConverter {

    private final ObjectMapper objectMapper;

    @Override
    protected <T> boolean canConvertFrom(T payload, MediaType mediaType) {
        JavaType javaType = objectMapper.constructType(payload.getClass());
        AtomicReference<Throwable> causeRef = new AtomicReference<>();
        return objectMapper.canDeserialize(javaType, causeRef) && (mediaType == null || isEqualsIgnoreCharset(MediaType.APPLICATION_JSON, mediaType));
    }

    @Override
    protected <T> String writeBodyInternal(T payload, MessageAttributes attributes) {
        try {
            attributes.addString(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            if (payload.getClass() == char[].class)
                return objectMapper.writeValueAsString(IntStream.range(0, ((char[])payload).length)
                                                                .mapToObj(i -> ((char[])payload)[i])
                                                                .collect(Collectors.toList()));

            if (payload.getClass() == byte[].class)
                return objectMapper.writeValueAsString(IntStream.range(0, ((byte[])payload).length)
                                                                .mapToObj(i -> ((byte[])payload)[i])
                                                                .collect(Collectors.toList()));

            return objectMapper.writeValueAsString(payload);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected <T> boolean canConvertTo(String body, Class<T> cls, MediaType mediaType) {
        AtomicReference<Throwable> causeRef = new AtomicReference<>();
        return objectMapper.canSerialize(cls, causeRef) && isEqualsIgnoreCharset(MediaType.APPLICATION_JSON, mediaType);
    }

    @Override
    protected <T> T readBodyInternal(String body, Class<T> cls, MediaType mediaType) {
        try {
            if (cls == String.class)
                return (T)body;
            return objectMapper.readerFor(cls).readValue(body);
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
