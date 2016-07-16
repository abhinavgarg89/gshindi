package com.gshindi.android.testfirebase;

/**
 * Created by abhinavgarg on 16/07/16.
 */
public class UserPerformanceReport {
    private String email_;
    private String quesionSetId_;
    private int marks_;
    private int attempDate_;

    public UserPerformanceReport(String email, String quesionSetId, int marks, int attempDate) {
        email_ = email;
        quesionSetId_ = quesionSetId;
        marks_ = marks;
        attempDate_ = attempDate;
    }

    public UserPerformanceReport() {
    }

    public String getEmail() {
        return email_;
    }

    public String getQuesionSetId() {
        return quesionSetId_;
    }

    public int getMarks() {
        return marks_;
    }

    public int getAttempDate() {
        return attempDate_;
    }

    public void setEmail(String email) {
        email_ = email;
    }

    public void setQuesionSetId(String quesionSetId) {
        quesionSetId_ = quesionSetId;
    }

    public void setMarks(int marks) {
        marks_ = marks;
    }

    public void setAttempDate(int attempDate) {
        attempDate_ = attempDate;
    }
}
