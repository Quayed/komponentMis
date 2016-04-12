/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        if (student.getStudentId() == 0 && student.getName() == null && student.getStatus() == 0)
            return -1;
        
        String sql = "INSERT INTO " + DATABASE_NAME + "(";
        String sqlValues = "";
        
        if(student.getStudentId() != 0){
            sql += "studentId";
            sqlValues += "?";
        }
        
        if(student.getName() != null){
            if(sqlValues.equals("")){
                sql += "name";
                sqlValues += "?";
            }else{
                sql += ", name";
                sqlValues += ", ?";
            }
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
        
     
        try {
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;
            
            if(student.getStudentId() != 0)
                stm.setInt(param++, student.getStudentId());
            
            if (student.getName() != null)
                stm.setString(param++, student.getName());
            
            if(student.getStatus() != 0)
                stm.setInt(param++, student.getStatus());
            
            stm.execute();
            
            if(stm.getUpdateCount() != 1){
                return -2;
            }else
                return 1;
                
        } catch (SQLException e) {
            e.printStackTrace();
        }
     
        return -3;
    }

    @Override
    public StudentDTO getStudent(int studentId) {
        try{
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME + "WHERE id = ?");
            stm.setInt(1, studentId);
            ResultSet result = stm.executeQuery();
            while(result.next())
                return new StudentDTO(result.getInt("studentId"), result.getString("name"), result.getInt("status"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public StudentDTO[] getStudents() {
        try{
            PreparedStatement stm = CONN.prepareStatement("SELECT * FROM " + DATABASE_NAME);
            ArrayList<StudentDTO> students = new ArrayList<>();
            ResultSet result = stm.executeQuery();
            while(result.next())
                students.add(new StudentDTO(result.getInt("studentId"), result.getString("name"), result.getInt("status")));

            return students.toArray(new StudentDTO[students.size()]);
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int updateStudent(StudentDTO student) {
        if(student.getStudentId() == 0 || (student.getName() == null && student.getStatus() == 0))
            return -1;

        String sql = "UPDATE " + DATABASE_NAME + " SET ";
        String sqlValues = "";

        if (student.getName() != null) {
            sqlValues += "name = ?";
        }

        if(student.getStatus() != 0){
            if(sqlValues.equals("")){
                sqlValues += "status = ?";
            }else{
                sqlValues += ", status = ?";
            }
        }

        sql += sqlValues + " WHERE studentId = ?";

        try{
            PreparedStatement stm = CONN.prepareStatement(sql);
            int param = 1;

            if(student.getName() != null)
                stm.setString(param++, student.getName());

            if(student.getStatus() != 0)
                stm.setInt(param++, student.getStatus());

            stm.setInt(param++, student.getStudentId());

            stm.execute();

            if(stm.getUpdateCount() != 1){
                return -2;
            }else{
                return 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return -3;
    }

    @Override
    public int deleteStudent(int studentId) {
        if(studentId == 0)
            return -1;

        try{
            PreparedStatement stm = CONN.prepareStatement("DELETE FROM " + DATABASE_NAME + "WHERE studentId = ?");
            stm.setInt(1, studentId);
            stm.execute();

            if(stm.getUpdateCount() == 1){
                return 1;
            }else{
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -3;
    }
    
}
