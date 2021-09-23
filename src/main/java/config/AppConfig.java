package config;

import java.util.ResourceBundle;

public enum AppConfig {
    DB_CONNECTION_STRING,
    DB_CONNECTION_STRING_TEMPLATE,
    DB_USER,
    DB_PASSWORD,
    DB_CLUSTER_ENDPOINT,
    DB_SSL,
    DB_REPLICATE_SET,
    DB_READ_PREFERENCE,
    DB_RETRY_WRITES,
    DB_NAME,
    DB_NAME_DEFAULT,
    COLL_DOC_METADATA,
    COLL_DOC_CATEGORIES_REF;

    ResourceBundle appConfig = ResourceBundle.getBundle("application");

    public String getValue() {
        return appConfig.getString(name());
    }
}
