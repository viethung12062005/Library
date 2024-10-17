/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarian.controller;

import com.mysql.cj.util.StringUtils;
import librarian.model.Librarian;
import util.Utils;

import java.util.Arrays;


public class LibrarianController {

    Librarian librarian;
    public static final int RESULT_NULL_POINT = 0;
    public static final int RESULT_PASS_SMALL = 1;
    public static final int RESULT_EMAIL_INCORRECT = 2;
    public static final int RESULT_PHONE_INCORRECT = 3;
    public static final int RESULT_ID_EXITS = 4;
    public static final int RESULT_SUCCESS = 6;
    public static final int RESULT_SQLITE = 7;

    public LibrarianController() {
        librarian = new Librarian();
    }

    public int changePassLib(Librarian librarian, String passNew, String passAgain) {
        return this.librarian.changePassLib(librarian, passNew, passAgain);
    }

    public int insertUser(String id, String name, String email, String phone, char[] passfield) {
        String pass = Arrays.toString(passfield);
        if (StringUtils.isNullOrEmpty(id) || StringUtils.isNullOrEmpty(name)
                || StringUtils.isNullOrEmpty(email)
                || StringUtils.isNullOrEmpty(phone)
                || StringUtils.isNullOrEmpty(pass)) {
            return RESULT_NULL_POINT;
        }
        if (!Utils.isAdressEmail(email)) {
            return RESULT_EMAIL_INCORRECT;
        }
        if (!Utils.isPhoneNumber(phone)) {
            return RESULT_PHONE_INCORRECT;
        }
        if (passfield.length < 6) {
            return RESULT_PASS_SMALL;
        }
        if (librarian.checkIdExist(id)) {
            return RESULT_ID_EXITS;
        }
        librarian.setId(id);
        librarian.setPass(pass);
        librarian.setName(name);
        librarian.setEmail(email);
        librarian.setPhone(phone);
        return librarian.save();
    }
}
