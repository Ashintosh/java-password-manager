import com.google.gson.Gson;
import database.Database;
import database.Entry;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        /*Gson gson = new Gson();
        Database database = new Database();
        List<String> groupNames = database.getGroupNames();
        List<List<Entry>> groups = database.getGroups();

        for (int i = 0; i < groups.size(); i++) {
            System.out.println(groupNames.get(i));
            System.out.println("----------");
            for (int j = 0; j < groups.get(i).size(); j++) {
                System.out.println(gson.toJson(database.getEntry(i, j)));
            }
            System.out.println("----------");
        }

        String jsonObject = gson.toJson(database);
        System.out.println(jsonObject);

        Database newDatabase = gson.fromJson(jsonObject, Database.class);
        List<List<Entry>> newGroups = newDatabase.getGroups();
        System.out.println(gson.toJson(newGroups));*/

        new MainForm();
    }
}
