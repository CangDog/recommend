package com.neuedu.recommend.entity;

public class QuestionInPart extends QuestionInPartKey {
    private Integer grade;

    private Integer serialnum;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(Integer serialnum) {
        this.serialnum = serialnum;
    }
}