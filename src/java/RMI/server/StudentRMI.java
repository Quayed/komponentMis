/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI.server;

import DAL.StudentDAO;
import RMI.IStudentRMI;
import java.sql.Connection;

/**
 *
 * @author hippomormor
 */
public class StudentRMI implements IStudentRMI{

    private final String studentId;
    private final StudentDAO student;

    public StudentRMI(Connection conn, String id) {
        studentId = id;
        student = new StudentDAO(conn);
    }

    @Override
    public String getStudentId() {
        return student.getStudent(studentId).getStudentId();
    }

    @Override
    public String getName() {
        return student.getStudent(studentId).getName();
    }

    @Override
    public void setStatus(int status) {
        student.getStudent(studentId).setStatus(status);
    }

    @Override
    public int getStatus() {
        return student.getStudent(studentId).getStatus();
    }
 
}
