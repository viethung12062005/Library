//TanDao
 
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
            
            URL url = new URL(GOOGLE_API_URL + "*"); // lay all
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); // Phương thức GET thường được sử dụng để lấy thông tin mà không thay đổi bất kỳ dữ liệu nào trên máy chủ.
            conn.connect(); //thực hiện quá trình thiết lập kết nối và gửi yêu cầu đến máy chủ

            int responseCode = conn.getResponseCode();/* Lấy mã phản hồi
            200 OK: Yêu cầu đã thành công và dữ liệu đã được trả về.
			404 Not Found: Tài nguyên mà bạn đang yêu cầu không tồn tại trên máy chủ.
			500 Internal Server Error: Có sự cố xảy ra trên máy chủ khi xử lý yêu cầu.            
            */
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); //đọc dữ liệu phản hồi từ máy chủ 
                StringBuilder response = new StringBuilder();// tạo ra một đối tượng để xây dựng chuỗi dữ liệu phản hồi từ máy chủ
                String inputLine; // lưu trữ từng dòng dữ liệu mà bạn đọc từ phản hồi của máy chủ qua BufferedReader
                while ((inputLine = in.readLine()) != null) { 
                    response.append(inputLine); //đọc dữ liệu từ một BufferedReader cho đến khi không còn dòng nào để đọc
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray items = jsonResponse.optJSONArray("items"); // Sử dụng optJSONArray để tránh NullPointerException

                if (items != null) {
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject volumeInfo = item.getJSONObject("volumeInfo");

            //Lấy giá trị của trường id từ đối tượng item
                        String id = item.getString("id");
                        
            //  lấy giá trị của trường publisher từ volumeInfo. Nếu không tìm thấy trường này, nó sẽ mặc định trả về "Unknown Publisher".
                        String publisherName = volumeInfo.optString("publisher", "Unknown Publisher");

                        publishers.add(new PublisherModel(id, publisherName));
                    }
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
