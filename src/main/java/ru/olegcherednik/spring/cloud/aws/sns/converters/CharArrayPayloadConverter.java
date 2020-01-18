package ru.olegcherednik.spring.cloud.aws.sns.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@RequiredArgsConstructor
public class CharArrayPayloadConverter extends BasePayloadConverter {

    @Override
    protected <T> boolean canConvertFrom(T payload, MediaType mediaType) {
        return payload.getClass() == char[].class && (mediaType == null || isEqualsIgnoreCharset(MediaType.TEXT_PLAIN, mediaType));
    }

    @Override
    protected <T> String writeBodyInternal(T payload, MessageAttributes attributes) {
        attributes.addString(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return new String((char[])payload);
    }

    @Override
    protected <T> boolean canConvertTo(String body, Class<T> cls, MediaType mediaType) {
        return cls == char[].class && isEqualsIgnoreCharset(MediaType.TEXT_PLAIN, mediaType);
    }

    @Override
    protected <T> T readBodyInternal(String body, Class<T> cls, MediaType mediaType) {
        return (T)body.toCharArray();
    }

}
