package ru.olegcherednik.spring.cloud.aws.sns.converters;

import com.amazonaws.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 16.01.2020
 */
@RequiredArgsConstructor
public class ByteArrayPayloadConverter extends BasePayloadConverter {

    @Override
    protected <T> boolean canConvertFrom(T payload, MediaType mediaType) {
        return payload.getClass() == byte[].class && (mediaType == null || isEqualsIgnoreCharset(MediaType.TEXT_PLAIN, mediaType)
                || isEqualsIgnoreCharset(MediaType.APPLICATION_OCTET_STREAM, mediaType));
    }

    @Override
    protected <T> String writeBodyInternal(T payload, MessageAttributes attributes) {
        MediaType mediaType = attributes.getContentType();

        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
            attributes.addString(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        }

        if (isEqualsIgnoreCharset(MediaType.TEXT_PLAIN, mediaType)) {
            Charset charset = Optional.ofNullable(mediaType.getCharset()).orElse(StandardCharsets.UTF_8);
            return new String((byte[])payload, charset);
        }

        if (isEqualsIgnoreCharset(MediaType.APPLICATION_OCTET_STREAM, mediaType))
            return Base64.encodeAsString((byte[])payload);

        return null;
    }

    @Override
    protected <T> boolean canConvertTo(String body, Class<T> cls, MediaType mediaType) {
        return cls == byte[].class && (isEqualsIgnoreCharset(MediaType.TEXT_PLAIN, mediaType)
                || isEqualsIgnoreCharset(MediaType.APPLICATION_OCTET_STREAM, mediaType));
    }

    @Override
    protected <T> T readBodyInternal(String body, Class<T> cls, MediaType mediaType) {
        if (isEqualsIgnoreCharset(MediaType.TEXT_PLAIN, mediaType))
            return (T)body.getBytes(StandardCharsets.UTF_8);

        if (isEqualsIgnoreCharset(MediaType.APPLICATION_OCTET_STREAM, mediaType))
            return (T)Base64.decode(body);

        return null;
    }

}
