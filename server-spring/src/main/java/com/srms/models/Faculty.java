package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String username;
    private String email;
    private String avatar;
    private String password;
    private String registrationNumber;
    private String gender;
    private String designation;
    private String department;
    private Long contactNumber;
    private String dob;
    private Integer joiningYear;
    private Boolean passwordUpdated = false;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Long getContactNumber() { return contactNumber; }
    public void setContactNumber(Long contactNumber) { this.contactNumber = contactNumber; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public Integer getJoiningYear() { return joiningYear; }
    public void setJoiningYear(Integer joiningYear) { this.joiningYear = joiningYear; }
    public Boolean getPasswordUpdated() { return passwordUpdated; }
    public void setPasswordUpdated(Boolean passwordUpdated) { this.passwordUpdated = passwordUpdated; }
}
