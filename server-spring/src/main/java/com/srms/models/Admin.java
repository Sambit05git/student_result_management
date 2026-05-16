package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String email;
    private String password;
    private String username;
    private String department;
    private String dob;
    private String joiningYear;
    private String avatar;
    private Long contactNumber;
    private Boolean passwordUpdated = false;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getJoiningYear() { return joiningYear; }
    public void setJoiningYear(String joiningYear) { this.joiningYear = joiningYear; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Long getContactNumber() { return contactNumber; }
    public void setContactNumber(Long contactNumber) { this.contactNumber = contactNumber; }
    public Boolean getPasswordUpdated() { return passwordUpdated; }
    public void setPasswordUpdated(Boolean passwordUpdated) { this.passwordUpdated = passwordUpdated; }
}
