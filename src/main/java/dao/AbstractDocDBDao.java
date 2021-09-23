package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class AbstractDocDBDao {
    protected MongoClient mongoClient;
    protected String databaseName;
    protected MongoDatabase database;

    protected AbstractDocDBDao(MongoClient mongoClient, String databaseName) {
        this.mongoClient = mongoClient;
        this.databaseName = databaseName;
        this.database = this.mongoClient.getDatabase(databaseName);
    }

}
