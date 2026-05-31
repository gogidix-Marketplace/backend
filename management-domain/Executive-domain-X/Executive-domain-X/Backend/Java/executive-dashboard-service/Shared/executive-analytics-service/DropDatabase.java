mport com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DropDatabase {
    public static void main(String[] args) {
        try (var client = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = client.getDatabase("management_executive");
            db.drop();
            System.out.println("Database management_executive dropped successfully!");
        }
    }
}
