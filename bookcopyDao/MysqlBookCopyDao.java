package bookCopyDao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import bookCopyModel.BookCopy;

public class MysqlBookCopyDao implements BookCopyDao {

	@Override
	public String generateBookId(String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addBookCopies(String id, int countCopy, double price, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<String> getTypeBookCopy(String idCopy) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Integer> getStatusBookCopy(String idCopy) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean createRegisterBook(int id, String idCard, String name, Date dateNow) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registerBookCopy(int id, String idCopy, String title) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unregisterBookCopy(String idCopy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unregisterAllBookCopies(String idCard) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<Integer> getRegisteredIdByBookCopyId(String idCopy) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public int getCountBookCopySameRegisteredId(int idRegistered) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean updateBookCopyStatus(String idCopy, int status) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<BookCopy> getBookCopiesById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Integer> getBookCopyCountById(String id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public int getRegisteredBookCopyCountByCardNo(String cardNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BookCopy> getBorrowableBookCopiesById(String bookId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Integer> getMaxBookCopyCountByBookId(String id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<String> getCategoryCodeById(String id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public int generateRegisterId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUnpaidBookCopyCountNoOutDate(String idCard) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getRegisteredBookCopyIds(String cardNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllBookIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookCopy> getBookCopyListByCount(String id, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateRegisteredStatusInLibrary(int status, String idCopy) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int updateBookCopyInfo(String idCopy, String status, String oldType, String newType, double price) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
