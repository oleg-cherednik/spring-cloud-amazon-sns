package ru.olegcherednik.spring.cloud.aws.sns.converters;

import org.springframework.http.MediaType;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

/**
 * @author Oleg Cherednik
 * @since 17.01.2020
 */
public interface PayloadConverter {

    <T> String writeBody(T payload, MessageAttributes attributes);

    <T> T readBody(String body, Class<T> cls, MediaType mediaType);

}
