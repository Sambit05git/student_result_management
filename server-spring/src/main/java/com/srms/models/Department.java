package com.srms.models;

import jakarta.persistence.*;


@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String department;
    private String departmentCode;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }
}
