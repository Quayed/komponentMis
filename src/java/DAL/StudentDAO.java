/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.Connection;

/**
 *
 * @author mathias
 */
public class StudentDAO implements IStudentDAO{

    private final Connection CONN;
    private final String DATABASE_NAME = "Student";
    
    public StudentDAO(Connection conn){
        this.CONN = conn;
    }
    
    @Override
    public int createStudent(StudentDTO student) {
        if (student.getName() == null && student.getStatus() == 0)
            return -1;
        
        String sql = "INSERT INTO " + DATABASE_NAME + "(";
        String sqlValues = "";
        
        if(student.getName() != null){
            sql += "studentId";
            sqlValues += "?";
        }
        
        if(student.getStatus() != 0){
            if(sqlValues.equals("")){
                sql += "status";
                sqlValues += "?";
            }else{
                sql+= ", status";
                sqlValues += ", ?";
            }
        }
        
        sql += ") VALUES ( " + sqlValues + ")";
        
        return 0;
    
    }

    @Override
    public StudentDTO getStudent(int studentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StudentDTO[] getStudents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int updateStudent(StudentDTO student) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteStudent(int studentId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
