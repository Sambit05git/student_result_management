package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Marks {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String exam; // ObjectId string
    private String student; // ObjectId string
    private Integer marks = -1;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getExam() { return exam; }
    public void setExam(String exam) { this.exam = exam; }
    public String getStudent() { return student; }
    public void setStudent(String student) { this.student = student; }
    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }
}
