/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.dao;

import sqlite.Mysql;
import user.model.User;
import util.HashPassword;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MysqlUserDao implements UserDao {

    //columns of table User.
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SEX = "sex";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PERIOD = "period";
    private static final String COLUMN_STUDENT_ID = "studentId";

    //SQL COMMAND
    private static final String INSERT_STUDENT = "INSERT INTO user (id,name,sex,studentId,email,phone, period) VALUES (?,?,?,?,?,?,?)";
    private static final String INSERT_ACCOUNT = "INSERT INTO account(username,password) VALUES (?,?)";
    private static final String INSERT_CARD = "INSERT INTO borrowcard (id,cardno,status,regdate) VALUES (?,?,?,?)";
    private static final String ALL = "SELECT * FROM user";
    private static final String FIND_BY_ID = "SELECT * FROM user WHERE id = ?";
    private static final String GET_NAME_BY_ID = "SELECT name FROM user WHERE id = ?";
    private static final String SEARCH_USER_BY_KEYWORD = "SELECT * FROM `user` WHERE id LIKE ? "
            + "OR name LIKE ? OR sex LIKE ? OR email LIKE ? "
            + "OR phone LIKE ? OR period like ?";
    private static final String UPDATE_INFOR_USER = "UPDATE user SET name =?, sex =?, email =?, phone =? WHERE id =?";
    private static final String CHANGE_PASS_USER = "UPDATE account SET password = ? WHERE username = ?";
    public static final int RESULT_LOGIN_ADMIN = -1;
    public static final int RESULT_NULL_POINT = 0;
    public static final int RESULT_PASS_SMALL = 1;
    public static final int RESULT_EMAIL_INCORRECT = 2;
    public static final int RESULT_PHONE_INCORRECT = 3;
    public static final int RESULT_ID_EXITS = 4;
    public static final int RESULT_NO_CHECK_TERM = 5;
    public static final int RESULT_SUCCESS = 6;
    public static final int RESULT_SQLITE = 7;

    public static final int RESULT_ID_NOT_EXITS = 1;
    public static final int RESULT_PASS_TOO_SMALL = 2;
    public static final int RESULT_PASS_ERROR_CONFIRM = 3;

    @Override
    public int insert(User user) {
        try {
            boolean isSuccess = true;
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(INSERT_STUDENT);

            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSex());
            pstmt.setString(4, user.getStudentId());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getPeriod());
            if (pstmt.executeUpdate() > 0) {
                pstmt.close();

            } else {
                isSuccess = false;
            }

            PreparedStatement pstmt2 = c.prepareStatement(INSERT_ACCOUNT);
            pstmt2.setString(1, user.getAccount().getUsername());
            pstmt2.setString(2, HashPassword.hashPass(user.getAccount().getPassword()));
            if (pstmt2.executeUpdate() > 0) {
                pstmt2.close();

            } else {
                isSuccess = false;
            }

            PreparedStatement pstmt3 = c.prepareStatement(INSERT_CARD);
            pstmt3.setString(1, user.getCard().getId());
            pstmt3.setString(2, user.getCard().getCardNo());
            pstmt3.setBoolean(3, false);
            java.sql.Date regDate = new java.sql.Date(System.currentTimeMillis());
            pstmt3.setDate(4, regDate);
            if (pstmt3.executeUpdate() > 0) {
                pstmt3.close();

            } else {
                isSuccess = false;
            }
            if (isSuccess) {
                return RESULT_SUCCESS;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
            return RESULT_SQLITE;
        }

        return RESULT_SQLITE;
    }

    @Override
    public List<User> getAll() {

        List<User> list = new ArrayList<>();

        try {
            Connection c = Mysql.getInstance().getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(ALL);
            while (rs.next()) {
                User user = new User.Builder()
                        .setId(rs.getString(COLUMN_ID))
                        .setName(rs.getString(COLUMN_NAME))
                        .setSex(rs.getString(COLUMN_SEX))
                        .setEmail(rs.getString(COLUMN_EMAIL))
                        .setPhone(rs.getString(COLUMN_PHONE))
                        .setStudentId(rs.getString(COLUMN_STUDENT_ID))
                        .setPeriod(rs.getString(COLUMN_PERIOD)).build();
                list.add(user);
                // print the results
            }
            st.close();
            return list;

        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public User findById(String id) {

        try {

            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(FIND_BY_ID);
            pstm.setString(1, id);
            System.out.println("Find" + pstm);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {

                User user = new User.Builder()
                        .setId(rs.getString(COLUMN_ID))
                        .setName(rs.getString(COLUMN_NAME))
                        .setSex(rs.getString(COLUMN_SEX))
                        .setEmail(rs.getString(COLUMN_EMAIL))
                        .setPhone(rs.getString(COLUMN_PHONE))
                        .setStudentId(rs.getString(COLUMN_STUDENT_ID))
                        .setPeriod(rs.getString(COLUMN_PERIOD)).build();
                return user;
            }
            System.out.println("Find" + pstm);

        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @Override
    public String getNameById(String id) {
        String name = "";
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(GET_NAME_BY_ID);
            pstm.setString(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
            }
            return name;

        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static void main(String[] args) {
        MysqlUserDao mud = new MysqlUserDao();
        System.out.println(mud.getNameById("kienvv2903"));
    }

    @Override
    public List<User> searchUser(String keyword) {
        List<User> listUsers = new ArrayList<>();

        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(SEARCH_USER_BY_KEYWORD);
            pstm.setString(1, "%" + keyword + "%");
            pstm.setString(2, "%" + keyword + "%");
            pstm.setString(3, "%" + keyword + "%");
            pstm.setString(4, "%" + keyword + "%");
            pstm.setString(5, "%" + keyword + "%");
            pstm.setString(6, "%" + keyword + "%");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                User user = new User.Builder()
                        .setId(rs.getString(COLUMN_ID))
                        .setName(rs.getString(COLUMN_NAME))
                        .setSex(rs.getString(COLUMN_SEX))
                        .setEmail(rs.getString(COLUMN_EMAIL))
                        .setPhone(rs.getString(COLUMN_PHONE))
                        .setPeriod(rs.getString(COLUMN_PERIOD)).build();
                listUsers.add(user);
            }
            return listUsers;

        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int updateInforUser(User userUpdate) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(UPDATE_INFOR_USER);
            pstm.setString(1, userUpdate.getName());
            pstm.setString(2, userUpdate.getSex());
            pstm.setString(3, userUpdate.getEmail());
            pstm.setString(4, userUpdate.getPhone());
            pstm.setString(5, userUpdate.getId());
            int check = pstm.executeUpdate();
            if (check > 0) {
                return RESULT_SUCCESS; //6 thành công
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RESULT_SQLITE; //7 lỗi
    }

    @Override
    public int changePass(String id, String newPass) {
        try {
            Connection c = Mysql.getInstance().getConnection();
            PreparedStatement pstm = c.prepareStatement(CHANGE_PASS_USER);
            pstm.setString(1, HashPassword.hashPass(newPass));
            pstm.setString(2, id);
            int check = pstm.executeUpdate();
            if (check > 0) {
                return RESULT_SUCCESS; //6 thành công
            }
        } catch (SQLException ex) {
            Logger.getLogger(MysqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RESULT_SQLITE; //7 lỗi
    }
}
