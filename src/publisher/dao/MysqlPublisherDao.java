package publisher.dao;
import publisher.model.Publisher;
import sqlite.Mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MysqlPublisherDao implements PublisherDao {

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_PUBLISHER = "pub";
    private static final String GET_ALL = "SELECT* FROM publisher";

    private MysqlPublisherDao() {
    }
    private static MysqlPublisherDao instance;

    public static MysqlPublisherDao getIntance() {
        if (instance == null) {
            instance = new MysqlPublisherDao();
        }
        return instance;
    }

    @Override
    public List<Publisher> getAll() {
        List<Publisher> results = new ArrayList<>();
        try {
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(GET_ALL);
            while (rs.next()) {
                results.add(createCategory(rs));
            }
            c.close();
            return results;
        } catch (SQLException e) {

        }
        return null;
    }

    private Publisher createCategory(ResultSet rs) throws SQLException {
        return new Publisher(rs.getString(COLUMN_ID), rs.getString(COLUMN_PUBLISHER));

    }
    
    public static void main(String[] args) {
        MysqlPublisherDao mpd = new MysqlPublisherDao();
        System.out.println(mpd.getAll());
    }

}
