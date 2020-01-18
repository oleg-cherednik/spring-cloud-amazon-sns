package ru.olegcherednik.spring.cloud.aws.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.olegcherednik.spring.cloud.aws.sns.converters.PayloadConverter;

/**
 * @author Oleg Cherednik
 * @since 18.01.2020
 */
public class SnsLetterTopic extends AmazonSnsPublish {

    public SnsLetterTopic(AmazonSnsTopic topic, AmazonSNS sns, PayloadConverter converter, ObjectMapper objectMapper) {
        super(topic, sns, converter, objectMapper);
    }

}
