
package util;


public class Constants {

    public static final String TYPE_BORROWABLE = "Có thể mượn";
    public static final String TYPE_REGISTEBORROW = "Đã đăng ký";
    public static final String TYPE_BORROWED = "Đã mượn";

    public static final String TYPE_REFERENCES = "Tham khảo";
    public static final String STATUS_BORROWABLE_STRING = "Có thể mượn";
    public static final String STATUS_REGISTED_STRING = "Đã đăng ký";
    public static final String STATUS_REGISTED_IN_LIBRARY_STRING = "Đã đăng ký tại thư viện";
    public static final String STATUS_BORROWED_STRING = "Đã mượn";
    public static final String STATUS_BORROWABLE1 = "Hiện có";
    public static final int STATUS_BORROWABLE = 0;
    public static final int STATUS_REGISTED = 1;
    public static final int STATUS_REGISTED_IN_LIBRARY = 2;
    public static final int STATUS_BORROWED = 3;

    public static final String SEX_MALE = "Nam";
    public static final String SEX_FEMALE = "Nữ";
    public static final String SEX_OTHER = "Khác";
    public static final String STUDENT = "Sinh Viên Hust";
    public static final String OTHER = "Khác";
    public static final int TYPE_READER = 0;
    public static final int TYPE_LIBRARIAN = 1;
    public static final int TYPE_ADMIN = 2;

    public static final String LOGIN_WITH_ADMIN = "admin";
    public static final String LOGIN_WITH_USER = "user";
    public static final String LOGIN_WITH_LIBRARIAN = "librarian";
    public static final int STATUS_UNACTIVED = 0;
    public static final int STATUS_ACTIVED = 1;

    public static final String DEFAULT_CARD_NO_REGISTER = "NULL";
    public static final String DEFAULT_CARD_NO_REGISTER_IN_TABLE = "Chưa có";

    public static final String STATUS_REG_OVER_DUE = "Quá hạn mượn";

    public static final String STATUS_REG_YES = "Có thể sử dụng";

    public static final int NO_SELECT_ROW_TABLE = 0;
    public static final int NO_ACTIVE_CARD = 1;
    public static final int EXPIRED_CARD = 2;
    public static final int OVER_DUE_BOOKCOPY = 3;
    public static final int BORROWABLE = 4;
    public static final int LIST_BOOKCOPY_BORROWBYID_IS_NULL = 5;
    public static final int BORROW_BIGGER_BOOKCOPYABLE = 6;
    public static final int BORROW_OVER_FIVE_BOOKCOPY = 7;
    public static final int BORROW_OVER_FIVE_BOOKCOPY_BOTH_REGISTED_BOOK = 8;
    public static final int BORROW_OVER_FIVE_BOOKCOPY_BOTH_BORROWED = 9;
    public static final int BORROW_OVER_FIVE_BOOKCOPY_BOTH_REGISTED_BOOK_AND_BORROWED = 10;
    public static final int SUCCESS = 11;
}
