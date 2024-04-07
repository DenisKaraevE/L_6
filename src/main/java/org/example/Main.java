package org.example;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

class ApplicationProperties {

    private static final String FILE_NAME = "/application.properties";

    private static ApplicationProperties INSTANCE;

    private final Properties properties = new Properties();

    private void init() {
        try (InputStream is = getClass().getResourceAsStream(FILE_NAME)) {
            if(Objects.nonNull(is))
                properties.load(is);
        } catch (IOException e) {
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        for (final String name: properties.stringPropertyNames())
            map.put(name, properties.getProperty(name));

        return map;
    }

    public static ApplicationProperties getInstance() {
        if(Objects.isNull(INSTANCE)) {
            INSTANCE = new ApplicationProperties();
            INSTANCE.init();
        }

        return INSTANCE;
    }
}

class DBProperties {

    private static final String URL = "database.url";
    private static final String USER = "database.user";
    private static final String PASSWORD = "database.password";

    private static DBProperties INSTANSE;

    private String url;
    private String user;
    private String password;

    private DBProperties() {}

    private void init(Properties properties) {
        url = properties.getProperty(URL);
        user = properties.getProperty(USER);
        password = properties.getProperty(PASSWORD);
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public static DBProperties getProperties() {
        if(Objects.isNull(INSTANSE)) {
            INSTANSE = new DBProperties();
            INSTANSE.init(ApplicationProperties.getInstance().getProperties());
        }

        return INSTANSE;
    }
}