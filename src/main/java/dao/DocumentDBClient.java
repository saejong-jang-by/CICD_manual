package dao;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DocumentDBClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDBClient.class);

    static final Set<String> possibleErrorList = populateExceptionList();
    static final int MAX_RETRIES_READS = 1;
    static final Long BASE = 1000L;
    static final Long CAP = 7000L;
    static final Integer MAX_RETRIES_FOR_WRITES = 4;

    public static void main(String[] args) throws InterruptedException {
        MongoCollection<Document> collection = null;

        try {
            // get client object using Singleton pattern
            MongoClient mongoClient = Connection.getInstance().getClient();
            // get database to use
            MongoDatabase database = mongoClient.getDatabase("document_repo");
            // get list of Collections
            listCollections(database);
            // get to collection from database
            collection = database.getCollection("doc_categories_ref");
            // create data
            demoWritesWithRetry(collection, CRUDOperations.CREATE);
            // read data
            demoReadsWithRetry(collection);
            // Update data
            demoWritesWithRetry(collection, CRUDOperations.UPDATE);
            // delete data
            demoWritesWithRetry(collection, CRUDOperations.DELETE);
            // Transactions
            demoWritesWithRetry(collection, CRUDOperations.TRANSACTION);
        } catch (Exception e) {
            LOGGER.error("Exception Occurred " + e.getMessage() + e.getClass());
            e.printStackTrace();
        }
    }

    // List Collections
    private static void listCollections(MongoDatabase database) {
        MongoIterable<String> list = database.listCollectionNames();
        list.forEach(name -> LOGGER.info("Collection: " + name));

        int n = 0;
        for (String name : list) {
            LOGGER.info("Collection - " + ++n + ": " + name);
        }
    }

    private static void demoReadsWithRetry(MongoCollection<Document> collection) throws InterruptedException {
        MongoCursor<Document> cursor = null;
        int retryCount = 0;
        while (retryCount <= MAX_RETRIES_READS) { // For reads , using secondary preferred is recommended and a single retry should redirect the read to the appropriate secondary, avoiding need for multiple retries
            try {
                cursor = performReadOperation(collection);

                while (cursor != null && cursor.hasNext()) {
                    System.out.println("read");
                    System.out.println(cursor.next());
                }
                return;
            } catch (Exception exception) {
                if (!isRetryEligible(possibleErrorList, exception)) {
                    LOGGER.error("Error  Occurred while reading data from namespace " + collection.getNamespace() + ". Exception not eligible for retry and hence aborting");
                    throw exception;
                } else {
                    LOGGER.info("Retrying read operation. Attempt number " + retryCount);
                    Thread.sleep(1 * 1000);
                    retryCount++;
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    private static void demoWritesWithRetry(MongoCollection<Document> collection, CRUDOperations operation) throws InterruptedException {
        List<Document> documentList = getDocumentsForBulkWrite();
        ObjectId operationID = new ObjectId();
        int retryCount = 0;
        while (retryCount <= MAX_RETRIES_FOR_WRITES) {
            try {
                switch (operation) {
                    case CREATE:
                        performWriteOperation(collection, documentList);
                        break;
                    case UPDATE:
                        performUpdateOperation(collection, operationID);
                        break;
                    case DELETE:
                        performDeleteOperation(collection);
                        break;
                    case TRANSACTION:
                        performTransactions();
                        break;
                }
                return;
            } catch (Exception exception) {

                if (!isRetryEligible(possibleErrorList, exception)) {
                    LOGGER.error("Error  Occurred while performing" + operation.name() + " operation to namespace " + collection.getNamespace() + ". Exception not eligible for retry and hence aborting. Exception is " + exception.getMessage());
                    throw exception;
                } else {
                    LOGGER.info("Retrying " + operation.name() + " operation. Attempt number " + retryCount + ". Exception is " + exception.getMessage());
                    Thread.sleep(randomWithRange(BASE, (long) Math.min(CAP, (Math.pow(2, retryCount)) * BASE)));
                    retryCount++;
                }
            }
        }
    }

    private static void performTransactions() {
        MongoClient mongoClient = Connection.getInstance().getClient();
        MongoDatabase database = mongoClient.getDatabase("documentDB");
        MongoCollection<Document> userProfileCollection = database.getCollection("userProfile");
        MongoCollection<Document> personalizationCollection = database.getCollection("personalization");

        ClientSession session = mongoClient.startSession();
        try {
            TransactionOptions txnOptions = TransactionOptions.builder()
                    .readPreference(ReadPreference.primary())
                    .writeConcern(WriteConcern.MAJORITY)
                    .build();
            session.startTransaction(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
            Document userProfile = getMockDocument();
            userProfileCollection.insertOne(userProfile);
            // Insert Personalization collection to cross sell or up sell
            Document personalizationRecord = new Document();
            personalizationRecord.put("_id", UUID.randomUUID());
            personalizationRecord.put("user_id", userProfile.get("_id"));
            List<String> campaignList = new ArrayList<String>();
            campaignList.add("CROSS_SELL");
            campaignList.add("UP_SELL");
            personalizationRecord.put("campaign", campaignList);
            personalizationRecord.put("notificationDate", new Date());
            personalizationCollection.insertOne(personalizationRecord);
            session.commitTransaction();
        } catch (Exception exception) {
            LOGGER.error("Error Occurred while performing transaction .Exception is " + exception.getMessage());
            session.abortTransaction();
            throw exception;
        } finally {
            session.close();
        }

    }

    private static MongoCursor<Document> performReadOperation(MongoCollection<Document> collection) {
        Document searchQuery = new Document();
        searchQuery.put("age", 25);
        MongoCursor<Document> cursor = collection.find(searchQuery).batchSize(5).iterator();
        return cursor;
    }

    private static void performWriteOperation(MongoCollection<Document> collection, List<Document> documentList) {
        collection.insertMany(documentList, new InsertManyOptions().ordered(true));
    }

    private static void performUpdateOperation(MongoCollection<Document> collection, ObjectId operationID) {
        Document query = new Document();
        query.put("_id", 58702580);
        prepareForUpdateOperation(collection, query, operationID);
        performUpdateOperation(collection, query, operationID);
    }

    private static ObjectId prepareForUpdateOperation(MongoCollection<Document> collection, Document query, ObjectId operationID) {
        Document newDocument = new Document();
        newDocument.put("pendingOperations", operationID);

        Document updateObject = new Document();
        updateObject.put("$addToSet", newDocument);
        collection.updateOne(query, updateObject, new UpdateOptions().upsert(true));
        return operationID;
    }

    private static void performUpdateOperation(MongoCollection<Document> collection, Document query, ObjectId operationID) {
        query.put("pendingOperations", operationID);
        Document incrementAge = new Document();
        incrementAge.put("age", 1);

        Document removePendingOperations = new Document();
        removePendingOperations.put("pendingOperations", operationID);
        Document updateObject = new Document();
        updateObject.put("$inc", incrementAge);
        updateObject.put("$pull", removePendingOperations);
        collection.updateOne(query, updateObject, new UpdateOptions().upsert(false));
    }

    private static void performDeleteOperation(MongoCollection<Document> collection) {
        Document deleteQuery = new Document();
        deleteQuery.put("name", "Shaun Feest");
        collection.deleteOne(deleteQuery);
    }

    private static boolean isRetryEligible(Set<String> possibleErrorList, Exception exception) {
        boolean canRetry = false;

        if (possibleErrorList.contains(exception.getClass().getName()) || possibleErrorList.contains(exception.getMessage())) {
            canRetry = true;
        }
        return canRetry;
    }

    private static Set<String> populateExceptionList() {
        Set<String> possibleErrorList = new HashSet<>();
        possibleErrorList.add(MongoSocketOpenException.class.getName());
        possibleErrorList.add(MongoSocketReadException.class.getName());
        possibleErrorList.add(MongoNotPrimaryException.class.getName());
        possibleErrorList.add(MongoNodeIsRecoveringException.class.getName());
        return possibleErrorList;
    }

    private static List<Document> getDocumentsForBulkWrite() {
        int docCount = 10;
        List<Document> documentList = new ArrayList<Document>();

        Document document;
        for (int i = 0; i < docCount; i++) {
            document = getMockDocument();
            documentList.add(document);
        }
        return documentList;
    }

    private static Document getMockDocument() {
        Document document = new Document();
        document.put("_id", UUID.randomUUID());
        document.put("name", "testname");
        document.put("address", "test address");
        document.put("age", 22);
        return document;
    }

    private static long randomWithRange(long min, long max) {
        long range = (max - min) + 1;
        return (long) (Math.random() * range) + min;
    }
}