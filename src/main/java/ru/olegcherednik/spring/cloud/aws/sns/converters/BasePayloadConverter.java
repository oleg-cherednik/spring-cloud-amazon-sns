package ru.olegcherednik.spring.cloud.aws.sns.converters;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
public abstract class BasePayloadConverter implements PayloadConverter {

    @Override
    public <T> String writeBody(T payload, MessageAttributes attributes) {
        Assert.notNull(payload, "'payload' must not be null");
        return canConvertFrom(payload, attributes.getContentType()) ? writeBodyInternal(payload, attributes) : null;
    }

    protected abstract <T> String writeBodyInternal(T payload, MessageAttributes attributes);

    protected abstract <T> boolean canConvertFrom(T payload, MediaType mediaType);

    @Override
    public <T> T readBody(String body, Class<T> cls, MediaType mediaType) {
        Assert.notNull(body, "'body' must not be null");
        return canConvertTo(body, cls, mediaType) ? readBodyInternal(body, cls, mediaType) : null;
    }

    protected abstract <T> T readBodyInternal(String body, Class<T> cls, MediaType mediaType);

    protected abstract <T> boolean canConvertTo(String body, Class<T> cls, MediaType mediaType);

    protected boolean isEqualsIgnoreCharset(MediaType one, MediaType two) {
        return one.getType().equals(two.getType()) && one.getSubtype().equals(two.getSubtype());
    }

}
