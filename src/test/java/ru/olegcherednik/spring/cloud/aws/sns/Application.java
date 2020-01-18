package ru.olegcherednik.spring.cloud.aws.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@SpringBootApplication
public class Application {

    public static void main(String... args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        sendLetterMessage(context);
//        sendLetterMessage(context);
    }

    public static void sendLetterMessage(ConfigurableApplicationContext context) {
        SnsLetterTopic topic = context.getBean(SnsLetterTopic.class);

        topic.subject("subject: " + System.currentTimeMillis());
//        topic.payload("abcde".toCharArray());

        Map<String, Object> payload = new HashMap<>();
        payload.put("one-1", "one-2");
        payload.put("two-1", Collections.singleton("two-2"));
        payload.put("three-1", Collections.singletonMap("three-2", "three-3"));
        topic.payload(payload);

        //        topic.payload("abcdef".getBytes(StandardCharsets.UTF_8));

        topic.addStringAttribute("string-key", "string-value");
        topic.addStringArrayAttribute("string.array-key", Arrays.asList("one", "two", "three"));
        topic.addNumberAttribute("number-key", 666);
        topic.addBinaryAttribute("binary-key", "abcde".getBytes(StandardCharsets.UTF_8));
//        topic.addStringAttribute(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

        topic.send();
    }

}
