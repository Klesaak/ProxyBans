package ua.klesaak.proxybans.utils.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageField {
    String key() default "";
    String[] defaultMessage() default "Default minecraft-json message.";
    boolean withoutQuotes() default false;
}
