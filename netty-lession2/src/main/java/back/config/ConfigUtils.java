package back.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class ConfigUtils {

    private volatile static Config config = null;

    public static Config getConfig(){
        if(config == null){
            synchronized (ConfigUtils.class){
                if(config == null){
                    config = ConfigFactory.parseFile(new File("./netty-lession2/conf/application.conf"));
                }
            }
        }
        return config;
    }
}
