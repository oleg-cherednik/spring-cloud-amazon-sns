package ru.olegcherednik.spring.cloud.aws.sns;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Getter
@Component
@ConfigurationProperties("amazon.sns")
@ConditionalOnProperty(prefix = "amazon.sns", name = "enabled", havingValue = "true")
public class AmazonSnsProperties {

    private Map<String, AmazonSnsTopic> topicById = Collections.emptyMap();

    public void setTopics(Map<String, Topic> topics) {
        Map<String, AmazonSnsTopic> map = topics.entrySet().stream()
                                                .map(entry -> {
                                                    String id = entry.getKey();
                                                    String arn = entry.getValue().getArn();
                                                    String endpoint = entry.getValue().getEndpoint();
                                                    return new AmazonSnsTopic(id, arn, endpoint);
                                                })
                                                .collect(Collectors.toMap(AmazonSnsTopic::getId, Function.identity()));

        topicById = map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    public AmazonSnsTopic getTopic(String topicId) {
        if (topicById.containsKey(topicId))
            return topicById.get(topicId);

        throw new RuntimeException(String.format("topicId '%s' was not found", topicId));
    }

    @Getter
    @Setter
    public static class Topic {

        private String arn;
        private String endpoint;
    }

}
