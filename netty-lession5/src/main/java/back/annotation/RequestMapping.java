package back.annotation;

import io.netty.handler.codec.http.HttpMethod;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String method();
    String value();
}
