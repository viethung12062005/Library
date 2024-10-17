
package card.dao;

import card.controller.CardController;
import card.model.BorrowCard;
import sqlite.Mysql;
import user.model.User;
import user.dao.*;
import util.Constants;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MysqlBorrowCardDao implements CardDao {

    private static MysqlBorrowCardDao instance;

    private MysqlBorrowCardDao() {
    }

    //columns of table BorrowCard.
    private static final String TABLE_NAME = "borrowcard";
    private static final String TABLE_NAME_USER = "user";
    private static final String COLUMN_ID_USER = "id";
    private static final String COLUMN_NAME_USER = "name";
    private static final String COLUMN_MAIL_USER = "email";
    private static final String COLUMN_PHONE_USER = "phone";

    private static final String COLUMN_ID_CARD = "id";
    private static final String COLUMN_CARD_NO = "cardno";
    private static final String COLUMN_STATUS_CARD = "status";
    private static final String COLUMN_EXPIRED_DATE = "expired";
    private static final String COLUMN_REG_DATE = "regdate";
    private static final String COLUMN_ACT_DATE = "actdate";

    public static final String PREFIX_ID_CARD = "BCLIBRARY";

    public static final int STATUS_DEACTIVED = 0;
    public static final int STATUS_ACTIVED = 1;
    public static final int ACTIVATE_FAIL_EXPERIED = 0;
    public static final int ACTIVATE_FAIL_NULL_POINT = 1;
    public static final int ACTIVATE_SUCCESS = 2;
    public static final int ACTIVATE_FAIL_OTHER = 3;

    private static final String INSERT_CARD = "INSERT INTO borrowcard (id,cardno,status,regdate) VALUES (?,?,?,?)";
    private static final String GET_CARD_REGISTED = "SELECT *FROM " + TABLE_NAME + "," + TABLE_NAME_USER
            + " WHERE " + COLUMN_STATUS_CARD + " =?"
            + " AND " + TABLE_NAME_USER + "." + COLUMN_ID_USER + " = " + TABLE_NAME + "." + COLUMN_ID_CARD;

    private static final String GET_COUNT_CARD_ACTIVED = "SELECT count(*) FROM " + TABLE_NAME
            + " WHERE " + COLUMN_CARD_NO + " <> 'NULL'";
    private static final String ACTIVATE_CARD = " UPDATE " + TABLE_NAME + " SET " + COLUMN_CARD_NO + " = ?"
            + "," + COLUMN_STATUS_CARD + " = " + STATUS_ACTIVED + " ," + COLUMN_ACT_DATE + " = ?" + "," + COLUMN_EXPIRED_DATE + " =?"
            + " WHERE " + COLUMN_ID_CARD + " = ?";

    private static final String GET_CARDNO_BY_ID = "SELECT cardNo FROM borrowcard WHERE id = ?";
    private static final String GET_CARD_BY_ID = "SELECT * FROM borrowcard WHERE id = ?";
    private static final String GET_NAME_USER_BY_ID = "SELECT name FROM user WHERE id = ?";
    private static final String GET_STATUS_USER_BY_ID = "SELECT status FROM borrowcard WHERE id = ?";
    private static final String GET_STATUS_CARD_BY_CARD_NO = "SELECT status FROM borrowcard WHERE cardno = ?";
    private static final String CHECK_USER_OVER_DUE_BOOK_COPY = "SELECT COUNT(*) FROM loanbook,loandetail WHERE loanbook.id = loandetail.id AND loanbook.cardno = ? AND loandetail.isreturn = false AND loanbook.enddate < CURRENT_DATE";
    private static final String GET_COUNT_BOOK_BORROW_BY_ID_CARD = "SELECT COUNT(loandetail.idcopy) "
            + "FROM loandetail,loanbook"
            + " WHERE loanbook.id = loandetail.id and loanbook.cardno = ?  and loandetail.isreturn = false";
    private static final String GET_COUNT_BOOK_REGISTED_BY_ID_CARD = "SELECT COUNT(idcopy) FROM registebook,registedetail WHERE registebook.id = registedetail.id and registebook.idcard = ?";
    private static final String GET_ALL_ID_CARD = "SELECT cardno FROM borrowcard";
    private static final String GET_USER_BY_ID_CARD = "SELECT * FROM borrowcard,user WHERE user.id = borrowcard.id and borrowcard.cardno=?";
    private static final String GET_USER_HAVE_CARD_UN_ACTIVE_BY_KEY = "SELECT * FROM borrowcard,user WHERE user.id = borrowcard.id and borrowcard.status = ? and ( cardno like ? or name like ? or borrowcard.id like ? )";
    private static final String DEACTIVE_CARD = "UPDATE borrowcard SET status = 0 WHERE cardno = ?";
    private static final String UPDATE_CARD = "UPDATE borrowcard SET expired = ? WHERE cardno = ?";
    private static final String CHECK_CARD_OVER_DUE = "SELECT expired FROM borrowcard WHERE cardno = ? ";

    public static MysqlBorrowCardDao getInstance() {
        if (instance == null) {
            instance = new MysqlBorrowCardDao();
        }
        return instance;
    }

    /**
     * Dùng để thêm thẻ mượn khi người dùng gửi form đăng ký
     *
     * @param card đối tượng thẻ cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại
     * @exception SQLException lỗi khi câu lệnh truy vấn sai hoặc không kết nối
     * được với CSDL
     */
    @Override
    public boolean insert(BorrowCard card) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(INSERT_CARD);
            pstmt.setString(1, card.getId());
            pstmt.setString(2, card.getCardNo());
            pstmt.setBoolean(3, false);
            Date regDate = new Date(System.currentTimeMillis());
            pstmt.setDate(4, regDate);
            if (pstmt.executeUpdate() > 0) {
                pstmt.close();
                c.close();
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Lấy thông tin thẻ đăng ký hiển thị lên bảng của Form kích hoạt thẻ
     *
     * @param model kiểu dữ liệu của bảng ở form kích hoạt thẻ
     * @exception SQLException lỗi khi câu lệnh truy vấn sai hoặc không kết nối
     * được với CSDL.
     */
    @Override
    public int getInforCardRegisted(DefaultTableModel model) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_CARD_REGISTED);
            System.out.println("query " + GET_CARD_REGISTED);
            st.setInt(1, 0);
            ResultSet rs = st.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                String cardNo = rs.getString(COLUMN_CARD_NO);
                if (cardNo.equals(Constants.DEFAULT_CARD_NO_REGISTER)) {
                    cardNo = Constants.DEFAULT_CARD_NO_REGISTER_IN_TABLE;
                }
                String idUser = rs.getString(TABLE_NAME + "." + COLUMN_ID_USER);
                String name = rs.getString(TABLE_NAME_USER + "." + COLUMN_NAME_USER);
                String mail = rs.getString(TABLE_NAME_USER + "." + COLUMN_MAIL_USER);
                String phone = rs.getString(TABLE_NAME_USER + "." + COLUMN_PHONE_USER);
                Date regDate = rs.getDate(COLUMN_REG_DATE);
                model.insertRow(model.getRowCount(), new Object[]{idUser, name, mail, phone, regDate, cardNo});
            }

        } catch (SQLException ex) {
            System.out.println("ex" + ex.getMessage());
            return CardController.RESULT_SQLITE;
        }
        if (model.getRowCount() > 0) {
            return CardController.RESULT_FOUND;
        } else {
            return CardController.RESULT_NOT_FOUND;
        }
    }

    /**
     * Truy cập bảng đăng ký , tính xem có bao nhiêu thẻ đã kích hoạt và sinh id
     * thẻ với phần trước BCLIBRARY + 1 số phía sau
     *
     * @return mã thẻ thư viện dùng để kích hoạt thẻ.
     */
    @Override
    public String generateIdCard() {
        try {
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(GET_COUNT_CARD_ACTIVED);
            int count;
            if (rs.next()) {
                count = rs.getInt(1);
                count = count + 1;
                return PREFIX_ID_CARD + count;
            }

        } catch (SQLException ex) {

        }
        return null;
    }

    /**
     * Dùng để kích hoạt thẻ thành viên
     *
     * @param card đối tượng chứa thông tin kích hoạt
     * @return ACTIVATE_FAIL_NULL_POINT nếu các thông tin chưa điền đầy đủ
     * ACTIVATE_FAIL_EXPERIED nếu thông tin đầy đủ nhưng hạn sử dụng thẻ không
     * hợp lệ ACTIVATE_SUCCESS nếu kích hoạt thẻ thành công ACTIVATE_FAIL_OTHER
     * nếu gặp lỗi truy vấn csdl
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int activateCard(BorrowCard card) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(ACTIVATE_CARD);
            st.setString(1, card.getCardNo());
            st.setDate(2, card.getActDate());
            st.setDate(3, card.getExpiredDate());
            st.setString(4, card.getId());
            System.out.println("query " + ACTIVATE_CARD);
            if (st.executeUpdate() > 0) {
                return ACTIVATE_SUCCESS;
            }

        } catch (SQLException ex) {
            System.out.println("Sqlite " + ex.getMessage());
        }
        return ACTIVATE_FAIL_OTHER;
    }

    /**
     * Dùng để lấy ra mã thẻ mượn thông qua mã độc giả
     *
     * @param id mã độc giả
     * @return mã thẻ mượn tương ứng với mã độc giả ,nếu thẻ chưa kích hoạt sẽ
     * trả về "NULL"
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public String getCardNoById(String id) {
        String cardNo = Constants.DEFAULT_CARD_NO_REGISTER;
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(GET_CARDNO_BY_ID);
            pstm.setString(1, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                cardNo = rs.getString(COLUMN_CARD_NO);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cardNo;
    }

    /**
     * Dùng để lấy ra tên người dùng từ mã độc giả
     *
     * @param id mã độc giả
     * @return tên người dùng ứng với mã độc giả đó, return empty nếu không tìm
     * thấy hoặc lỗi Sqlite
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public String getNameCardById(String id) {
        String name = "";
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(GET_NAME_USER_BY_ID);
            pstm.setString(1, id);
            ResultSet rs = pstm.executeQuery();
            rs.next();
            name = rs.getString(COLUMN_NAME_USER);
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }

    /**
     * Dùng để lấy ra trạng thái thẻ bởi mã thẻ mượn
     *
     * @param idCard mã thẻ mượn
     * @return 0 nếu thẻ chưa được kích hoạt và 1 nếu thẻ đã được kích hoạt
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int getStatusCardById(String idCard) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(GET_STATUS_USER_BY_ID);
            pstm.setString(1, idCard);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(COLUMN_STATUS_CARD);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Kiểm tra xem người dùng có bao nhiêu quyển sách quá hạn chưa trả
     *
     * @param idCard mã thẻ mượn
     * @return số lượng sách quá hạn chưa trả
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int checkUserOverDueBookCopy(String idCard) {
        int count = 0;
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(CHECK_USER_OVER_DUE_BOOK_COPY);
            pstm.setString(1, idCard);
            ResultSet rs = pstm.executeQuery();
            System.out.println("QueryErrow : " + pstm);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error " + ex.getMessage());
        }
        System.out.println(count);
        return count;
    }

    /**
     * Dùng để lấy ra thông tin thẻ mượn bởi mã độc giả
     *
     * @param id mã độc giả
     * @return đối tượng BorrowCard chứa thông tin thẻ mượn của độc giả hoặc
     * null nếu không tìm thấy hoặc lỗi truy vấn.
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public BorrowCard getBorrowCardByUsername(String id) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_CARD_BY_ID);
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String cardNo = rs.getString(COLUMN_CARD_NO);
                int status = rs.getInt(COLUMN_STATUS_CARD);
                Date expired = rs.getDate(COLUMN_EXPIRED_DATE);

                Date date = null;
                if (expired != null) {
                    date = new Date(expired.getTime());
                }
                BorrowCard bc = new BorrowCard(id, cardNo, status, date);
                return bc;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Trả về danh sách mã thẻ mượn đã kích hoạt hoặc từng được kích hoạt
     *
     * @return List<String> chứa danh sách cần tìm , null nếu lỗi truy vấn.
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public List<String> getAllIdCard() {
        List<String> results = new ArrayList<>();

        try {
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();

            ResultSet rs = st.executeQuery(GET_ALL_ID_CARD);
            while (rs.next()) {
                String cardNo = rs.getString("cardno");
                if (!cardNo.equals(Constants.DEFAULT_CARD_NO_REGISTER)) {
                    results.add(rs.getString("cardno"));
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return results;
    }

    /**
     * Lấy thông tin người dùng từ mã thẻ mượn
     *
     * @param cardNo mã thẻ mượn
     * @return Đối tượng User chứa thông tin người dùng , null nếu lỗi truy
     * vấn,lỗi kết nối csdl hoặc không tìm thấy
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public User getUserByCardNo(String cardNo) {

        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_USER_BY_ID_CARD);
            st.setString(1, cardNo);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User user = new User.Builder()
                        .setId(rs.getString("user.id"))
                        .setEmail(rs.getString("user.email"))
                        .setPhone(rs.getString("user.phone"))
                        .setName(rs.getString("user.name")).build();
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Lấy ra số lượng sách đã mượn và đã đăng ký của độc giả bằng mã thẻ mượn
     *
     * @param cardNo mã thẻ mượn
     * @return tổng số lượng sách đã mượn và đã đăng ký của độc giả, 0 nếu lỗi
     * truy vấn, lỗi kết nối csdl hoặc người dùng chưa từng mượn sách,chưa đăng
     * ký hoặc đã trả hết sách
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int getCountBookBorrowAndRegisted(String cardNo) {
        int count = 0;
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pt = c.prepareStatement(GET_COUNT_BOOK_BORROW_BY_ID_CARD);
            pt.setString(1, cardNo);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                System.out.println("Borrow" + rs.getInt(1));
                count = count + rs.getInt(1);
            }
            PreparedStatement pt2 = c.prepareStatement(GET_COUNT_BOOK_REGISTED_BY_ID_CARD);
            pt2.setString(1, cardNo);
            ResultSet rs2 = pt2.executeQuery();
            if (rs2.next()) {
                System.out.println("Regis" + rs2.getInt(1));
                count = count + rs2.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("SqliteException" + e.getMessage());

        }
        return count;
    }

    /**
     * Dùng để lấy ra trạng thái thẻ bởi mã thẻ mượn
     *
     * @param cardNo mã thẻ mượn
     * @return 0 nếu thẻ chưa được kích hoạt và 1 nếu thẻ đã được kích hoạt
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int getStatusCardByCardNo(String cardNo) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(GET_STATUS_CARD_BY_CARD_NO);
            pstm.setString(1, cardNo);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(COLUMN_STATUS_CARD);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlBorrowCardDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Tìm kiếm thông tin thẻ chưa kích hoạt từ mã độc giả hoặc mã thẻ mượn hoặc
     * tên độc giả
     *
     * @param key từ khóa dùng để tìm kiếm
     * @param model model của table để hiện thị kết quả tìm kiếm
     * @return 3 nếu lỗi kết nối csdl , lỗi truy vấn 1 nếu tìm thấy 1 vài kết
     * quả 2 nếu không tìm thấy kết quả nào
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int searchCardUnActive(String key, DefaultTableModel model) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_USER_HAVE_CARD_UN_ACTIVE_BY_KEY);
            st.setInt(1, Constants.STATUS_UNACTIVED);
            st.setString(2, "%" + key + "%");
            st.setString(3, "%" + key + "%");
            st.setString(4, "%" + key + "%");
            ResultSet rs = st.executeQuery();
            model.setRowCount(0);
            System.out.println("ex" + st);
            while (rs.next()) {
                String idUser = rs.getString(TABLE_NAME + "." + COLUMN_ID_USER);
                String name = rs.getString(TABLE_NAME_USER + "." + COLUMN_NAME_USER);
                String mail = rs.getString(TABLE_NAME_USER + "." + COLUMN_MAIL_USER);
                String phone = rs.getString(TABLE_NAME_USER + "." + COLUMN_PHONE_USER);
                Date regDate = rs.getDate(COLUMN_REG_DATE);
                model.insertRow(model.getRowCount(), new Object[]{idUser, name, mail, phone, regDate});
            }

        } catch (SQLException ex) {
            System.out.println("ex" + ex.getMessage());
            return CardController.RESULT_SQLITE;
        }
        if (model.getRowCount() > 0) {
            return CardController.RESULT_FOUND;
        } else {
            return CardController.RESULT_NOT_FOUND;
        }
    }

    /**
     * Lấy ra tất cả thẻ mượn đã kích hoạt , lấy thông tin độc giả tương ứng với
     * thẻ tìm được và hiển thị lên bảng
     *
     * @param model model của bảng hiển thị thông tin
     * @return 1 nếu tìm thấy 1 vài kết quả 2 nếu không tìm thấy kết quả nào 3
     * nếu lỗi kết nối csdl , lỗi truy vấn
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int getAllCardActive(DefaultTableModel model) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_CARD_REGISTED);
            st.setInt(1, Constants.STATUS_ACTIVED);
            ResultSet rs = st.executeQuery();
            model.setRowCount(0);
            System.out.println("ex" + st);
            while (rs.next()) {
                String cardNo = rs.getString(COLUMN_CARD_NO);
                String idUser = rs.getString(TABLE_NAME + "." + COLUMN_ID_USER);
                String name = rs.getString(TABLE_NAME_USER + "." + COLUMN_NAME_USER);
                String mail = rs.getString(TABLE_NAME_USER + "." + COLUMN_MAIL_USER);
                String phone = rs.getString(TABLE_NAME_USER + "." + COLUMN_PHONE_USER);
                Date regDate = rs.getDate(COLUMN_REG_DATE);
                Date expried = rs.getDate(COLUMN_EXPIRED_DATE);
                model.insertRow(model.getRowCount(), new Object[]{idUser, cardNo, regDate, expried});
            }

        } catch (SQLException ex) {
            System.out.println("ex" + ex.getMessage());
            return CardController.RESULT_SQLITE;
        }
        if (model.getRowCount() > 0) {
            return CardController.RESULT_FOUND;
        } else {
            return CardController.RESULT_NOT_FOUND;
        }
    }

    /**
     * Tìm kiếm thẻ mượn đã kích hoạt bằng tên độc giả hoặc mã độc giả hoặc mã
     * thẻ mượn và hiển thị lên bảng thông tin
     *
     * @param key từ khóa tìm kiếm
     * @param model model của bảng hiển thị thông tin
     * @return 1 nếu tìm thấy 1 vài kết quả 2 nếu không tìm thấy kết quả nào 3
     * nếu lỗi kết nối csdl , lỗi truy vấn
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int searchAllCardActiveByKey(String key, DefaultTableModel model) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement st = c.prepareStatement(GET_USER_HAVE_CARD_UN_ACTIVE_BY_KEY);
            st.setInt(1, Constants.STATUS_ACTIVED);
            st.setString(2, "%" + key + "%");
            st.setString(3, "%" + key + "%");
            st.setString(4, "%" + key + "%");
            ResultSet rs = st.executeQuery();
            model.setRowCount(0);
            System.out.println("ex" + st);
            while (rs.next()) {
                String cardNo = rs.getString(COLUMN_CARD_NO);

                String idUser = rs.getString(TABLE_NAME + "." + COLUMN_ID_USER);
                String name = rs.getString(TABLE_NAME_USER + "." + COLUMN_NAME_USER);
                String mail = rs.getString(TABLE_NAME_USER + "." + COLUMN_MAIL_USER);
                String phone = rs.getString(TABLE_NAME_USER + "." + COLUMN_PHONE_USER);
                Date regDate = rs.getDate(COLUMN_REG_DATE);
                Date expried = rs.getDate(COLUMN_EXPIRED_DATE);
                model.insertRow(model.getRowCount(), new Object[]{idUser, cardNo, regDate, expried});
            }

        } catch (SQLException ex) {
            System.out.println("ex" + ex.getMessage());
            return CardController.RESULT_SQLITE;
        }
        if (model.getRowCount() > 0) {
            return CardController.RESULT_FOUND;
        } else {
            return CardController.RESULT_NOT_FOUND;
        }
    }

    /**
     * Dùng để hủy kích hoạt thẻ
     *
     * @param cardNo mã thẻ mượn
     * @return 2 nếu hủy kích hoạt thành công 3 nếu thất bại do lỗi kết nối
     * csdl, lỗi câu truy vấn hoặc không tìm thấy thẻ.
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int deActiveCard(String cardNo) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pt = c.prepareStatement(DEACTIVE_CARD);
            pt.setString(1, cardNo);
            if (pt.executeUpdate() > 0) {
                return CardController.RESULT_SUCCESS;
            }
        } catch (SQLException e) {
            System.out.println("ex" + e.getMessage());
        }
        return CardController.RESULT_SQLITE;
    }

    /**
     * Dùng để cập nhật thông tin thẻ, cập nhật hạn thẻ
     *
     * @param cardNo mã thẻ mượn
     * @param expired ngày hết hạn cập nhật
     * @return 2 nếu hủy kích hoạt thành công 3 nếu thất bại do lỗi kết nối
     * csdl, lỗi câu truy vấn hoặc không tìm thấy thẻ.
     * @exception Lỗi về câu lệnh truy vấn , lỗi kết nối csdl
     */
    @Override
    public int updateCard(String cardNo, Date expired) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pt = c.prepareStatement(UPDATE_CARD);
            pt.setDate(1, expired);
            pt.setString(2, cardNo);
            if (pt.executeUpdate() > 0) {
                return CardController.RESULT_SUCCESS;
            }
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
        return CardController.RESULT_SQLITE;
    }

    @Override
    public boolean checkCardOverDueExpired(String cardNo) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pt = c.prepareStatement(CHECK_CARD_OVER_DUE);
            pt.setString(1, cardNo);
            ResultSet rs = pt.executeQuery();
            if (rs.next()) {
                Date expired = rs.getDate("expired");
                if (expired != null) {
                    if (expired.before(new java.util.Date())) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }

        } catch (Exception e) {
        }
        return true;
    }
}
