package app.getxray.xray.testng.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A Custom Annotation to inject additional information into a TestNG Test
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface XrayTest {

    /**
     Summary for the Test issue

     @return a {@code String} containing the Summary's text
    */
    String summary() default "";

    /**
     Description for the Test issue

     @return a {@code String} containing the Description's text
    */
    String description() default "";
    
    /**
     issue key of the Test issue to report the results to

     @return a {@code String} containing the Summary's text
    */
    String key() default "";

    /**
     labels, delimited by space, for the Test issue

     @return a {@code String} containing the labels (one or more)
    */
    String labels() default "";
}
