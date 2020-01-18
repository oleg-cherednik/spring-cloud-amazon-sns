package ru.olegcherednik.spring.cloud.aws.sns.resolvers;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import ru.olegcherednik.spring.cloud.aws.sns.AmazonSnsMessageType;
import ru.olegcherednik.spring.cloud.aws.sns.UnsubscribeConfirmation;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@RequiredArgsConstructor
public class UnsubscribeConfirmationArgumentResolver extends BaseArgumentResolver {

    protected final AmazonSNS sns;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UnsubscribeConfirmation.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    protected Object resolveArgument(JsonNode content, HttpInputMessage request, Class<?> cls) {
        requireMessageType(content, UnsubscribeConfirmation.class, AmazonSnsMessageType.UNSUBSCRIBE_CONFIRMATION);

        String topicArn = content.get("TopicArn").asText();
        String token = content.get("Token").asText();
        return new UnsubscribeConfirmationImpl(topicArn, token);
    }

    @RequiredArgsConstructor
    private final class UnsubscribeConfirmationImpl implements UnsubscribeConfirmation {

        private final String topicArn;
        private final String token;

        @Override
        public void apply() {
            sns.confirmSubscription(topicArn, token);
        }

    }

}

