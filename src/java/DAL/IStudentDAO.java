/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

/**
 *
 * @author mathias
 */
public interface IStudentDAO {
    int createStudent(StudentDTO student);
    StudentDTO getStudent(int studentId);
    StudentDTO[] getStudents();
    int updateStudent(StudentDTO student);
    int deleteStudent(int studentId);
}
