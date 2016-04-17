/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;


/**
 *
 * @author hippomormor
 */
public class StudentRMI {

    private String studentId;
    private String name;
    private int status;

    public StudentRMI() {

    }

    public StudentRMI(String studentId, String name, int status) {
        this.studentId = studentId;
        this.name = name;
        this.status = status;
    }
 
    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
 
}
