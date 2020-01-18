package ru.olegcherednik.spring.cloud.aws.sns;

import org.springframework.stereotype.Service;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.AmazonSnsSubscription;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.Attributes;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.Message;
import ru.olegcherednik.spring.cloud.aws.sns.annotations.Subject;

import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 15.01.2020
 */
@Service
public class SnsConsumerService {

    @AmazonSnsSubscription("letter")
    public void onLetterUpdate(MessageMetadata metadata, @Attributes Map<String, Object> attributes,
            @Subject String subject, @Message String payload) {
        System.out.println("---------- letter ----------");
        System.out.println("Subject: " + subject);
        System.out.println();
        System.out.println(String.join(",", payload));
        System.out.println();
        System.out.println("Attributes");
        attributes.forEach((name, value) -> System.out.println("name: " + name + ", value: " + value));
        System.out.println("---------- -------------- ----------");
    }

    @AmazonSnsSubscription("digit")
    public void onDigitUpdate(MessageMetadata metadata, @Attributes Map<String, Object> attributes,
            @Subject String subject, @Message String payload) {
        System.err.println(String.format("topic: 'digit', subject: '%s': %s", subject, payload));
    }

}
