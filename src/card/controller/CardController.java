
package card.controller;

import card.dao.MysqlBorrowCardDao;
import card.model.BorrowCard;
import com.mysql.cj.util.StringUtils;
import util.Constants;
import util.Utils;

import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.Random;


public class CardController {

    public static final int RESULT_NULL_POINT = 0;
    public static final int RESULT_FOUND = 1;
    public static final int RESULT_EXPRIED_FAIL = 1;
    public static final int RESULT_NOT_FOUND = 2;
    public static final int RESULT_SQLITE = 3;
    public static final int RESULT_SUCCESS = 2;

    public CardController() {
        model = new BorrowCard();
    }
    BorrowCard model;


    public int showInforCardRegisted(DefaultTableModel tableModel) {
        if (tableModel == null) {
            return RESULT_NULL_POINT;
        }
        return model.getInforCardRegisted(tableModel);
    }
    
    /**
     * Hiển thị toàn bộ các thẻ mượn đã kích hoạt
     * @param tableModel bảng hiển thị thông tin thẻ
     * @return 0 nếu không tồn tại bảng này
     *         1 nếu tìm được 1 hoặc 1 số thẻ
     *         2 nếu không tìm thấy thẻ nào
     *         3 lỗi kết nối csdl
     */
    public int showAllCardActive(DefaultTableModel tableModel) {
        if (tableModel == null) {
            return RESULT_NULL_POINT;
        }
        return model.getAllCardActive(tableModel);
    }

    public String generateIdCard() {
        return model.generateIdCard();
    }
    /**
     * Sinh ngẫu nhiên chuỗi số dùng cho việc kích hoạt thẻ thành viên
     * @return chuỗi số kích hoạt giá trị từ 100000 đến 999999
     */
    public String generateCodeActivateCard() {
        Random random = new Random();
        int code = random.nextInt(999999 - 100000 + 1) + 100000;
        return String.valueOf(code);
    }
    

    public int activateCard(String idUser, Date actDate, Date expiredDate, String codeActivate, String cardNo) {
        return 0;
    }

    public String getCardNoById(String idCard) {
        return model.getCardNoById(idCard);
    }

    public BorrowCard getBorrowCardById(String idCard) {
        return model.getBorrowCardByUsername(idCard);
    }

    public String getNameCardById(String id) {
        return model.getNameCardById(id);
    }

    public int getStatusCardById(String idCard) {
        return model.getStatusCardById(idCard);
    }

    public int checkUseOverDueBookCopy(String idCard) {
        return model.checkUserOverDueBookCopy(idCard);
    }

    public BorrowCard getBorrowCardByUsername(String id) {
        return model.getBorrowCardByUsername(id);
    }

    public int searchCardUnActive(String key, DefaultTableModel modelTable) {
        return 0;
    }

    public int searchCardActive(String key, DefaultTableModel modelTable) {

        return 0;
    }

    public int deActiveCard(String cardNo) {

        return 0;
    }

    public int updateCard(String cardNo, Date expired) {

        return 0;
    }
}
