/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;

/**
 * @author mathias
 */
public class StudentDTO implements Serializable {
    private String studentId;
    private String name;
    private int status;

    public StudentDTO() {

    }

    public StudentDTO(String studentId, String name, int status) {
        this.studentId = studentId;
        this.name = name;
        this.status = status;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
