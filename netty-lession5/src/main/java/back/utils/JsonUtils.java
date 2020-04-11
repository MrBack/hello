package back.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static ObjectMapper om = new ObjectMapper();

    public static String parse(Object obj){
        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
