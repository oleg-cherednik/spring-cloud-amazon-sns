package ru.olegcherednik.spring.cloud.aws.sns.converters;

import org.springframework.http.MediaType;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

import javax.validation.constraints.NotNull;

/**
 * @author Oleg Cherednik
 * @since 17.01.2020
 */
public interface PayloadConverter {

    <T> String writeBody(@NotNull T payload, @NotNull MessageAttributes attributes);

    <T> T readBody(@NotNull String body, @NotNull Class<T> cls, MediaType mediaType);

}
