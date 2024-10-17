
package user.dao;

import sqlite.Mysql;
import user.model.Account;
import util.HashPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MysqlAccountDao implements AccountDao {

    private static MysqlAccountDao instance;

    private MysqlAccountDao() {
    }

    private static final String TABLE_NAME = "account";
    //columns of table Account.
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String INSERT_ACCOUNT = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_USERNAME + "," + COLUMN_PASSWORD + ")" + " VALUES (?,?)";
    private static final String FIND_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " =?";
    private static final String CHANGE_PASS = "UPDATE account SET account.password = ? WHERE account.username = ? ";

    public static final int LOGIN_WITH_READER = 0;
    public static final int LOGIN_WITH_LIBRARIAN = 1;
    public static final int LOGIN_WITH_ADMIN = 2;
       public static final int LOGIN_FAIL_CONECTION = 3;
    public static final int LOGIN_FAIL = 4;

    public static final int CHANGE_PASS_SUCCESS = 0;
    public static final int ACCOUNT_FALSE = 1;
    public static final int PASSAGAIN_NOT_SAME_PASSNEW = 2;
    public static final int FAILE_SYSTEM = 3;

    public static MysqlAccountDao getInstance() {
        if (instance == null) {
            instance = new MysqlAccountDao();
        }
        return instance;
    }

    @Override
    public boolean insert(Account account) {
        try {
            Connection c = Mysql.getInstance().getConnection();

            PreparedStatement pstmt = c.prepareStatement(INSERT_ACCOUNT);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, HashPassword.hashPass(account.getPassword()));

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

    @Override
    public int login(Account acc) {
        try {

            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(FIND_BY_ID);
            pstmt.setString(1, acc.getUsername());
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                String username = rset.getString(COLUMN_USERNAME);
                String password = rset.getString(COLUMN_PASSWORD);

                String hashLogin = HashPassword.hashPass(acc.getPassword());
                if (acc.getUsername().equals(username) && hashLogin.equals(password)) {
                    return LOGIN_WITH_READER;
                }
            }

        } catch (SQLException ex) {
            return LOGIN_FAIL_CONECTION;

        }
        return LOGIN_FAIL;
    }

    private Account createAccount(ResultSet rset) throws SQLException {
        Account acc = new Account(rset.getString(COLUMN_USERNAME),
                rset.getString(COLUMN_PASSWORD));
        return acc;
    }

    @Override
    public int changePass(Account account, String passNew, String passAgain) {
        if (login(account) != LOGIN_WITH_READER) {
            return ACCOUNT_FALSE;
        } else if (passAgain.equals(passNew) == false) {
            return PASSAGAIN_NOT_SAME_PASSNEW;
        } else {
            try {
                Connection c = Mysql.getInstance().getConnection();
                PreparedStatement pstm = c.prepareStatement(CHANGE_PASS);
                pstm.setString(1, HashPassword.hashPass(passNew));
                pstm.setString(2, account.getUsername());
                if (pstm.executeUpdate() > 0) {
                    return CHANGE_PASS_SUCCESS;
                }
            } catch (SQLException ex) {
                Logger.getLogger(MysqlAccountDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            return FAILE_SYSTEM;
        }
    }
}
