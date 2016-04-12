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
public class StudentDTO {
    private int studentId;
    private String name;
    private int status;
    
    public StudentDTO(int studentId, String name, int status){
        this.studentId = studentId;
        this.name = name;
        this.status = status;
    }
    
    public void setStudentId(int studentId){
        this.studentId = studentId;
    }
    
    public int getStudentId(){
        return this.studentId;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }    
    
    public void setStatus(int status){
        this.status = status;
    }
    
    public int getStatus(){
        return this.status;
    }
}
