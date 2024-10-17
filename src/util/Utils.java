
package util;

import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;


public class Utils {
    
    public static String format(Date date) {
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }


    public static Date convertDate(java.util.Date date) {
        return date != null ? new Date(date.getTime()) : null;
    }

    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isStringInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public static boolean isAdressEmail(String email) {
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(email_pattern);
    }
    
    public static boolean isPhoneNumber(String phone){
        String phone_pattern ="^0(1\\d{9}|9\\d{8})";
        return phone.matches(phone_pattern);
    }

}
