package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String test;
    private String subjectCode;
    private String department;
    private Integer totalMarks = 10;
    @Column(name = "study_year")
    private String year;
    private String semester;
    private String division;
    private String date;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTest() { return test; }
    public void setTest(String test) { this.test = test; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
