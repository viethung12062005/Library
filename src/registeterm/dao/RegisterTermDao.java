
package registeterm.dao;

import card.model.BorrowCard;
import registeterm.model.RegisterBorrow;
import registeterm.model.RegisterTermDetail;

import java.sql.Date;
import java.util.List;


public interface RegisterTermDao {

    List<RegisterBorrow> getAll();

    boolean checkBookBorrowOverDue(String cardNo);

    int getCountBookBorrow(String cardNo);

    int borrowBook(RegisterBorrow reg, String idlib, Date startDate, Date endDate, double money);

    BorrowCard getCardByIdCard(String cardNo);

    List<RegisterBorrow> findRegByKey(String key);

    List<RegisterTermDetail> getAllRegisDetailById(int id);
    
    int getCountLoan();
}
