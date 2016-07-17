package com.gshindi.android.testfirebase;

/**
 * Created by abhinavgarg on 06/07/16.
 */
public class User {
    private String firstName_;
    private String lastName_;
    private String emailId_;
    private int phoneNumber_;

    public User() {
      /*Blank default constructor essential for Firebase*/
    }

    public User(String firstName, String lastName, String emailId, int phoneNumber) {
        firstName_ = firstName;
        lastName_ = lastName;
        emailId_ = emailId;
        phoneNumber_ = phoneNumber;
    }

    public String getFirstName() {
        return firstName_;
    }

    public void setFirstName(String firstName) {
        firstName_ = firstName;
    }

    public String getLastName() {
        return lastName_;
    }

    public void setLastName(String lastName) {
        lastName_ = lastName;
    }

    public String getEmailId() {
        return emailId_;
    }

    public void setEmailId(String emailId) {
        emailId_ = emailId;
    }

    public int getPhoneNumber() {
        return phoneNumber_;
    }

    public void setPhoneNumber(int phoneNumber) {
        phoneNumber_ = phoneNumber;
    }
}
