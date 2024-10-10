/*TanDao
 */
package publisherDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import publisherModel.*;
import publisherModel.PublisherModel;

/**
 *
 * Class to implement PublisherDao interface using Google API
 */
public class GooglePublisherDao implements PublisherDao {

    private static GooglePublisherDao instance;
    private static final String GOOGLE_API_URL = "https://www.googleapis.com/books/v1/volumes?q=publisher:";

    private GooglePublisherDao() {}

    public static GooglePublisherDao getInstance() {
        if (instance == null) {
            instance = new GooglePublisherDao();
        }
        return instance;
    }

    @Override
    public List<PublisherModel> getAll() {
        List<PublisherModel> publishers = new ArrayList<>();
        try {
            // URL for Google API query, replace "sample_publisher" with desired publisher query
            URL url = new URL(GOOGLE_API_URL + "sample_publisher");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray items = jsonResponse.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                    
                    String id = item.getString("id");
                    String publisherName = volumeInfo.optString("publisher", "Unknown Publisher");
                    
                    publishers.add(new PublisherModel(id, publisherName));
                }
            } else {
                System.out.println("Error fetching data from Google API: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publishers;
    }

    public static void main(String[] args) {
        GooglePublisherDao gpd = GooglePublisherDao.getInstance();
        System.out.println(gpd.getAll());
    }
}
