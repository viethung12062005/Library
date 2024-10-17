
package registeterm.model;

import card.model.BorrowCard;
import registeterm.dao.MysqlRegisterTerrmDao;
import registeterm.dao.RegisterTermDao;

import java.sql.Date;
import java.util.List;

public class RegisterBorrow {

    private int id;

    public RegisterBorrow(int id, String idcard, String name, Date regDate, List<RegisterTermDetail> details) {
        this.id = id;
        this.idcard = idcard;
        this.name = name;
        this.regDate = regDate;
        this.details = details;
    }

    public RegisterBorrow() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public List<RegisterTermDetail> getDetails() {
        return details;
    }

    public void setDetails(List<RegisterTermDetail> details) {
        this.details = details;
    }
    private String idcard;
    private String name;
    private Date regDate;
    private List<RegisterTermDetail> details;

    private RegisterTermDao regDao() {
        return MysqlRegisterTerrmDao.getInstance();
    }

    public List<RegisterBorrow> getAll() {
        return regDao().getAll();
    }

    public int borrowBook(RegisterBorrow reg, String idlib, Date startDate, Date endDate, double money) {
        return regDao().borrowBook(reg, idlib, startDate, endDate, money);
    }

    public boolean checkOverDueBook(String cardNo) {
        return regDao().checkBookBorrowOverDue(cardNo);
    }

    public int getCountBookBorrow(String cardNo) {
        return regDao().getCountBookBorrow(cardNo);
    }

    public BorrowCard getCardByIdCard(String cardNo) {
        return regDao().getCardByIdCard(cardNo);
    }

    public List<RegisterBorrow> findRegByKey(String key) {
        return regDao().findRegByKey(key);
    }

    public List<RegisterTermDetail> getAllRegisDetailById(int id) {
        return regDao().getAllRegisDetailById(id);
    }
    
    public int getCountLoan(){
        return regDao().getCountLoan();
    }
}
