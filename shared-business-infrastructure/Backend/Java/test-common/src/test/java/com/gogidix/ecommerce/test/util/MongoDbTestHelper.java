package com.gogidix.ecommerce.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.function.Consumer;

/**
 * Utility class providing helper methods for MongoDB-related test operations.
 */
public class MongoDbTestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Clears all collections in the specified database.
     *
     * @param mongoTemplate the MongoTemplate to use
     */
    public static void clearAllCollections(MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().listCollectionNames()
            .forEach(collectionName -> {
                MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
                collection.deleteMany(new Document());
            });
    }

    /**
     * Clears a specific collection in the database.
     *
     * @param mongoTemplate the MongoTemplate to use
     * @param collectionName the name of the collection to clear
     */
    public static void clearCollection(MongoTemplate mongoTemplate, String collectionName) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        collection.deleteMany(new Document());
    }

    /**
     * Counts documents in a specific collection.
     *
     * @param mongoTemplate the MongoTemplate to use
     * @param collectionName the name of the collection
     * @return the count of documents in the collection
     */
    public static long countDocuments(MongoTemplate mongoTemplate, String collectionName) {
        return mongoTemplate.getCollection(collectionName).countDocuments();
    }

    /**
     * Executes a consumer function with a MongoCollection for the specified collection name.
     *
     * @param mongoTemplate the MongoTemplate to use
     * @param collectionName the name of the collection
     * @param consumer the consumer to execute with the collection
     */
    public static void withCollection(MongoTemplate mongoTemplate, String collectionName,
                                     Consumer<MongoCollection<Document>> consumer) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        consumer.accept(collection);
    }

    /**
     * Gets the database name from the MongoTemplate.
     *
     * @param mongoTemplate the MongoTemplate to use
     * @return the database name
     */
    public static String getDatabaseName(MongoTemplate mongoTemplate) {
        return mongoTemplate.getDb().getName();
    }

    private MongoDbTestHelper() {
        // Utility class - prevent instantiation
    }
}
