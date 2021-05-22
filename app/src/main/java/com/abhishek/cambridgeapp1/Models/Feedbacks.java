package com.abhishek.cambridgeapp1.Models;

public class Feedbacks {

    private String subjectId;
    private String subjectName;
    private double feedbackScore1;
    private double feedbackScore2;
    private double feedbackScore3;
    private double feedbackScore4;
    private double feedbackScore5;
    private double feedbackScore6;
    private double feedbackScore7;
    private double feedbackScore8;
    private double feedbackScore9;
    private double feedbackScore10;
    private double no_of_feedbacks;

    public Feedbacks() {
    }

    public Feedbacks(String subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.feedbackScore1 = 0;
        this.feedbackScore2 = 0;
        this.feedbackScore3 = 0;
        this.feedbackScore4 = 0;
        this.feedbackScore5 = 0;
        this.feedbackScore6 = 0;
        this.feedbackScore7 = 0;
        this.feedbackScore8 = 0;
        this.feedbackScore9 = 0;
        this.feedbackScore10 = 0;
        this.no_of_feedbacks = 0;
    }

    public Feedbacks(String subjectId, String subjectName, double feedbackScore1, double feedbackScore2,
                     double feedbackScore3, double feedbackScore4, double feedbackScore5, double feedbackScore6, double feedbackScore7,
                     double feedbackScore8, double feedbackScore9, double feedbackScore10, double no_of_feedbacks) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.feedbackScore1 = feedbackScore1;
        this.feedbackScore2 = feedbackScore2;
        this.feedbackScore3 = feedbackScore3;
        this.feedbackScore4 = feedbackScore4;
        this.feedbackScore5 = feedbackScore5;
        this.feedbackScore6 = feedbackScore6;
        this.feedbackScore7 = feedbackScore7;
        this.feedbackScore8 = feedbackScore8;
        this.feedbackScore9 = feedbackScore9;
        this.feedbackScore10 = feedbackScore10;
        this.no_of_feedbacks = no_of_feedbacks;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public double getFeedbackScore1() {
        return feedbackScore1;
    }

    public void setFeedbackScore1(double feedbackScore1) {
        this.feedbackScore1 = feedbackScore1;
    }

    public double getFeedbackScore2() {
        return feedbackScore2;
    }

    public void setFeedbackScore2(double feedbackScore2) {
        this.feedbackScore2 = feedbackScore2;
    }

    public double getFeedbackScore3() {
        return feedbackScore3;
    }

    public void setFeedbackScore3(double feedbackScore3) {
        this.feedbackScore3 = feedbackScore3;
    }

    public double getFeedbackScore4() {
        return feedbackScore4;
    }

    public void setFeedbackScore4(double feedbackScore4) {
        this.feedbackScore4 = feedbackScore4;
    }

    public double getFeedbackScore5() {
        return feedbackScore5;
    }

    public void setFeedbackScore5(double feedbackScore5) {
        this.feedbackScore5 = feedbackScore5;
    }

    public double getFeedbackScore6() {
        return feedbackScore6;
    }

    public void setFeedbackScore6(double feedbackScore6) {
        this.feedbackScore6 = feedbackScore6;
    }

    public double getFeedbackScore7() {
        return feedbackScore7;
    }

    public void setFeedbackScore7(double feedbackScore7) {
        this.feedbackScore7 = feedbackScore7;
    }

    public double getFeedbackScore8() {
        return feedbackScore8;
    }

    public void setFeedbackScore8(double feedbackScore8) {
        this.feedbackScore8 = feedbackScore8;
    }

    public double getFeedbackScore9() {
        return feedbackScore9;
    }

    public void setFeedbackScore9(double feedbackScore9) {
        this.feedbackScore9 = feedbackScore9;
    }

    public double getFeedbackScore10() {
        return feedbackScore10;
    }

    public void setFeedbackScore10(double feedbackScore10) {
        this.feedbackScore10 = feedbackScore10;
    }

    public double getNo_of_feedbacks() {
        return no_of_feedbacks;
    }

    public void setNo_of_feedbacks(double no_of_feedbacks) {
        this.no_of_feedbacks = no_of_feedbacks;
    }
}
