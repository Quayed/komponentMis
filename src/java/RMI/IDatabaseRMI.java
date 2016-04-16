/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import DAL.ComponentDTO;
import DAL.ComponentGroupDTO;
import DAL.LoanDTO;
import DAL.StudentDTO;

/**
 *
 * @author hippomormor
 */
public interface IDatabaseRMI extends java.rmi.Remote {
    
    // Component
    
    ComponentDTO getComponent(int componentId) throws java.rmi.RemoteException;
    
    ComponentDTO[] getComponents() throws java.rmi.RemoteException;
    
    int updateComponent(ComponentDTO component) throws java.rmi.RemoteException;
    
    
    // ComponentGroup

    ComponentGroupDTO getComponentGroup(int componentGroupId) throws java.rmi.RemoteException;
    
    ComponentGroupDTO[] getComponentGroups() throws java.rmi.RemoteException;
    
    int updateComponentGroups(ComponentGroupDTO componentGroup) throws java.rmi.RemoteException;
    
    
    // Loan
 
    int createLoan(LoanDTO loan) throws java.rmi.RemoteException;

    LoanDTO getLoan(int loanId) throws java.rmi.RemoteException;

    LoanDTO[] getLoans() throws java.rmi.RemoteException;

    int updateLoan(LoanDTO loan) throws java.rmi.RemoteException;

    int deleteLoan(int loanId) throws java.rmi.RemoteException;
    
    
    // Student

    StudentDTO getStudent(String studentId) throws java.rmi.RemoteException;

    StudentDTO[] getStudents() throws java.rmi.RemoteException;
    
}
