/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DTO.ComponentDTO;
import DTO.ComponentGroupDTO;
import DTO.LoanDTO;
import DTO.StudentDTO;
import java.rmi.RemoteException;


/**
 *
 * @author hippomormor
 */
public interface IDatabaseRMI extends java.rmi.Remote {
    
    // Token-stuff
    
    int generateToken(int randomToken)  throws java.rmi.RemoteException;
    
    // Component
    
    ComponentDTO getComponent(int componentId) throws java.rmi.RemoteException;
    
    ComponentDTO getComponent(String barcode) throws RemoteException;
    
    ComponentDTO[] getComponents() throws RemoteException;
    
    // ComponentGroup
    
    ComponentGroupDTO getComponentGroup(int componentGroupId) throws java.rmi.RemoteException;
    
    ComponentGroupDTO[] getComponentGroups() throws java.rmi.RemoteException;
    
    // Loan
    
    void createLoan(LoanDTO loanDTO) throws java.rmi.RemoteException;

    LoanDTO getLoan(int loanId) throws java.rmi.RemoteException;
    
    LoanDTO[] getLoans() throws java.rmi.RemoteException;

    int deleteLoan(int loanId) throws java.rmi.RemoteException;
    
    
    // Student

    StudentDTO getStudent(String studentId) throws java.rmi.RemoteException;
    
    StudentDTO[] getStudents() throws java.rmi.RemoteException;
    
    // Tests
    
    StudentDTO getTest() throws java.rmi.RemoteException;
}
