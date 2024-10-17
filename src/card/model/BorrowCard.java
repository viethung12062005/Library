/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package card.model;

import card.dao.CardDao;
import card.dao.MysqlBorrowCardDao;
import user.model.User;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.util.List;


public class BorrowCard {

    private String id;
    private int status;
    private Date expiredDate;
    private String cardNo;
    private Date regisDate;
    private Date actDate;

    public BorrowCard(String id, int status, Date expiredDate, String cardNo, Date regisDate, Date actDate) {
        this.id = id;
        this.status = status;
        this.expiredDate = expiredDate;
        this.cardNo = cardNo;
        this.regisDate = regisDate;
        this.actDate = actDate;
    }

    public Date getRegisDate() {
        return regisDate;
    }

    public void setRegisDate(Date regisDate) {
        this.regisDate = regisDate;
    }

    public Date getActDate() {
        return actDate;
    }

    public void setActDate(Date actDate) {
        this.actDate = actDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BorrowCard(String id, String cardNo, int status, Date expiredDate) {
        this.id = id;
        this.status = status;
        this.expiredDate = expiredDate;
        this.cardNo = cardNo;
    }

    public BorrowCard() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expityDate) {
        this.expiredDate = expityDate;
    }

    public boolean save() {
        return cardDao().insert(this);
    }

    private CardDao cardDao() {
        return MysqlBorrowCardDao.getInstance();
    }

    public int getInforCardRegisted(DefaultTableModel model) {
        return cardDao().getInforCardRegisted(model);
    }

    public String generateIdCard() {
        return cardDao().generateIdCard();
    }

    public int activateCard() {
        return cardDao().activateCard(this);
    }

    public String getCardNoById(String idCard) {
        return cardDao().getCardNoById(idCard);
    }

    public String getNameCardById(String id) {
        return cardDao().getNameCardById(id);
    }

    public int getStatusCardById(String idCard) {
        return cardDao().getStatusCardById(idCard);
    }

    public int checkUserOverDueBookCopy(String idCard) {
        return cardDao().checkUserOverDueBookCopy(idCard);
    }

    public BorrowCard getBorrowCardByUsername(String id) {
        return cardDao().getBorrowCardByUsername(id);
    }

    public List<String> getAllIdCard() {
        return cardDao().getAllIdCard();
    }

    public User getUserByCardNo(String cardNo) {
        return cardDao().getUserByCardNo(cardNo);
    }

    public int getCountBookBorrowAndRegisted(String cardNo) {
        return cardDao().getCountBookBorrowAndRegisted(cardNo);
    }

    public int getStatusCardByCardNo(String cardNo) {
        return cardDao().getStatusCardByCardNo(cardNo);
    }
    public boolean checkCardOverDueExpired(String cardNo){
        return cardDao().checkCardOverDueExpired(cardNo);
    }

    public int searchCardUnActive(String key, DefaultTableModel modelTableModel) {
        return cardDao().searchCardUnActive(key, modelTableModel);

    }

    public int getAllCardActive(DefaultTableModel modelTableModel) {
        return cardDao().getAllCardActive(modelTableModel);
    }

    public int searchAllCardActiveByKey(String key, DefaultTableModel modelTableModel) {
        return cardDao().searchAllCardActiveByKey(key, modelTableModel);
    }

    public int deActiveCard(String cardNo) {
        return cardDao().deActiveCard(cardNo);
    }

    public int updateCard(String cardNo, Date expired) {

        return cardDao().updateCard(cardNo, expired);
    }
}
