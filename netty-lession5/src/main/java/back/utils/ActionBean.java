package back.utils;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ActionBean {
    private Method method;
    private Object bean;
}
