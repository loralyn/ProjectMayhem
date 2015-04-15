package edu.uw.ProjectMayhem;

import java.io.Serializable;

/**
 * Created by Brian on 4/11/2015.
 */
public class User implements Serializable {

    private String mUserID;
    private String mEmail;
    private String mPassword;
    private String mSecurityQuestion;
    private String mSecurityAnswer;


    public User(String id,String email,String password,String question) {

        mUserID = id;
        mEmail = email;
        mPassword = password;
        mSecurityQuestion = question;
    }

    public String getUserID() {

        return mUserID;
    }

    public String getEmail() {

        return mEmail;
    }

    public String getSecurityQuestion() {

        return mSecurityQuestion;
    }

    public void saveUserInfo() {

    }

    public void clearUserInfo()  {

    }

}
