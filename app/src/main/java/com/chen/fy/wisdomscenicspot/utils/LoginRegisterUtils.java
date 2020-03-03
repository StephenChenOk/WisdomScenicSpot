package com.chen.fy.wisdomscenicspot.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.fy.wisdomscenicspot.application.MyApplication;
import com.chen.fy.wisdomscenicspot.model.Manager;
import com.chen.fy.wisdomscenicspot.model.Visitor;

import org.litepal.LitePal;

import java.security.MessageDigest;
import java.util.List;

/**
 * 登入,注册账号时的工具类
 */
public class LoginRegisterUtils {


    /**
     * 注册账号时判断账号是否可以使用
     * @param userId      用户名
     */
    public static boolean userAvailable(String userId ) {
        if (userId.isEmpty()) {
            Toast.makeText(MyApplication.getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        List<Visitor> users = LitePal.select("userId").find(Visitor.class);
        for (Visitor user : users) {
            if (user.getUserId().equals(userId)) {
                //如果账号已经存在
                Toast.makeText(MyApplication.getContext(), "账号已经存在", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * 注册时判断两次密码输入是否一致
     * @param password1  密码1
     * @param password2  密码2
     */
    public static boolean passwordSame(String password1, String password2) {
        if (password1.isEmpty() || password2.isEmpty()) {
            Toast.makeText(MyApplication.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password1.equals(password2)) {
            //密码相等
            return true;
        } else {
            Toast.makeText(MyApplication.getContext(), "两次密码不相等", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 登入时判断是否有当前用户名
     * @param userId      用户名
     * @param loginType   登入类型(游客端或者管理员端)
     */
    public static boolean userExisted(String userId, int loginType) {
        if (userId.isEmpty()) {
            Toast.makeText(MyApplication.getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (loginType) {
            case 1:  //游客
                List<Visitor> users = LitePal.select("userId").find(Visitor.class);
                for (Visitor user : users) {
                    if (userId.equals(user.getUserId())) {
                        return true;
                    }
                }
                break;
            case 2:  //管理者
                List<Manager> managers = LitePal.select("userId").find(Manager.class);
                for (Manager manager : managers) {
                    if (userId.equals(manager.getUserId())) {
                        return true;
                    }
                }
                break;
        }
        Toast.makeText(MyApplication.getContext(), "此用户名不存在", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 登入时判断密码是否与用户名对应
     * @param userId     用户名
     * @param password   密码
     * @param loginType  登入类型
     */
    public static boolean passwordCorrected(String userId, String password, int loginType) {
        if (password.isEmpty()) {
            Toast.makeText(MyApplication.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (loginType) {
            case 1:    //游客
                List<Visitor> users = LitePal.where("userId = ?", userId).find(Visitor.class);
                for (Visitor user : users) {
                    //判断密码是否相同
                    if (user.getPwHash().equals(getMD5(password + user.getPwSalt()))) {
                        return true;
                    }
                }
                break;
            case 2:    //管理者
                List<Manager> managers = LitePal.where("userId = ?", userId).find(Manager.class);
                for (Manager manager : managers) {
                    //判断密码是否相同
                    if (manager.getPwHash().equals(getMD5(password + manager.getPwSalt()))) {
                        return true;
                    }
                }
                break;
        }
        Toast.makeText(MyApplication.getContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * MD5加密密码
     * @param s   要加密的密码
     */
    public static String getMD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 禁止EditText输入空格
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" "))
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}