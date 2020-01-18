package ru.olegcherednik.spring.cloud.aws.sns.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsMessageType;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 13.01.2020
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String NOTIFICATION_REQUEST = "NOTIFICATION_REQUEST";

    protected final MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        Assert.notNull(parameter, "'parameter' should not be null");

        JsonNode content = (JsonNode)webRequest.getAttribute(NOTIFICATION_REQUEST, RequestAttributes.SCOPE_REQUEST);
        HttpInputMessage request = copyHttpRequest(webRequest);

        if (content == null) {
            content = (JsonNode)messageConverter.read(JsonNode.class, request);
            webRequest.setAttribute(NOTIFICATION_REQUEST, content, RequestAttributes.SCOPE_REQUEST);
        }

        return resolveArgument(content, request, parameter.getParameterType());
    }

    protected abstract Object resolveArgument(JsonNode content, HttpInputMessage request, Class<?> cls) throws IOException;

    protected void requireAnnotatedMessageType(JsonNode content, AmazonSnsMessageType messageType, Class<? extends Annotation> cls) {
        if (AmazonSnsMessageType.parseType(content) != messageType)
            throw new IllegalArgumentException(String.format("'@%s' annotated parameter is only allowed for '%s' requests",
                    cls.getSimpleName(), messageType.getId()));
    }

    protected void requireMessageType(JsonNode content, Class<?> cls, AmazonSnsMessageType... messageTypes) {
        AmazonSnsMessageType actual = AmazonSnsMessageType.parseType(content);

        for (AmazonSnsMessageType messageType : messageTypes)
            if (messageType == actual)
                return;

        throw new IllegalArgumentException(String.format("'%s' is only allowed for requests: %s", cls.getSimpleName(),
                Arrays.stream(messageTypes).map(AmazonSnsMessageType::getId).collect(Collectors.joining(","))));
    }

    private static ServerHttpRequest copyHttpRequest(NativeWebRequest webRequest) throws IOException {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(servletRequest, "'servletRequest' should not be null");
        return new ServletServerHttpRequest(servletRequest);
    }

}
