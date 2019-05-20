package com.system.po;

/**
 * Created by Jacey on 2017/6/29.
 */
public class SelectedCourseCustom extends Selectedcourse {
    //新增Student 对象字段
    private StudentCustom studentCustom;

    //扩展课程信息对象
    private CourseCustom courseCustom;

    //判断该学生是否已经完成该课程
    private Boolean over = false;
    
    private Integer regulargrade;
    
    private Integer boardscores;
    
    public Integer getRegulargrade(){
    	return regulargrade;
    }
    
    public void setRegulargrade(Integer regulargrade) {
    	this.regulargrade=regulargrade;
    }
    
    public Integer getBoardscores(){
    	return boardscores;
    }
    
    public void setBoardscores(Integer boardscores) {
    	this.boardscores=boardscores;
    }

    public Boolean getOver() {
        return over;
    }

    public void setOver(Boolean over) {
        this.over = over;
    }

    public StudentCustom getStudentCustom() {
        return studentCustom;
    }

    public void setStudentCustom(StudentCustom studentCustom) {
        this.studentCustom = studentCustom;
    }

    public CourseCustom getCouseCustom() {
        return courseCustom;
    }

    public void setCouseCustom(CourseCustom couseCustom) {
        this.courseCustom = couseCustom;
    }
}
