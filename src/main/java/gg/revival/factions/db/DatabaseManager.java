package gg.revival.factions.db;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

public class DatabaseManager {

    @Getter
    @Setter
    static MongoCollection<Document> factionsCollection;
    @Getter
    @Setter
    static MongoCollection<Document> playersCollection;
    @Getter
    @Setter
    static MongoCollection<Document> claimsCollection;
    @Getter
    @Setter
    static MongoCollection<Document> subclaimsCollection;

}
