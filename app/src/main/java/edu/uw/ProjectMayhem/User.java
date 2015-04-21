/**
 * Project Mayhem: Jacob Hohisel, Loralyn Solomon, Brian Plocki, Brandon Soto
 */
package edu.uw.ProjectMayhem;

import java.io.Serializable;

/**
 * Stores data about an app user.
 */
public final class User implements Serializable {

    /**
     * Unique ID for the user.
     */
    private String mUserID;

    /**
     * The user's email address.
     */
    private String mEmail;

    /**
     * The user's passsword.
     */
    private String mPassword;

    /**
     * The selected security question.
     */
    private String mSecurityQuestion;

    /**
     * The answer for the security question.
     */
    private String mSecurityAnswer;

    /**
     * Constructs a user with the given id, email, password, security question, and security answer.
     *
     * @param id       the unique ID for the user
     * @param email    the user's email address
     * @param password the user's password
     * @param question the user's security question
     * @param answer   the answer to the security question
     */
    public User(String id, String email, String password, String question, String answer) {

        mUserID = id;
        mEmail = email;
        mPassword = password;
        mSecurityQuestion = question;
        mSecurityAnswer = answer;
    }

    /**
     * @return the user's unique ID.
     */
    public String getUserID() {
        return mUserID;
    }

    /**
     * @return the user's email address.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @return the user's security question.
     */
    public String getSecurityQuestion() {
        return mSecurityQuestion;
    }

    /**
     * To DO
     */
    public void saveUserInfo() {

    }


    /**
     * TO DO
     */
    public void clearUserInfo() {

    }

}
