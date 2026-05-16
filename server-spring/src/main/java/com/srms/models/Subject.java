package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String subjectName;
    private String subjectCode;
    private String department;
    private Integer totalLectures = 10;
    private String year;
    private String semester;
    private String attendence; // ObjectId string, kept the typo in the name intentionally to match nodejs model

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getTotalLectures() { return totalLectures; }
    public void setTotalLectures(Integer totalLectures) { this.totalLectures = totalLectures; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public String getAttendence() { return attendence; }
    public void setAttendence(String attendence) { this.attendence = attendence; }
}
