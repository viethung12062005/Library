package categoryDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import categoryModel.CategoryModel;

public class GoogleCategoryDao implements CategoryDao {
	
	
	 private static final String GOOGLE_API_URL = "https://www.googleapis.com/books/v1/categories";

	@Override
	public List<CategoryModel> getAll() {
		List<CategoryModel> categories=new ArrayList<>();
		try {
			URL url=new URL(GOOGLE_API_URL);
			HttpsURLConnection conn=  (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			
			int responseCode = conn.getResponseCode();
			
			if(responseCode=200) {
/*đọc và lưu trữ dữ liệu vào bộ nhớ đệm*/
				BufferedReader in =new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder response =new StringBuilder(); // stringbuilder giup nối thêm chuỗi mà không cần tạo đối tượng mới
																//-> tietkiem bo nho
				
				String inputLine;
				while((inputLine=in.readLine())!=null) {
					response.append(inputLine);
				}
				in.close();
				
				JSONObject jsonResponse = new JSONObject(response.toString());
				JSONArray items = jsonResponse.optJSONArray("items");
				
				if(items!=null) {
					for(int i=0;i<items.length();i++) {
						JSONObject item= items.getJSONObject(i);
						String id= item.getString("id");
						String categoryName = item.optString("category", "Unknown Category");
						
						categories.add(new CategoryModel(id,categoryName));
					}
				}
			}else {
				 System.out.println("Error fetching data from Google API: " + responseCode);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return categories;
	}

	

}
