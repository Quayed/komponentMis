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
    
    ComponentDTO getComponent(int componentId, int publicToken) throws java.rmi.RemoteException;
    
    ComponentDTO getComponent(String barcode, int publicToken) throws RemoteException;
    
    ComponentDTO[] getComponents(int publicToken) throws RemoteException;
    
    // ComponentGroup
    
    ComponentGroupDTO getComponentGroup(int componentGroupId, int publicToken) throws java.rmi.RemoteException;
    
    ComponentGroupDTO[] getComponentGroups(int publicToken) throws java.rmi.RemoteException;
    
    // Loan
    
    int createLoan(LoanDTO loanDTO, int publicToken) throws java.rmi.RemoteException;

    LoanDTO getLoan(int loanId, int publicToken) throws java.rmi.RemoteException;
    
    LoanDTO[] getLoans(int publicToken) throws java.rmi.RemoteException;
    
    int updateLoan(LoanDTO loan, int publicToken) throws java.rmi.RemoteException;

    int deleteLoan(int loanId, int publicToken) throws java.rmi.RemoteException;
    
    
    // Student

    StudentDTO getStudent(String studentId, int publicToken) throws java.rmi.RemoteException;
    
    StudentDTO[] getStudents(int publicToken) throws java.rmi.RemoteException;
    
    // Tests
    
    StudentDTO getTest(int publicToken) throws java.rmi.RemoteException;
}
