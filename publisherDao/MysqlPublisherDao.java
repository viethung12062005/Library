package publisherDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.JDBCUtil;
import publisherModel.Publisher;

public class MysqlPublisherDao implements PublisherDao {

    // Thiết kế kiểu Singleton Pattern 
    // Chỉ tạo ra một instance duy nhất và dùng lại nó
    private static MysqlPublisherDao instance;

    public static MysqlPublisherDao getInstance() {
        if (instance == null) {
            instance = new MysqlPublisherDao();
        }
        return instance;
    }

    @Override
    public List<Publisher> getAll() {
        List<Publisher> result = new ArrayList<>();

        try {
            // Bước 1: Tạo kết nối đến cơ sở dữ liệu
            Connection con = JDBCUtil.getConnection();

            // Bước 2: Tạo đối tượng statement
            Statement st = con.createStatement();

            // Bước 3: Thực thi câu lệnh SQL
            ResultSet res = st.executeQuery("SELECT * FROM publisher");

            // Bước 4: Xử lý kết quả
            while (res.next()) {
                String id = res.getString("id");
                String publisherName = res.getString("publisherName");

                Publisher pub = new Publisher(id, publisherName);
                result.add(pub);
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
