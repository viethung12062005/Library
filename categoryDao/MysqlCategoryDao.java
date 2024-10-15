package categoryDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import categoryModel.Category;
import database.JDBCUtil;




public class MysqlCategoryDao implements CategoryDao{
	
	
	 // Thiết kế kiểu Singleton Pattern 
    // Chỉ tạo ra một instance duy nhất và dùng lại nó
    private static MysqlCategoryDao instance;

    public static MysqlCategoryDao getInstance() {
        if (instance == null) {
            instance = new MysqlCategoryDao();
        }
        return instance;
    }


	@Override
	public List<Category> getAll() {
		 List<Category> result = new ArrayList<>();

	        try {
	            // Bước 1: Tạo kết nối đến cơ sở dữ liệu
	            Connection con = JDBCUtil.getConnection();

	            // Bước 2: Tạo đối tượng statement
	            Statement st = con.createStatement();

	            // Bước 3: Thực thi câu lệnh SQL
	            ResultSet res = st.executeQuery("SELECT * FROM category");

	            // Bước 4: Xử lý kết quả
	            while (res.next()) {
	                String id = res.getString("id");
	                String categoryName = res.getString("categoryName");

	              Category c=new Category(id,categoryName);
	                result.add(c);
	            }

	            // Bước 5: Đóng kết nối
	            JDBCUtil.closeConnection(con);

	        } catch (SQLException e) {
	            System.err.println("Lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage());
	            e.printStackTrace();
	        }
	        return result;
	}


}
