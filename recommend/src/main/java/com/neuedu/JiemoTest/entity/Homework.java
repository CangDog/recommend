package com.neuedu.recommend.entity;

public class Homework extends HomeworkKey {
    private Integer homeworktime;

    private String homeworkintroduction;

    private Integer spare1;

    private Integer spare2;

    private String spare3;

    private String spare4;

    public Integer getHomeworktime() {
        return homeworktime;
    }

    public void setHomeworktime(Integer homeworktime) {
        this.homeworktime = homeworktime;
    }

    public String getHomeworkintroduction() {
        return homeworkintroduction;
    }

    public void setHomeworkintroduction(String homeworkintroduction) {
        this.homeworkintroduction = homeworkintroduction == null ? null : homeworkintroduction.trim();
    }

    public Integer getSpare1() {
        return spare1;
    }

    public void setSpare1(Integer spare1) {
        this.spare1 = spare1;
    }

    public Integer getSpare2() {
        return spare2;
    }

    public void setSpare2(Integer spare2) {
        this.spare2 = spare2;
    }

    public String getSpare3() {
        return spare3;
    }

    public void setSpare3(String spare3) {
        this.spare3 = spare3 == null ? null : spare3.trim();
    }

    public String getSpare4() {
        return spare4;
    }

    public void setSpare4(String spare4) {
        this.spare4 = spare4 == null ? null : spare4.trim();
    }
}