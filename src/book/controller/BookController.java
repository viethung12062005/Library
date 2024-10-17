package book.controller;

import book.model.Book;
import category.model.Category;
import com.mysql.cj.util.StringUtils;
import publisher.model.Publisher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import static book.dao.MysqlBookDao.BOOK_ADD_SAME;
import static bookcopy.dao.MysqlBookCopyDao.RESULT_NULL_POINT;

public class BookController {

    private List<Category> catList;
    private List<Publisher> pubList;
    private Book model;

    public BookController() {
        model = new Book.BookBuilder().build();
        catList = new ArrayList<>();
        pubList = new ArrayList<>();
    }

    /**
     * Tìm kiếm sách theo mã sách, tiêu đề, tác giả, nhà xuất bản, thể loại
     *
     * @param idSearch Mã sách
     * @param titleSearch Tiêu đề
     * @param authorSearch Tác giả
     * @param publishSearch Nhà xuất bản
     * @param categorySearch Thể loại sách
     * @return Danh sách các sách thỏa mãn yêu cầu tìm kiếm
     */
    public List<Book> searchBook_Registe(String idSearch, String titleSearch, String authorSearch, String publishSearch, String categorySearch) {
        System.out.println(idSearch + ";" + titleSearch + ";" + authorSearch + ";" + publishSearch + ";" + categorySearch);
        return model.searchBook_Registe(idSearch, titleSearch, authorSearch, publishSearch, categorySearch);
    }

    /**
     * Tìm kiếm sách theo mã sách, tiêu đề, tác giả, nhà xuất bản, thể loại
     *
     * @param idSearch Mã sách
     * @param titleSearch Tiêu đề
     * @param authorSearch Tác giả
     * @param publishSearch Nhà xuất bản
     * @param categorySearch Thể loại sách
     * @return Danh sách các sách thỏa mãn yêu cầu tìm kiếm
     */
    public List<Book> searchBook_Manager(String idSearch, String titleSearch, String authorSearch, String publishSearch, String categorySearch) {
        System.out.println(idSearch + ";" + titleSearch + ";" + authorSearch + ";" + publishSearch + ";" + categorySearch);
        return model.searchBook_Manager(idSearch, titleSearch, authorSearch, publishSearch, categorySearch);
    }

    /**
     * Dùng để cập nhật thông tin của sách
     *
     * @param id
     * @param title
     * @param author
     * @param publisher
     * @return 0 nếu thiếu 1 trong số các thông tin 1 nếu cập nhật thành công 2
     * nếu đã có 1 quyển sách giống hệt thông tin bạn cần sửa 3 lỗi kết nối
     * csdl, lỗi câu truy vấn
     */
    public int updateInforBook(String id, String title, String author, String publisher, String category) {
        if (StringUtils.isNullOrEmpty(id)
                || StringUtils.isNullOrEmpty(title)
                || StringUtils.isNullOrEmpty(author) || StringUtils.isNullOrEmpty(publisher)) {
            return RESULT_NULL_POINT;
        } else if (model.checkBookSame(id, title, author, publisher, category)) {
            return BOOK_ADD_SAME;
        } else {
            return model.updateInforBook(id, title, author, publisher, category);
        }
    }

    //Lấy toàn bộ thông tin sách
    public List<Book> getAllBook() {
        return model.getAllBook();
    }

    public void displayBookListToTable(JTable jtb, List<Book> listBook) {
        DefaultTableModel dtm = (DefaultTableModel) jtb.getModel();
        dtm.setNumRows(0);
        if (listBook.isEmpty()) {
            dtm.addRow(new Object[]{"", "", "Không có sách nào được tìm thấy", "", "", ""});
        } else {
            for (int i = 0; i < listBook.size(); i++) {
                Book book = listBook.get(i);
                dtm.addRow(new Object[]{book.getId(), book.getTitle(),
                    book.getAuthor(), book.getPublisher().getPublisher(),
                    book.getCategory().getCat(), book.getListCopies().size()});
            }
        }
        jtb.setModel(dtm);
    }

    /**
     * Dùng để thêm 1 sách mới
     *
     * @param title
     * @param author
     * @param publisher
     * @param category
     * @return 0 nếu thiếu 1 trong số các thông tin 1 nếu thêm thành công 2 nếu
     * đã có 1 quyển sách giống hệt thông tin bạn cần thêm 3 lỗi kết nối csdl,
     * lỗi câu truy vấn
     */
    public int addBook(String title, String author, String publisher, String category) {
        String idPub = getIdPubByPublisher(publisher);
        String idCat = getIdCatByCategory(category);
        String id = generateIdBook(idCat);
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(title)
                || StringUtils.isNullOrEmpty(author) || StringUtils.isNullOrEmpty(idPub)
                || StringUtils.isNullOrEmpty(idCat)) {
            return RESULT_NULL_POINT;
        } else if (model.checkBookSame(id, title, author, idPub, idCat)) {
            return BOOK_ADD_SAME;
        } else {
            return model.addBook(id, title, author, idPub, idCat);
        }
    }

    public void addBookToTable(JTable jtb, Book book) {
        DefaultTableModel dtm = (DefaultTableModel) jtb.getModel();
        dtm.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getPublisher().getPublisher(), book.getCategory().getCat()});
    }

 

    public void updateInforBookInTable(JTable jtb, int index, Book bookUpdate) {
        DefaultTableModel dtm = (DefaultTableModel) jtb.getModel();
        dtm.setValueAt(bookUpdate.getTitle(), index, 1);
        dtm.setValueAt(bookUpdate.getAuthor(), index, 2);
        dtm.setValueAt(bookUpdate.getPublisher().getPublisher(), index, 3);
        jtb.setModel(dtm);
    }

    /**
     * Sinh mã sách để cho chức năng thêm sách
     *
     * @param idCat mã category dùng để phân loại sách , ví dụ Công Nghệ thông
     * tin sẽ có mã category là IT , sách loại này sẽ có mã sách bắt đầu bằng IT
     * @return mã sách cần cho việc thêm
     */
    public String generateIdBook(String idCat) {
        return model.generateIdBook(idCat);
    }

    public String getIdPubByPublisher(String publisher) {
        return model.getIdPubByPublisher(publisher);
    }

    public String getIdCatByCategory(String category) {
        return model.getIdCatByCategory(category);
    }
}
