package ru.olegcherednik.spring.cloud.aws.sns.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.util.ClassUtils;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsMessageType;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.Attributes;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.Attribute;
import ru.olegcherednik.spring.cloud.aws.sns.attributes.MessageAttributes;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@RequiredArgsConstructor
public class AttributesArgumentResolver extends BaseArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Attributes.class) && ClassUtils.isAssignable(Map.class, parameter.getParameterType());
    }

    @Override
    protected Object resolveArgument(JsonNode content, HttpInputMessage request, Class<?> cls) {
        requireAnnotatedMessageType(content, AmazonSnsMessageType.NOTIFICATION, Attributes.class);

        if (!content.has("MessageAttributes"))
            return Collections.emptyMap();

        Iterator<Map.Entry<String, JsonNode>> it = content.findPath("MessageAttributes").fields();
        Map<String, Object> map = StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false)
                                               .map(this::createAttribute)
                                               .filter(Objects::nonNull)
                                               .collect(Collectors.toMap(Attribute::getName, Attribute::getValue));

        return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    protected final Attribute createAttribute(Map.Entry<String, JsonNode> entry) {
        JsonNode node = entry.getValue();
        String name = entry.getKey();
        String type = node.findPath("Type").asText();
        String value = node.findPath("Value").asText();
        return MessageAttributes.getAttribute(type, name, value, objectMapper);
    }

}
