package com.srms.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String avatar;
    private String password;
    private String year;
    private String semester;
    @ElementCollection
    private List<String> subjects = new ArrayList<>(); // ObjectId strings
    private String username;
    private String registrationNumber;
    private String gender;
    private String fatherName;
    private String motherName;
    private String department;
    private String division;
    private String batch;
    private String academicYear;
    private Long contactNumber;
    private Long fatherContactNumber;
    private String dob;
    private Boolean passwordUpdated = false;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public List<String> getSubjects() { return subjects; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }
    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Long getContactNumber() { return contactNumber; }
    public void setContactNumber(Long contactNumber) { this.contactNumber = contactNumber; }
    public Long getFatherContactNumber() { return fatherContactNumber; }
    public void setFatherContactNumber(Long fatherContactNumber) { this.fatherContactNumber = fatherContactNumber; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public Boolean getPasswordUpdated() { return passwordUpdated; }
    public void setPasswordUpdated(Boolean passwordUpdated) { this.passwordUpdated = passwordUpdated; }
}
