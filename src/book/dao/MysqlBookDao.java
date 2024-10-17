/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book.dao;

import book.model.Book;
import bookcopy.dao.MysqlBookCopyDao;
import bookcopy.model.BookCopy;
import category.model.Category;
import publisher.model.Publisher;
import sqlite.Mysql;
import util.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MysqlBookDao implements BookDao {

    private static MysqlBookDao mysqlBookDao;
    private static final String COLUMN_ID_BOOK = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_PUBLISHER = "idpub";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_ID_CATEGORY = "id";
    private static final String COLUMN_ID_PUBLISHER = "id";

    private static final String COLUMN_ID_COPY = "idcopy";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_PRICE = "price";

    private static final String SEARCH_BOOK = "SELECT * FROM book, category, publisher "
            + "where category.id = book.idcat and book.idpub = publisher.id and"
            + " book.id like ? and title like ? and author like ? and"
            + " publisher.pub like ? and category.category like ?";

    private static final String GET_ALL_BOOK = "SELECT * FROM book,category,publisher "
            + "where idcat = category.id and idpub = publisher.id";
    private static final String GET_ALL_BOOKCOPY_BORROWABLE_BY_ID = "SELECT * FROM bookcopy WHERE bookcopy.idbook =? AND type ='Có thể mượn' AND status =0;";
    private static final String GET_ALL_BOOKCOPY_MANAGER_BY_ID = "SELECT * FROM bookcopy WHERE bookcopy.idbook =?;";
    private static final String UPDATE_INFOR_BOOK ="UPDATE book set title =? , author = ?, idpub =? where id =?";
    private static final String INSERT_BOOK = "INSERT INTO book(id,title,author,idpub,idcat) values(?,?,?,?,?)";
    private static final String GET_IDCAT_BYCATEGORY = "SELECT * FROM category WHERE category =?";
    private static final String GET_IDPUB_BYPUBLISHER = "SELECT * FROM publisher WHERE pub =?";
    private static final String GET_ID_BOOK_MAX_BY_IDCAT = "SELECT MAX(id) FROM book WHERE idcat =?";
    private static final String CHECK_BOOK = "SELECT COUNT(*) FROM book WHERE title =? AND idpub= ? AND idcat =? AND author =?";
    public static final int RESULT_NULL_POINT = 0;
    public static final int RESULT_SUCCESS = 1;
    public static final int BOOK_ADD_SAME = 2;
    public static final int RESULT_SQLITE = 3;

    public static MysqlBookDao getInstance() {
        if (mysqlBookDao == null) {
            mysqlBookDao = new MysqlBookDao();
        }
        return mysqlBookDao;
    }
    
    /**
     * 
     * @param idSearch
     * @param titleSearch
     * @param authorSearch
     * @param publishSearch
     * @param categorySearch
     * @return 
     */
    @Override
    public List<Book> searchBook_Registe(String idSearch, String titleSearch, String authorSearch, String publishSearch, String categorySearch) {
        List<Book> results = new ArrayList<>();;
        if (publishSearch.equalsIgnoreCase("All")) {
            publishSearch = "";
        }
        if (categorySearch.equalsIgnoreCase("All")) {
            categorySearch = "";
        }
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(SEARCH_BOOK);
            pstmt.setString(1, "%" + idSearch + "%");
            pstmt.setString(2, "%" + titleSearch + "%");
            pstmt.setString(3, "%" + authorSearch + "%");
            pstmt.setString(4, "%" + publishSearch + "%");
            pstmt.setString(5, "%" + categorySearch + "%");
            System.out.println("Query : " + pstmt);
            PreparedStatement st2 = c.prepareStatement(GET_ALL_BOOKCOPY_BORROWABLE_BY_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("book.id");
                Book.BookBuilder bookBuilder = createBookBuilder(rs);
                List<BookCopy> listCopy = new ArrayList<>();
                st2.setString(1, id);
                System.out.println(st2);
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()) {
                    System.out.println("Hello" + id);
                    BookCopy copy = new BookCopy.BookCopyBuilder()
                            .setId(id)
                            .setCopyNumber(rs2.getString(COLUMN_ID_COPY))
                            .setStatus(rs2.getInt(COLUMN_STATUS))
                            .setType(rs2.getString(COLUMN_TYPE))
                            .setPrice(rs2.getDouble(COLUMN_PRICE))
                            .build();
                    listCopy.add(copy);

                }
                bookBuilder.setListCopies(listCopy);
                results.add(bookBuilder.build());
            }
            return results;

        } catch (SQLException e) {
            System.out.println("" + e.getMessage());
        }
        return results;
    }

    @Override
    public List<Book> searchBook_Manager(String idSearch, String titleSearch, String authorSearch, String publishSearch, String categorySearch) {
        List<Book> results = new ArrayList<>();;
        if (publishSearch.equalsIgnoreCase("All")) {
            publishSearch = "";
        }
        if (categorySearch.equalsIgnoreCase("All")) {
            categorySearch = "";
        }
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(SEARCH_BOOK);
            pstmt.setString(1, "%" + idSearch + "%");
            pstmt.setString(2, "%" + titleSearch + "%");
            pstmt.setString(3, "%" + authorSearch + "%");
            pstmt.setString(4, "%" + publishSearch + "%");
            pstmt.setString(5, "%" + categorySearch + "%");
            System.out.println("Query : " + pstmt);
            PreparedStatement st2 = c.prepareStatement(GET_ALL_BOOKCOPY_MANAGER_BY_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("book.id");
                Book.BookBuilder bookBuilder = createBookBuilder(rs);
                List<BookCopy> listCopy = new ArrayList<>();
                st2.setString(1, id);
                System.out.println(st2);
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()) {
                    System.out.println("Hello" + id);
                    BookCopy copy = new BookCopy.BookCopyBuilder()
                            .setId(id)
                            .setCopyNumber(rs2.getString(COLUMN_ID_COPY))
                            .setStatus(rs2.getInt(COLUMN_STATUS))
                            .setType(rs2.getString(COLUMN_TYPE))
                            .setPrice(rs2.getDouble(COLUMN_PRICE))
                            .build();
                    listCopy.add(copy);

                }
                bookBuilder.setListCopies(listCopy);
                results.add(bookBuilder.build());
            }
            return results;

        } catch (SQLException e) {
            System.out.println("" + e.getMessage());
        }
        return results;
    }

    private Book.BookBuilder createBookBuilder(ResultSet rs) throws SQLException {
        Category category = new Category(rs.getString("category.id"), rs.getString("category.category"));
        Publisher publisher = new Publisher(rs.getString("publisher.id"), rs.getString("publisher.pub"));
        return new Book.BookBuilder()
                .setId(rs.getString("book.id"))
                .setTitle(rs.getString(COLUMN_TITLE))
                .setAuthor(rs.getString(COLUMN_AUTHOR))
                .setPublisher(publisher)
                .setCategory(category);

    }

    @Override
    public int updateInforBook(String id, String title, String author, String publisher, String category){
        try {
            Connection c = Mysql.getInstance().getConnection();
            String idPub = getIdPubByPublisher(publisher);
            PreparedStatement pstm = c.prepareStatement(UPDATE_INFOR_BOOK);
            pstm.setString(1, title);
            pstm.setString(2, author);
            pstm.setString(3, idPub);
            pstm.setString(4, id);
            int check = pstm.executeUpdate();
            if (check > 0) {
                return RESULT_SUCCESS;  //2 nghĩa là thêm thành công
            }
        } catch (SQLException e) {
            Logger.getLogger(MysqlBookCopyDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return RESULT_SQLITE;   //3 thêm thất bại ,
    }
    
    @Override
    public List<Book> getAllBook() {
        List<Book> results = new ArrayList<>();;
        try {
            System.out.println("getAll" + GET_ALL_BOOK);
            System.out.println("getById" + GET_ALL_BOOKCOPY_BORROWABLE_BY_ID);
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();
            PreparedStatement st2 = c.prepareStatement(GET_ALL_BOOKCOPY_BORROWABLE_BY_ID);

            ResultSet rs = st.executeQuery(GET_ALL_BOOK);
            while (rs.next()) {
                String id = rs.getString(COLUMN_ID_BOOK);
                Book.BookBuilder bookBuilder = createBookBuilder(rs);
                List<BookCopy> listCopy = new ArrayList<>();
                st2.setString(1, id);
                ResultSet rs2 = st2.executeQuery();
                while (rs2.next()) {
                    BookCopy copy = new BookCopy.BookCopyBuilder()
                            .setId(id)
                            .setCopyNumber(rs2.getString(COLUMN_ID_COPY))
                            .setStatus(rs2.getInt(COLUMN_STATUS))
                            .setType(rs2.getString(COLUMN_TYPE))
                            .setPrice(rs2.getDouble(COLUMN_PRICE))
                            .build();
                    listCopy.add(copy);

                }
                bookBuilder.setListCopies(listCopy);
                results.add(bookBuilder.build());
            }
            return results;

        } catch (SQLException e) {
            System.out.println("" + e.getMessage());
        }
        return results;
    }

    @Override
    public int addBook(String id, String title, String author, String idPub, String idCat) {
        
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(INSERT_BOOK);
            pstm.setString(1, id);
            pstm.setString(2, title);
            pstm.setString(3, author);
            pstm.setString(4, idPub);
            pstm.setString(5, idCat);
            int check = pstm.executeUpdate();
            if (check > 0) {
                return RESULT_SUCCESS;  //2 nghĩa là thêm thành công
            }
        } catch (SQLException e) {
            Logger.getLogger(MysqlBookCopyDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return RESULT_SQLITE;   //3 thêm thất bại ,
    }

    @Override
    public boolean checkBookSame(String id, String title, String author, String idPub, String idCat) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(CHECK_BOOK);
            pstm.setString(1, title);
            pstm.setString(2, idPub);
            pstm.setString(3, idCat);
            pstm.setString(4, author);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                if(rs.getInt(1)>1){
                    return true;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MysqlBookCopyDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public int getMaxCountIdByBookId(String id) {
        int max = 0;
        try {
            Connection c = Mysql.getInstance().getConnection();
            String GET_ID_BOOK_BY_ID = null;
            PreparedStatement st = c.prepareStatement(GET_ID_BOOK_BY_ID);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int COLUMN_ID = 0;
                String idCopy = rs.getString(COLUMN_ID);
                System.out.println("String Matcher id" + id);
                String PREFIX_ID = null;
                Pattern word = Pattern.compile(PREFIX_ID);
                Matcher match = word.matcher(id);
                while (match.find()) {
                    String numOfId = id.substring(match.end(), id.length());
                    System.out.println("String Matcher" + numOfId);
                    if (Utils.isStringInteger(numOfId)) {
                        int number = Integer.valueOf(numOfId);
                        if (max < number) {
                            max = number;
                        }
                    }
                }
            }
            c.close();

        } catch (SQLException e) {
        }
        return max;
    }

    @Override
    public String getCatCodeById(String id) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            String GET_CAT_BY_ID = null;
            PreparedStatement st = c.prepareStatement(GET_CAT_BY_ID);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(COLUMN_CATEGORY);
            }
            c.close();

        } catch (SQLException e) {
        }
        return null;
    }

    @Override
    public boolean checkId(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generateIdBook(String idCat) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_ID_BOOK_MAX_BY_IDCAT);
            st.setString(1, idCat);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String idBookMax = rs.getString(1);
                if (idBookMax == null) {
                    return idCat + 1;
                } else {
                    int length = idCat.length();
                    int idMaxNext = Integer.valueOf(idBookMax.substring(length)) + 1;
////                System.out.println(idCat+idMaxNext);
//                    System.out.println(idBookMax);
                    return idCat + idMaxNext;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MysqlBookDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return idCat + 1;
    }

    public static void main(String[] args) {
        MysqlBookDao mbd = new MysqlBookDao();
        System.out.println(mbd.generateIdBook("ET"));
        System.out.println(mbd.getAllBook().get(0).getId());
    }

    @Override
    public List<Book> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTypeBook(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getStatusBookCopy(String idCopy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean createRegisteBook(int id, Date dateNow) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean registeBook(int id, String author, String title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateStatusBook(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Book> getAllBookById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int generateIdRegiste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int generateIdRegisteDetail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCountBookById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIdCatByCategory(String category) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_IDCAT_BYCATEGORY);
            st.setString(1, category);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(COLUMN_ID_CATEGORY);
            }
            c.close();

        } catch (SQLException e) {
        }
        return null;
    }

    @Override
    public String getIdPubByPublisher(String publisher) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_IDPUB_BYPUBLISHER);
            st.setString(1, publisher);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString(COLUMN_ID_PUBLISHER);
            }
            c.close();

        } catch (SQLException e) {
        }
        return null;
    }
}
