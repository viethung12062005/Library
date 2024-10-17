/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.model;

import card.model.BorrowCard;
import user.dao.MysqlUserDao;
import user.dao.UserDao;

import java.util.List;


public class User {

    public User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.sex = builder.sex;
        this.email = builder.email;
        this.phone = builder.phone;
        this.studentId = builder.studentId;
        this.account = builder.account;
        this.card = builder.card;
        this.period = builder.period;
    }

    public User() {

    }
    private String id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPeriod() {
        return period;
    }

    private String name;
    private String sex;
    private String email;
    private String phone;
    private String studentId;

    public String getStudentId() {
        return studentId;
    }
    private String period;
    private BorrowCard card;

    public BorrowCard getCard() {
        return card;
    }

    public Account getAccount() {
        return account;
    }

    private Account account;

    public static class Builder {

        public User build() {
            return new User(this);
        }

        private String id;
        private String name;
        private String sex;
        private String email;
        private String phone;
        private String studentId;
        private String period;

        public Builder setPeriod(String period) {
            this.period = period;
            return this;
        }
        private Account account;
        private BorrowCard card;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSex(String sex) {
            this.sex = sex;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setStudentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder setAccount(Account account) {
            this.account = account;
            return this;
        }

        public Builder setCard(BorrowCard card) {
            this.card = card;
            return this;
        }

    }

    public int save() {
        return userDao().insert(this);
    }

    private static UserDao userDao() {
        return new MysqlUserDao();
    }

    public User findUserById(String id) {
        return userDao().findById(id);
    }

    public String getNameById(String id) {
        return userDao().getNameById(id);
    }

    public List<User> searchUser(String keyword) {
        return userDao().searchUser(keyword);
    }

    public int updateInforUser(User userUpdate) {
        return userDao().updateInforUser(userUpdate);
    }
    public int changePass(String id,String newPass){
        return userDao().changePass(id,newPass);
    }
//    public boolean  checkIdExist(String id){
//        return userDao().checkIdExist(String i)
//    }

}
