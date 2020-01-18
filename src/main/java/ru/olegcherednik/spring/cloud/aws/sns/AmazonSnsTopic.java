package ru.olegcherednik.spring.cloud.aws.sns;

import lombok.Getter;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Getter
public final class AmazonSnsTopic {

    private final String id;
    private final AmazonResourceName arn;
    private final String endpoint;

    public AmazonSnsTopic(String id, String arn, String endpoint) {
        this.id = id;
        this.arn = arn == null ? null : AmazonResourceName.fromString(arn);
        this.endpoint = endpoint;
    }

    public String getTopicName() {
        return arn.getResourceType();
    }

    public String getTopicArn() {
        return arn.toString();
    }

    @Override
    public String toString() {
        return arn.toString();
    }

}
