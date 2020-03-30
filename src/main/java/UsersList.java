import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UsersList {

    public Map<String, Integer> allUsers = new HashMap<>();
    private String propertiesPath = "src\\main\\resources\\user.properties";
    private Properties properties = new Properties();


    public UsersList() {
        getProperties();
    }

    public void getProperties() {
        try {
            File file = new File(propertiesPath);
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
            for (Object keyObject : properties.keySet()) {
                String key = (String) keyObject;
                String value = properties.getProperty(key);
                allUsers.put(key, Integer.valueOf(value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String userName, int id) {
        if (!allUsers.containsKey(userName)) {
            allUsers.put(userName, id);
            try {
                properties.setProperty(userName, String.valueOf(id));
                FileOutputStream fileOut = new FileOutputStream(new File(propertiesPath));
                properties.store(fileOut, "Users");
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
