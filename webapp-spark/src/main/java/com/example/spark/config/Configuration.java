package com.example.spark.config;

import com.example.spark.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Created by prathyushsp on 18/07/16.
 */
public class Configuration {

    final static Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static String userSessionKey = "userID";



    public static class ConfigKey {
        public static final String WEBSPARK_PORT = "webspark.port";
        public static final String WEBSPARK_STATICFILES = "webspark.staticfiles";
    }

    private static Configuration instance;
    private static Properties properties;
    private static String ENVIRONMENT = "";

    public Configuration(){}

    public Configuration(String environment) {
        ENVIRONMENT = environment;
        getInstance();
    }

    public static Configuration getInstance()  {
        if (!isInited()) {
            logger.error("Environment not set.");
            System.exit(1);
        }
        if (instance == null) {
            instance = new Configuration();
            try {
                init();
            }
            catch(Exception ex){
                logger.error(ex.getMessage());
            }
        }
        return instance;
    }

    private static void init() throws Exception {
        logger.info("Looking up configuration for environment '" + ENVIRONMENT + "'...");
        InputStream stream = Configuration.class.getClassLoader()
                .getResourceAsStream(Configuration.ENVIRONMENT + ".config.properties");
        if (stream == null) {
            throw new FileNotFoundException(
                    "Configuration '" + Configuration.ENVIRONMENT + ".config.properties" + "' not found.");
        }
        properties = new Properties();
        properties.load(stream);
        logger.info("Configuration '" + Configuration.ENVIRONMENT + ".config.properties" + "' found.");
        HashMap<Object, Object> map = new HashMap<>();
        for (@SuppressWarnings("rawtypes")
                Map.Entry e : properties.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
    }

    private static boolean isInited() {
        if (Configuration.ENVIRONMENT == null || Configuration.ENVIRONMENT.trim().isEmpty())
            return false;
        return true;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }


}
