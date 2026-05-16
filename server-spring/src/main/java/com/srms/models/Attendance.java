package com.srms.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attendances")
public class Attendance {
    @Id
    private String id;
    private String student; // ObjectId string
    private String subject; // ObjectId string
    private Integer totalLecturesByFaculty = 0;
    private Integer lectureAttended = 0;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStudent() { return student; }
    public void setStudent(String student) { this.student = student; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Integer getTotalLecturesByFaculty() { return totalLecturesByFaculty; }
    public void setTotalLecturesByFaculty(Integer totalLecturesByFaculty) { this.totalLecturesByFaculty = totalLecturesByFaculty; }
    public Integer getLectureAttended() { return lectureAttended; }
    public void setLectureAttended(Integer lectureAttended) { this.lectureAttended = lectureAttended; }
}
