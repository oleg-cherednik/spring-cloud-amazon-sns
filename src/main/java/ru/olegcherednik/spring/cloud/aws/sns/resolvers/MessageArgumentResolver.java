package ru.olegcherednik.spring.cloud.aws.sns.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsMessageType;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.Message;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@RequiredArgsConstructor
public class MessageArgumentResolver extends BaseArgumentResolver {

    private final PayloadConverter converter;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Message.class);
    }

    @Override
    protected Object resolveArgument(JsonNode content, HttpInputMessage request, Class<?> cls) throws IOException {
        requireAnnotatedMessageType(content, AmazonSnsMessageType.NOTIFICATION, Message.class);

        MediaType mediaType = getMediaType(content);
        String message = content.findPath("Message").asText();
        return converter.readBody(message, cls, mediaType);
    }

    private static MediaType getMediaType(JsonNode content) {
        JsonNode node = content.findPath("MessageAttributes").findPath(HttpHeaders.CONTENT_TYPE);
        String contentType = node.isObject() ? node.findPath("Value").asText() : null;
        return MediaType.parseMediaType(Optional.ofNullable(contentType).orElse(MediaType.TEXT_PLAIN_VALUE));
    }

}

