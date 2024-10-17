/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarian.dao;

import librarian.controller.LibrarianController;
import librarian.model.Librarian;
import sqlite.Mysql;
import user.dao.MysqlAccountDao;
import util.HashPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static user.dao.MysqlAccountDao.*;
import static user.dao.MysqlUserDao.RESULT_SQLITE;
import static user.dao.MysqlUserDao.RESULT_SUCCESS;

/**
 *
 * @author Linh
 */
public class MysqlLibrarianDao implements LibrarianDao {
    
    private static MysqlLibrarianDao instance;
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PASS = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String FIND_LIBRARIAN_BY_ID = "SELECT * FROM librarian WHERE id =?";
    private static final String CHANGE_PASS = "UPDATE librarian SET password = ? WHERE id = ? ";
    private static final String CHECK_ID_LIB_EXIST = "SELECT COUNT(*) FROM librarian WHERE id =?";
    private static final String INSERT_LIBRARIAN = "INSERT INTO librarian(id,password,name,email,phone) VALUES(?,?,?,?,?)";
    
    private MysqlLibrarianDao() {
    }
    
    public static MysqlLibrarianDao getInstance() {
        if (instance == null) {
            instance = new MysqlLibrarianDao();
        }
        return instance;
    }
    
    @Override
    public int loginWithLibrarian(Librarian lib) {
        try {
            
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(FIND_LIBRARIAN_BY_ID);
            pstmt.setString(1, lib.getId());
            System.out.println("" + pstmt);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                String username = rset.getString(COLUMN_ID);
                String password = rset.getString(COLUMN_PASS);
                String hashLogin = HashPassword.hashPass(lib.getPass());
                if (lib.getId().equals(username) && hashLogin.equals(password)) {
                    return LOGIN_WITH_LIBRARIAN;
                }
            }
            
        } catch (SQLException ex) {
            System.out.println("" + ex.getMessage());
        }
        return LOGIN_FAIL;
    }
    
    @Override
    public int changePassLib(Librarian lib, String passNew, String passAgain) {
        if (loginWithLibrarian(lib) != LOGIN_WITH_LIBRARIAN) {
            return ACCOUNT_FALSE;
        } else if (passAgain.equals(passNew) == false) {
            return PASSAGAIN_NOT_SAME_PASSNEW;
        } else {
            try {
                Connection c = Mysql.getInstance().getConnection();
                PreparedStatement pstm = c.prepareStatement(CHANGE_PASS);
                pstm.setString(1, HashPassword.hashPass(passNew));
                pstm.setString(2, lib.getId());
                if (pstm.executeUpdate() > 0) {
                    return CHANGE_PASS_SUCCESS;
                }
            } catch (SQLException ex) {
                Logger.getLogger(MysqlAccountDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            return FAILE_SYSTEM;
        }
    }
    
    @Override
    public boolean checkIdExist(String idLib) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(CHECK_ID_LIB_EXIST);
            pstmt.setString(1, idLib);
            System.out.println("" + pstmt);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                if (rset.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
        }
        return false;
    }
    
    @Override
    public int chagePass(String id, String passNew) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(CHANGE_PASS);
            pstm.setString(1, HashPassword.hashPass(passNew));
            pstm.setString(2, id);
            if (pstm.executeUpdate() > 0) {
                return RESULT_SUCCESS;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlAccountDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RESULT_SQLITE;
    }
    
    @Override
    public int insert(Librarian lib) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement ps = c.prepareStatement(INSERT_LIBRARIAN);
            ps.setString(1, lib.getId());
            ps.setString(2, HashPassword.hashPass(lib.getPass()));
            ps.setString(3, lib.getName());
            ps.setString(4, lib.getEmail());
            ps.setString(5, lib.getPhone());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            return LibrarianController.RESULT_SQLITE;
        }
        return LibrarianController.RESULT_SUCCESS;
    }
    
}
