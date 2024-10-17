/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registeterm.controller;

import card.model.BorrowCard;
import com.mysql.cj.util.StringUtils;
import registeterm.dao.MysqlRegisterTerrmDao;
import registeterm.model.RegisterBorrow;
import registeterm.model.RegisterTermDetail;
import util.Constants;
import util.Utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class RegisterTermController {

    private RegisterBorrow model;
    private List<RegisterBorrow> listModel;

    public List<RegisterBorrow> getListModel() {
        return listModel;
    }

    public RegisterTermController() {
        model = new RegisterBorrow();
        listModel = new ArrayList<>();
        listModel = model.getAll();
    }

    public void displayAllReg(DefaultTableModel regModelTable) {
        if (regModelTable != null) {

            if (listModel != null) {
                for (int i = 0; i < listModel.size(); i++) {
                    RegisterBorrow reg = listModel.get(i);
                    Date regDate = reg.getRegDate();
                    Date deadlineDate = new Date(regDate.getTime() + 2 * 24 * 3600 * 1000);
                    String statusReg;
                    if(deadlineDate.before(new java.util.Date())){
                        statusReg = Constants.STATUS_REG_OVER_DUE;
                    }else{
                        statusReg= Constants.STATUS_REG_YES;
                    }
                    regModelTable.insertRow(i, new Object[]{reg.getId(),
                        reg.getIdcard(), reg.getName(), reg.getRegDate(),statusReg});
                }
            }
        }
    }

    public void displayDetailReg(int rowClick, DefaultTableModel regModelTable, DefaultTableModel detailModelTable) {
        System.out.println("" + rowClick + " " + listModel.size());
        if (rowClick >= 0 && rowClick < listModel.size() && detailModelTable != null) {
            int idReg = (Integer) regModelTable.getValueAt(rowClick, 0);
            List<RegisterTermDetail> details = getAllRegisDetailById(idReg);
            detailModelTable.setRowCount(0);
            if (details != null) {
                listModel.get(rowClick).getDetails().clear();
                listModel.get(rowClick).setDetails(details);
                for (int i = 0; i < details.size(); i++) {
                    RegisterTermDetail loanDetail = details.get(i);
                    detailModelTable.insertRow(i, new Object[]{loanDetail.getId(), loanDetail.getIdCopy(), loanDetail.getTitle()});
                }
            }

        }
    }
    
    /**
     * Dùng để thực hiện chức năng cho mượn sách từ phiếu đăng ký mượn
     * @param regisTable bảng chứa thông tin chi tiết của phiếu mượn
     * @param startDate ngày mượn  
     * @param endDate ngày trả
     * @param idlib mã thủ thư 
     * @param money tiền cọc
     * @return 
     *          0 nếu thủ thư chưa chọn phiếu để tiến hành cho mượn
     *          1 nếu các trường thông tin chưa đủ
     *          2 nếu tiền cọc không hợp lệ 
     *          3 nếu hạn trả không hợp lệ
     *          4 nếu thẻ chưa kích hoạt
     *          5 nếu thẻ đã quá hạn
     *          6 nếu độc giả có sách quá hạn chưa trả
     *          7 nếu tổng của sách chuẩn bị mượn và sách độc giả đã mượn lớn hơn 5
     *          8 mượn thành công
     *          9 mượn thất bại do lỗi kết nối csdl
     *          
     */
    public int borrowBook(JTable regisTable, String idCard, String idlib, Date startDate, java.util.Date endDate, String money) {
        if (StringUtils.isEmptyOrWhitespaceOnly(idCard) || StringUtils.isEmptyOrWhitespaceOnly(idlib) || StringUtils.isEmptyOrWhitespaceOnly(money)
                || startDate == null || endDate == null || regisTable == null) {
            return MysqlRegisterTerrmDao.RESULT_NULL_POINT;
        }


        if (!Utils.isStringDouble(money)) {
            return MysqlRegisterTerrmDao.RESULT_MONEY_WRONG;

        }

        if (endDate.before(new Date(System.currentTimeMillis()))) {
            return MysqlRegisterTerrmDao.RESULT_END_DATE;
        }

        BorrowCard borrowCard = model.getCardByIdCard(idCard);
        if (borrowCard != null) {
            if (borrowCard.getStatus() == Constants.STATUS_UNACTIVED) {
                return MysqlRegisterTerrmDao.RESULT_CARD_NO_ACTIVE;
            }

            if (borrowCard.getExpiredDate().before(startDate)) {
                return MysqlRegisterTerrmDao.RESULT_OVER_DUE_CARD;
            }
        }

        if (model.checkOverDueBook(idCard)) {
            return MysqlRegisterTerrmDao.RESULT_OVER_DUE_BORROW;
        }

        if (regisTable.getSelectedRow() < 0 && regisTable.getSelectedRow() > listModel.size()) {

            return MysqlRegisterTerrmDao.RESULT_NO_SELECTED;
        } else {
            int count = model.getCountBookBorrow(idCard);

            if (count + listModel.get(regisTable.getSelectedRow()).getDetails().size() > 5) {

                return MysqlRegisterTerrmDao.RESULT_MAX_COUNT_BORROW;

            }

            return model.borrowBook(listModel.get(regisTable.getSelectedRow()), idlib, startDate, new Date(endDate.getTime()), Double.parseDouble(money));
        }

    }

    public List<RegisterBorrow> searchRegisByKey(String key) {

        return model.findRegByKey(key);
    }

    public List<RegisterTermDetail> getAllRegisDetailById(int id) {
        return model.getAllRegisDetailById(id);
    }

    public void disPlayListSearch(String key, DefaultTableModel regModelTable) {
        if (regModelTable != null) {
            regModelTable.setRowCount(0);
            List<RegisterBorrow> resultSearch = searchRegisByKey(key);
            if (resultSearch != null) {
                listModel.clear();
                listModel.addAll(resultSearch);
                
                for (int i = 0; i < resultSearch.size(); i++) {
                     RegisterBorrow reg = listModel.get(i);
                    Date regDate = reg.getRegDate();
                    Date deadlineDate = new Date(regDate.getTime() + 2 * 24 * 3600 * 1000);
                    String statusReg;
                    if(deadlineDate.before(new java.util.Date())){
                        statusReg = Constants.STATUS_REG_OVER_DUE;
                    }else{
                        statusReg= Constants.STATUS_REG_YES;
                    }
                    regModelTable.insertRow(i, new Object[]{resultSearch.get(i).getId(), resultSearch.get(i).getIdcard(), resultSearch.get(i).getName(), resultSearch.get(i).getRegDate(),statusReg});
                }
            }

        }

    }

    public int getCountLoan(){
        return model.getCountLoan();
    }
}
