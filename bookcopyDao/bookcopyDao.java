package bookCopyDao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import bookCopyModel.BookCopy;

public interface BookCopyDao {

    /**
     * Tạo mã sách dựa trên danh mục.
     */
    String generateBookId(String category);

    /**
     * Thêm số lượng bản sao sách với giá và loại được chỉ định.
     */
    int addBookCopies(String id, int countCopy, double price, String type);

    /**
     * Lấy loại của bản sao sách dựa trên mã bản sao.
     */
    Optional<String> getTypeBookCopy(String idCopy);

    /**
     * Lấy trạng thái của bản sao sách dựa trên mã bản sao.
     */
    Optional<Integer> getStatusBookCopy(String idCopy);

    /**
     * Đăng ký sách với các chi tiết được cung cấp.
     */
    boolean createRegisterBook(int id, String idCard, String name, Date dateNow);

    /**
     * Đăng ký bản sao sách cụ thể bằng mã và tiêu đề.
     */
    boolean registerBookCopy(int id, String idCopy, String title);

    /**
     * Hủy đăng ký bản sao sách dựa trên mã.
     */
    boolean unregisterBookCopy(String idCopy);

    /**
     * Hủy đăng ký tất cả bản sao sách cho một mã thẻ cụ thể.
     */
    boolean unregisterAllBookCopies(String idCard);

    /**
     * Lấy mã đăng ký liên kết với bản sao sách.
     */
    Optional<Integer> getRegisteredIdByBookCopyId(String idCopy);

    /**
     * Lấy số lượng bản sao sách có cùng mã đăng ký.
     */
    int getCountBookCopySameRegisteredId(int idRegistered);

    /**
     * Cập nhật trạng thái của bản sao sách.
     */
    boolean updateBookCopyStatus(String idCopy, int status);

    /**
     * Lấy tất cả bản sao sách cho mã sách cụ thể.
     */
    List<BookCopy> getBookCopiesById(String id);

    /**
     * Lấy tổng số bản sao sách dựa trên mã sách.
     */
    Optional<Integer> getBookCopyCountById(String id);

    /**
     * Lấy số lượng bản sao sách đã đăng ký dựa trên số thẻ.
     */
    int getRegisteredBookCopyCountByCardNo(String cardNo);

    /**
     * Lấy danh sách các bản sao sách có thể mượn dựa trên mã sách.
     */
    List<BookCopy> getBorrowableBookCopiesById(String bookId);

    /**
     * Lấy số lượng bản sao sách lớn nhất dựa trên mã sách.
     */
    Optional<Integer> getMaxBookCopyCountByBookId(String id);

    /**
     * Lấy mã danh mục dựa trên mã sách.
     */
    Optional<String> getCategoryCodeById(String id);

    /**
     * Tạo mã đăng ký mới.
     */
    int generateRegisterId();

    /**
     * Lấy số lượng bản sao sách chưa thanh toán và không quá hạn dựa trên mã thẻ.
     */
    int getUnpaidBookCopyCountNoOutDate(String idCard);

    /**
     * Lấy danh sách mã đăng ký bản sao sách dựa trên số thẻ.
     */
    List<Integer> getRegisteredBookCopyIds(String cardNo);

    /**
     * Lấy tất cả mã sách.
     */
    List<String> getAllBookIds();

    /**
     * Lấy danh sách bản sao sách theo số lượng.
     */
    List<BookCopy> getBookCopyListByCount(String id, int count);

    /**
     * Cập nhật trạng thái đăng ký trong thư viện.
     */
    boolean updateRegisteredStatusInLibrary(int status, String idCopy);
    
    /**
     * Cập nhật thông tin bản sao sách.
     */
    int updateBookCopyInfo(String idCopy, String status, String oldType, String newType, double price);
}
