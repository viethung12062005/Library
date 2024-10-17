
package registeterm.dao;

import card.dao.MysqlBorrowCardDao;
import card.model.BorrowCard;
import registeterm.model.RegisterBorrow;
import registeterm.model.RegisterTermDetail;
import sqlite.Mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MysqlRegisterTerrmDao implements RegisterTermDao {

    public static final int RESULT_NO_SELECTED = 0;
    public static final int RESULT_NULL_POINT = 1;
    public static final int RESULT_MONEY_WRONG = 2;
    public static final int RESULT_END_DATE = 3;
    public static final int RESULT_OVER_DUE_CARD = 4;
    public static final int RESULT_CARD_NO_ACTIVE = 5;
    public static final int RESULT_OVER_DUE_BORROW = 6;
    public static final int RESULT_MAX_COUNT_BORROW = 7;
    public static final int RESULT_SUCCESS = 8;
    public static final int RESULT_SQLITE = 9;
    private static final String GET_ALL = "SELECT * FROM registebook";
    private static final String GET_ALL_LOAN_DETAIL_BY_ID_LOAN = "SELECT * FROM registedetail WHERE id = ?";
    private static final String GET_COUNT_BOOK_BORROW_BY_ID = "SELECT COUNT(loandetail.idcopy) FROM loandetail,loanbook WHERE "
            + "loanbook.id = loandetail.id and loanbook.cardno = ? and loanbook.enddate < CURRENT_DATE and loandetail.isreturn = false;";
    private static final String GET_COUNT_BOOK_BORROW_BY_ID_CARD = "SELECT COUNT(loandetail.idcopy) "
            + "FROM loandetail,loanbook"
            + " WHERE loanbook.id = loandetail.id and loanbook.cardno = ?  and loandetail.isreturn = false";
    private static final String GET_COUNT_BOOK_REGISTED_BY_ID_CARD = "SELECT COUNT(idcopy) FROM registebook,registedetail WHERE registebook.id = registedetail.id and registebook.idcard = ?";
    private static final String GET_CARD_BY_CARD_NO = "SELECT * FROM borrowcard WHERE cardno = ?";

    private static final String INSERT_LOAN_BOOK = "INSERT INTO loanbook(id,cardno,idlibrarian,name,startdate,enddate,money) "
            + "SELECT COUNT(*)+1,?,?,?,?,?,? FROM loanbook ";
    private static final String INSERT_LOAN_DETAIL = "INSERT INTO loandetail(id,idcopy,title,isreturn) "
            + "            SELECT COUNT(*),?,?,? FROM loanbook  ";

    private static final String GET_COUNT = "SELECT COUNT(*) FROM loanbook";
    private static final String UN_ALL_REGISTED_BOOKCOPY_DETAIL_BYID = "DELETE FROM registedetail WHERE id =?";
    private static final String UN_ALL_REGISTED_BOOKCOPY_BYID = "DELETE FROM registebook WHERE id =?";
    private static final String UPDATE_BOOK_COPY_BORROW = "UPDATE bookcopy SET status = 3 WHERE idcopy =?";
    private static final String SEARCH_REGISTER_BORROW_BY_KEY = "SELECT * FROM registebook WHERE  registebook.idcard like ? or registebook.id like ? or registebook.name like ?;";

    private MysqlRegisterTerrmDao() {

    }
    private static MysqlRegisterTerrmDao instance;

    public static MysqlRegisterTerrmDao getInstance() {
        if (instance == null) {
            instance = new MysqlRegisterTerrmDao();
        }
        return instance;
    }

    //Lấy tất cả danh sách đăng ký mượn sách
    @Override
    public List<RegisterBorrow> getAll() {
        List<RegisterBorrow> results = new ArrayList<>();
        try {
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(GET_ALL);
            while (rs.next()) {
                RegisterBorrow regBorrow = new RegisterBorrow();
                int id = rs.getInt("id");
                regBorrow.setId(id);
                regBorrow.setIdcard(rs.getString("idcard"));
                regBorrow.setName(rs.getString("name"));
                regBorrow.setRegDate(rs.getDate("regDate"));
                PreparedStatement st2 = c.prepareStatement(GET_ALL_LOAN_DETAIL_BY_ID_LOAN);
                st2.setInt(1, id);
                ResultSet rs2 = st2.executeQuery();
                List<RegisterTermDetail> detail = new ArrayList<>();
                while (rs2.next()) {
                    RegisterTermDetail regDetail = new RegisterTermDetail();
                    regDetail.setId(id);
                    regDetail.setIdCopy(rs2.getString("idcopy"));
                    regDetail.setTitle(rs2.getString("title"));
                    detail.add(regDetail);

                }
                regBorrow.setDetails(detail);
                results.add(regBorrow);

            }

            return results;
        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return null;
    }

    @Override
    public int borrowBook(RegisterBorrow reg, String idlib, Date startDate, Date endDate, double money) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st1 = c.prepareStatement(INSERT_LOAN_BOOK);
            st1.setString(1, reg.getIdcard());
            st1.setString(2, idlib);
            st1.setString(3, reg.getName());
            st1.setDate(4, startDate);
            st1.setDate(5, endDate);
            st1.setDouble(6, money);
            PreparedStatement pstm = c.prepareStatement(UN_ALL_REGISTED_BOOKCOPY_DETAIL_BYID);
            pstm.setInt(1, reg.getId());
            PreparedStatement pstm1 = c.prepareStatement(UN_ALL_REGISTED_BOOKCOPY_BYID);
            pstm1.setInt(1, reg.getId());
            if (st1.executeUpdate() > 0) {
                for (int i = 0; i < reg.getDetails().size(); i++) {
                    RegisterTermDetail detail = reg.getDetails().get(i);
                    PreparedStatement st2 = c.prepareStatement(INSERT_LOAN_DETAIL);
                    PreparedStatement st3 = c.prepareStatement(UPDATE_BOOK_COPY_BORROW);
                    st2.setString(1, detail.getIdCopy());
                    st2.setString(2, detail.getTitle());
                    st2.setBoolean(3, false);
                    st3.setString(1, detail.getIdCopy());
                    st2.executeUpdate();
                    st3.executeUpdate();
                }
                pstm.executeUpdate();
                pstm1.executeUpdate();
            }
            
            return MysqlRegisterTerrmDao.RESULT_SUCCESS;

        } catch (SQLException e) {
            System.out.println("Error SQLITE" + e.getMessage());
        }
        return MysqlRegisterTerrmDao.RESULT_SQLITE;

    }

    @Override
    public boolean checkBookBorrowOverDue(String cardNo) {
        int count = 0;
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(GET_COUNT_BOOK_BORROW_BY_ID);
            pstm.setString(1, cardNo);
            ResultSet rs = pstm.executeQuery();
            System.out.println("Query : " + pstm);
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Errrow" + ex.getErrorCode());
        }
        System.out.println("no");
        return false;
    }

    @Override
    public int getCountBookBorrow(String cardNo) {

        int count = 0;
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pt = c.prepareStatement(GET_COUNT_BOOK_BORROW_BY_ID_CARD);
            pt.setString(1, cardNo);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                System.out.println("borrow " + rs.getInt(1));
                count = count + rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("SqliteException" + e.getMessage());

        }
        return count;
    }

    @Override
    public BorrowCard getCardByIdCard(String cardNo) {
        try {
            BorrowCard result = new BorrowCard();
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pt = c.prepareStatement(GET_CARD_BY_CARD_NO);
            pt.setString(1, cardNo);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {

                result = new BorrowCard();
                result.setStatus(rs.getInt("status"));
                result.setExpiredDate(rs.getDate("expired"));
                return result;
            }

        } catch (SQLException e) {
            System.out.println("SqliteException" + e.getMessage());

        }
        return null;
    }

    @Override
    public List<RegisterBorrow> findRegByKey(String key) {
        List<RegisterBorrow> results = new ArrayList<>();
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(SEARCH_REGISTER_BORROW_BY_KEY);
            st.setString(1, "%" + key + "%");
            st.setString(2, "%" + key + "%");
            st.setString(3, "%" + key + "%");
            System.out.println(st);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RegisterBorrow regBorrow = new RegisterBorrow();
                int id = rs.getInt("id");
                regBorrow.setId(id);
                regBorrow.setIdcard(rs.getString("idcard"));
                regBorrow.setName(rs.getString("name"));
                regBorrow.setRegDate(rs.getDate("regDate"));

                PreparedStatement st2 = c.prepareStatement(GET_ALL_LOAN_DETAIL_BY_ID_LOAN);
                st2.setInt(1, id);
                ResultSet rs2 = st2.executeQuery();
                List<RegisterTermDetail> detail = new ArrayList<>();
                while (rs2.next()) {
                    RegisterTermDetail regDetail = new RegisterTermDetail();
                    regDetail.setId(id);
                    regDetail.setIdCopy(rs2.getString("idcopy"));
                    regDetail.setTitle(rs2.getString("title"));
                    detail.add(regDetail);

                }
                regBorrow.setDetails(detail);
                results.add(regBorrow);

            }

            return results;
        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return null;
    }

    @Override
    public List<RegisterTermDetail> getAllRegisDetailById(int id) {
        List<RegisterTermDetail> results = new ArrayList<>();
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_ALL_LOAN_DETAIL_BY_ID_LOAN);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                RegisterTermDetail detail = new RegisterTermDetail();
                detail.setId(id);
                detail.setIdCopy(rs.getString("idcopy"));
                detail.setTitle(rs.getString("title"));
                results.add(detail);

            }

            return results;
        } catch (SQLException e) {
            System.out.println("" + e);
        }
        return null;
    }

    @Override
    public int getCountLoan() {
        try {
            Connection connection = Mysql.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(GET_COUNT);
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlRegisterTerrmDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }

    public static void main(String[] args) {
        MysqlRegisterTerrmDao mrtd = new MysqlRegisterTerrmDao();
        System.out.println(mrtd.getCountLoan());
    }
}
