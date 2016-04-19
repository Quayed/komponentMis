/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DTO.StudentDTO;

/**
 * @author mathias
 */
public interface IStudentDAO {
    int createStudent(StudentDTO student);

    StudentDTO getStudent(String studentId);

    StudentDTO[] getStudents();

    int updateStudent(StudentDTO student);

    int deleteStudent(String studentId);
}
