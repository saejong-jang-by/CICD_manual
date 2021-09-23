package dao;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private static Connection connection;
    private final MongoClient mongoClient;

    public Connection() {
        this.mongoClient = getMongoClient();
    }

    // singleton
    public static Connection getInstance(){
        if(connection == null) connection = new Connection();
        return connection;
    }

    private static MongoClient getMongoClient() {
        // Set-up SSL
        try {
            configureSSL();
        } catch (IOException e) {
            LOGGER.error("Error which finding the trust store");
            e.printStackTrace();
        }
        String connectionString = String.format(
                AppConfig.DB_CONNECTION_STRING_TEMPLATE.getValue(),
                AppConfig.DB_USER.getValue(),
                AppConfig.DB_PASSWORD.getValue(),
                AppConfig.DB_CLUSTER_ENDPOINT.getValue(),
                AppConfig.DB_SSL.getValue(),
                AppConfig.DB_REPLICATE_SET.getValue(),
                AppConfig.DB_READ_PREFERENCE.getValue(),
                AppConfig.DB_RETRY_WRITES.getValue());
        MongoClient mongoClient = MongoClients.create(connectionString);

        mongoClient.listDatabaseNames().forEach(printConsumer);
        LOGGER.info("Connected to DocumentDB Cluster");
        return mongoClient;
    }

    private static void configureSSL() throws IOException {
        LOGGER.info("Configuring SSL... ");
        Files.walk(Paths.get("/opt"), 4)
                .map(Path::getFileName)
                .map(Path::toString)
                .forEach(printConsumer);
        String trustStore = "TRUST_STORE";
        String trustStorePassword = "TRUST_STORE_PASSWORD";
        LOGGER.info(trustStore);
        LOGGER.info(trustStorePassword);

        if (Files.notExists(Paths.get(trustStore)))
            throw new DocDBRestApiException("RDS CA Certificate file could not found. Aborting!");

        LOGGER.info("Trust Store exists!");
        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    }

    private static final Consumer<String> printConsumer = LOGGER::info;

    public MongoClient getClient() {
        return mongoClient;
    }
}
