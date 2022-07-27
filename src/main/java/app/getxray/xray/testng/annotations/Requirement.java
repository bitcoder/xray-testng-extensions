package app.getxray.xray.testng.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A Custom Annotation to inject additional information into a TestNG Test, related to the covered requirement
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement {

    /**
     issue key of the requirement (e.g., Story, Epic) to link this test with

     @return a {@code String} containing the requirement's issue key
    */
    String key() default "";

}
