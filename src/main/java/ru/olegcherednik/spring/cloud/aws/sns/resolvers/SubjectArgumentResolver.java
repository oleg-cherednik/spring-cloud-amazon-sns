package ru.olegcherednik.spring.cloud.aws.sns.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.util.ClassUtils;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsMessageType;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.Subject;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
public class SubjectArgumentResolver extends BaseArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Subject.class) && ClassUtils.isAssignable(String.class, parameter.getParameterType());
    }

    @Override
    protected Object resolveArgument(JsonNode content, HttpInputMessage request, Class<?> cls) {
        requireAnnotatedMessageType(content, AmazonSnsMessageType.NOTIFICATION, Subject.class);
        return content.findPath("Subject").asText();
    }

}
