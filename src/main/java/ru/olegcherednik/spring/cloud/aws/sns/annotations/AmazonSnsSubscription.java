package ru.olegcherednik.spring.cloud.aws.sns.annotations;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Oleg Cherednik
 * @since 12.01.2020
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ResponseStatus(HttpStatus.NO_CONTENT)
public @interface AmazonSnsSubscription {

    /**
     * @return List of topicIds
     */
    String[] value();

}
