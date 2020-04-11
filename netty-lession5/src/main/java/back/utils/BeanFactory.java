package back.utils;

import back.annotation.Controller;
import back.annotation.RequestMapping;
import back.utils.ActionBean;
import io.netty.handler.codec.http.HttpMethod;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    public static Map<String,Map<String, ActionBean>> map = new HashMap<>();

    public static void start(){
        Reflections reflections = new Reflections("back.controller");
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        if(controllers != null){
            controllers.stream().forEach(clazz -> {
                Controller annotation = clazz.getAnnotation(Controller.class);
                String rootUri = annotation.value();
                Object controllerBean = null;
                try {
                    controllerBean = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Method[] methods = clazz.getMethods();
                if(methods != null){
                    final Object obj = controllerBean;
                    for (Method method : methods){
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String httpMethod = rm.method();
                            HttpMethod httpMethod1 = HttpMethod.valueOf(httpMethod);
                            String methodUri = rm.value();
                            String uri = rootUri + methodUri;

                            map.compute(httpMethod, (k,v) ->{
                                if(v==null){
                                    v = new HashMap<>();
                                }
                                ActionBean actionBean = new ActionBean();
                                actionBean.setMethod(method);
                                actionBean.setBean(obj);
                                v.put(uri, actionBean);
                            return v;
                        });
                        }
                    }
                }
            });
        }
    }
}
