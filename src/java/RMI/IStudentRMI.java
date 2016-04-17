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
public interface IStudentRMI {

    public String getStudentId();

    public String getName();

    public void setStatus(int status);

    public int getStatus();
    
}
